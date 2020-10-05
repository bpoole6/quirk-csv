package com.quirk.csv.processor;

import com.quirk.csv.annotation.CSVReadBinding;
import com.quirk.csv.annotation.CSVReadComponent;
import com.quirk.csv.annotation.CSVType;
import com.quirk.csv.exception.MethodParameterException;
import com.quirk.csv.exception.NamedParserException;
import com.quirk.csv.exception.OrderParserException;
import com.quirk.csv.exception.WrapperInstantiationException;
import com.quirk.csv.wrappers.read.ReadWrapper;
import com.quirk.csv.wrappers.read.defaults.DefaultReadWrappers;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Has most of the logic for processing {@link com.quirk.csv.annotation.CSVReadComponent} annotated classes
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractCSVReadProcessor<T> {
    protected final Map<Class<ReadWrapper>, ReadWrapper> wrapperInstantiated = new HashMap<>();
    protected final Map<Class, ReadWrapper> setValueMap = new HashMap<>();
    protected final List<CSVReadAnnotationManager> csvReadAnnotationManagers = new ArrayList<>();
    protected final Class<T> parsedClazz;
    private final static Logger LOGGER = Logger.getLogger(AbstractCSVReadProcessor.class.getName());

    public AbstractCSVReadProcessor(Class<T> parsedClazz,
                                    Map<Class, ReadWrapper> wrapperMap) {
        CSVReadComponent csv = parsedClazz.getAnnotation(CSVReadComponent.class);
        this.parsedClazz = parsedClazz;
        List<Class> classes = new ArrayList<>();
        setValueMap.putAll(getWrapperMethodMap(wrapperMap));
        getClasses(parsedClazz, classes);
        csvReadAnnotationManagers.addAll(getHolders(classes));
        if (csv.type() == CSVType.NAMED) {
            List<String> duplicatedHeaders = this.csvReadAnnotationManagers.stream().collect(Collectors
                    .groupingBy(c -> c.getHeader())).values().stream().filter(l -> l.size() > 1)
                    .flatMap(l -> l.stream().map(h -> h.getHeader())).collect(Collectors.toList());
            if (duplicatedHeaders.size() > 0) {
                throw new NamedParserException(String.format("Non Unique Headers in %s. Duplicated Headers: %s",
                        parsedClazz, duplicatedHeaders));
            }
        } else if (csv.type() == CSVType.ORDER) {
            List<Integer> duplicatedOrders = this.csvReadAnnotationManagers.stream().collect(Collectors.groupingBy(c -> c.getOrder()))
                    .values().stream()
                    .filter(l -> l.size() > 1).flatMap(l -> l.stream().map(h -> h.getOrder())).collect(Collectors.toList());
            if (duplicatedOrders.size() > 0) {
                throw new OrderParserException(String.format("Non Unique Order assignment in %s. Duplicated Orders: %s",
                        parsedClazz, duplicatedOrders));
            }
        }
    }

    private Map<Class, ReadWrapper> getWrapperMethodMap(Map<Class, ReadWrapper> wrapperMap) {
        Map<Class, ReadWrapper> map = new HashMap<>(DefaultReadWrappers.getDefault());
        if (wrapperMap != null) {
            map.putAll(wrapperMap);
        }
        return map;
    }

    protected abstract <T> List<T> read(Reader reader,
                                        CSVFormat format) throws IOException;

    private List<CSVReadAnnotationManager> getHolders(List<Class> classes) {
        List<CSVReadAnnotationManager> combined = new ArrayList<>();
        for (Class clazz : classes) {
            combined.addAll(getFields(clazz));
            combined.addAll(getMethods(clazz));
        }
        return combined;
    }

    private List<CSVReadAnnotationManager> getFields(Class clazz) {
        List<CSVReadAnnotationManager> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(CSVReadBinding.class)) {
                int order = f.getAnnotation(CSVReadBinding.class).order();
                String header = f.getAnnotation(CSVReadBinding.class).header();
                boolean isNullable = f.getAnnotation(CSVReadBinding.class).isNullable();
                ReadWrapper readWrapper = getWrapper(f.getAnnotation(CSVReadBinding.class).wrapper());
                CSVReadAnnotationManager manager = new CSVReadAnnotationManager(order, header,
                        f, isNullable, readWrapper);

                list.add(manager);
            }
        }
        return list;
    }

    private List<CSVReadAnnotationManager> getMethods(Class clazz) {
        List<CSVReadAnnotationManager> list = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(CSVReadBinding.class)) {
                validateParameter(m);
                int order = m.getAnnotation(CSVReadBinding.class).order();
                String header = m.getAnnotation(CSVReadBinding.class).header();
                boolean isNullable = m.getAnnotation(CSVReadBinding.class).isNullable();
                ReadWrapper readWrapper = getWrapper(m.getAnnotation(CSVReadBinding.class).wrapper());
                CSVReadAnnotationManager manager = new CSVReadAnnotationManager(order, header,
                        m, isNullable, readWrapper);
                list.add(manager);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private ReadWrapper getWrapper(Class<? extends ReadWrapper> wrapper) {
        if (wrapper == null || wrapper == ReadWrapper.class)
            return null;
        ReadWrapper wrap = this.wrapperInstantiated.get(wrapper);
        if (wrap == null) {
            try {
                wrap = wrapper.newInstance();
                this.wrapperInstantiated.put((Class<ReadWrapper>) wrapper, wrap);

                return wrap;
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Make sure your ReadWrapper class has a public no args constructor", e);
                throw new WrapperInstantiationException("ReadWrapper class does not have public no args constructor");
            }

        }
        return wrap;
    }

    protected <T> void getClasses(Class<T> clazz, List<Class> classes) {
        CSVReadComponent csv = clazz.getAnnotation(CSVReadComponent.class);
        if(csv != null){
            classes.add(clazz);
            if (csv.inheritSuper()) {
                getClasses(clazz.getSuperclass(), classes);
            }
        }

    }

    protected void validateParameter(Method m) {
        if (m.getParameterCount() != 1)
            throw new MethodParameterException("Setter must only have one parameter:" + m.getName());
    }
}
