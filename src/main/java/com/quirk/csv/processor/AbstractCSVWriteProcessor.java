package com.quirk.csv.processor;


import com.quirk.csv.annotation.CSVType;
import com.quirk.csv.annotation.CSVWriteBinding;
import com.quirk.csv.annotation.CSVWriteComponent;
import com.quirk.csv.exception.*;
import com.quirk.csv.wrappers.write.WriteWrapper;
import com.quirk.csv.wrappers.write.defaults.DefaultWriteWrappers;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Has most of the logic for processing {@link CSVWriteComponent} annotated classes
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractCSVWriteProcessor<T> {

    protected final Map<Class<WriteWrapper>, WriteWrapper> wrapperInstantiated = new HashMap<>();
    protected final Map<Class, WriteWrapper> setValueMap = new HashMap<>();
    protected final List<CSVWriteAnnotationManager> csvAnnotationManagers = new ArrayList<>();
    protected final Class<T> parsedClazz;
    private final static Logger LOGGER = Logger.getLogger(AbstractCSVWriteProcessor.class.getName());

    public AbstractCSVWriteProcessor(Class<T> parsedClazz,
                                     Map<Class, WriteWrapper> wrapperMap) {

        this.parsedClazz = parsedClazz;
        List<Class> classes = new ArrayList<>();
        setValueMap.putAll(getWrapperMethodMap(wrapperMap));
        getClasses(parsedClazz, classes);
        csvAnnotationManagers.addAll(getHolders(classes));
        CSVWriteComponent component = parsedClazz.getAnnotation(CSVWriteComponent.class);
        List<Integer> duplicatedOrders = this.csvAnnotationManagers.stream().collect(Collectors.groupingBy(c -> c.getOrder())).values().stream().filter(l -> l.size() > 1)
                .flatMap(l -> l.stream().map(h -> h.getOrder())).distinct().collect(Collectors.toList());
        if (component.type() == CSVType.NAMED) {
            List<String> duplicatedHeaders = this.csvAnnotationManagers.stream().collect(Collectors.groupingBy(c -> c.getHeader())).values().stream().filter(l -> l.size() > 1)
                    .flatMap(l -> l.stream().map(h -> h.getHeader())).distinct().collect(Collectors.toList());
            if (duplicatedHeaders.size() > 0) {
                throw new NamedParserException(String.format("Non Unique Headers in %s. Duplicated Headers: %s", parsedClazz, duplicatedHeaders));
            }
            if (component.namedIsOrdered()) {
                if (duplicatedOrders.size() > 0) {
                    throw new NamedParserException(String.format("namedIsOrdered is set to true and non unique order assignment in %s. Duplicated Orders: %s", parsedClazz, duplicatedOrders));
                }
                csvAnnotationManagers.sort(Comparator.comparing(CSVWriteAnnotationManager::getOrder));
            }
        } else if (component.type() == CSVType.ORDER) {
            if (duplicatedOrders.size() > 0) {
                throw new OrderParserException(String.format("Non unique order assignment in %s. Duplicated Orders: %s", parsedClazz, duplicatedOrders));
            }
            csvAnnotationManagers.sort(Comparator.comparing(c -> c.getOrder()));
        }

    }

    private Map<Class, WriteWrapper> getWrapperMethodMap(Map<Class, WriteWrapper> wrapperMap) {
        Map<Class, WriteWrapper> map = new HashMap<>(DefaultWriteWrappers.getDefault());
        if (wrapperMap != null) {
            map.putAll(wrapperMap);
        }
        return map;
    }

    /**
     * Warning do not set org.apache.commons.csv.CSVFormat.DEFAULT.withFirstRecordAsHeader() if using Named Headers.
     * It will skip over the headers and not print them.
     *
     * @param objects
     * @param appendable
     * @param format
     * @throws IOException da
     */
    protected abstract void write(List<T> objects, Appendable appendable,
                                  CSVFormat format) throws IOException;

    private List<CSVWriteAnnotationManager> getHolders(List<Class> classes) {
        List<CSVWriteAnnotationManager> combined = new ArrayList<>();
        for (Class clazz : classes) {
            combined.addAll(getFields(clazz));
            combined.addAll(getMethods(clazz));
        }
        return combined;
    }

    private List<CSVWriteAnnotationManager> getFields(Class clazz) {
        List<CSVWriteAnnotationManager> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(CSVWriteBinding.class)) {
                int order = f.getAnnotation(CSVWriteBinding.class).order();
                String header = f.getAnnotation(CSVWriteBinding.class).header();
                WriteWrapper readWrapper = getWrapper(f.getAnnotation(CSVWriteBinding.class).wrapper());
                CSVWriteAnnotationManager manager = new CSVWriteAnnotationManager(order, header, f, readWrapper);
                list.add(manager);
            }
        }
        return list;
    }

    private List<CSVWriteAnnotationManager> getMethods(Class clazz) {
        List<CSVWriteAnnotationManager> list = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(CSVWriteBinding.class)) {
                validateParameter(m);
                int order = m.getAnnotation(CSVWriteBinding.class).order();
                String header = m.getAnnotation(CSVWriteBinding.class).header();
                WriteWrapper writeWrapper = getWrapper(m.getAnnotation(CSVWriteBinding.class).wrapper());
                CSVWriteAnnotationManager manager = new CSVWriteAnnotationManager(order, header, m, writeWrapper);
                list.add(manager);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private WriteWrapper getWrapper(Class<? extends WriteWrapper> wrapper) {
        if (wrapper == null || wrapper == WriteWrapper.class)
            return null;
        WriteWrapper wrap = this.wrapperInstantiated.get(wrapper);
        if (wrap == null) {
            try {
                wrap = wrapper.newInstance();
                this.wrapperInstantiated.put((Class<WriteWrapper>) wrapper, wrap);

                return wrap;
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Make sure your WriteWrapper class has a public no args constructor", e);
                throw new WrapperInstantiationException("WriteWrapper class does not have public no args constructor");
            }

        }
        return wrap;
    }

    protected <T> void getClasses(Class<T> clazz, List<Class> classes) {
        CSVWriteComponent csv = clazz.getAnnotation(CSVWriteComponent.class);
        if(csv != null){
            classes.add(clazz);
            if (csv.inheritSuper()) {
                getClasses(clazz.getSuperclass(), classes);
            }
        }

    }

    protected void validateParameter(Method m) {
        if (m.getParameterCount() != 0)
            throw new MethodParameterException(String.format("Method %s#%s must have zero parameters. Consider putting CSVWriteBinding on a getter.", m.getDeclaringClass(),m.getName()));
        if(m.getReturnType()==Void.TYPE){
            throw new MethodReturnTypeException(String.format("Method %s#%s return type cannot be void.", m.getDeclaringClass(),m.getName()));

        }
    }

    protected void doWrite(List<T> objects, Appendable appendable, CSVFormat csvFormat, Logger logger) throws IOException {

        try (CSVPrinter printer = new CSVPrinter(appendable, csvFormat);) {
            for(T obj : objects){
                List<String>values = new ArrayList<>();
                for(CSVWriteAnnotationManager cwm: this.csvAnnotationManagers){
                    values.add(cwm.getValue(obj,this.setValueMap));
                }
                printer.printRecord(values);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.log(Level.SEVERE,
                    "Could not extract value", e);
            throw new UninstantiableException(
                    "Could not extract value", e);
        }
    }
}
