package com.poole.csv.processor;

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

import com.poole.csv.wrappers.read.ReadWrapper;
import org.apache.commons.csv.CSVFormat;

import com.poole.csv.annotation.CSVReadColumn;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.exception.MethodParameterException;
import com.poole.csv.exception.WrapperInstantiationException;
import com.poole.csv.wrappers.read.defaults.DefaultWrappers;

/**
 * Has most of the logic for processing CSVReadComponent annotated classes
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractCSVProcessor<T> {
	protected final Map<Class<ReadWrapper>, ReadWrapper> wrapperInstantiated = new HashMap<>();
	protected final Map<Class, ReadWrapper> setValueMap = new HashMap<>();
	protected final List<CSVAnnotationManager> csvAnnotationManagers = new ArrayList<>();
	protected final Class<T> parsedClazz;
	private final static Logger LOGGER = Logger.getLogger(AbstractCSVProcessor.class.getName());

	public AbstractCSVProcessor(Class<T> parsedClazz,
								Map<Class, ReadWrapper> wrapperMap){
		this.parsedClazz = parsedClazz;
		List<Class> classes = new ArrayList<>();
		setValueMap.putAll(getWrapperMethodMap(wrapperMap));
		getClasses(parsedClazz, classes);
		csvAnnotationManagers.addAll(getHolders(classes));
	}

	private Map<Class, ReadWrapper> getWrapperMethodMap(Map<Class, ReadWrapper> wrapperMap) {
		Map<Class, ReadWrapper> map = new HashMap<>(DefaultWrappers.getDefault());
		if (wrapperMap != null) {
			map.putAll(wrapperMap);
		}
		return map;
	}

	protected abstract <T> List<T> read(Reader reader,
			CSVFormat format) throws IOException;

	private List<CSVAnnotationManager> getHolders(List<Class> classes) {
		List<CSVAnnotationManager> combined = new ArrayList<>();
		for (Class clazz : classes) {
			combined.addAll(getFields(clazz));
			combined.addAll(getMethods(clazz));
		}
		return combined;
	}

	private List<CSVAnnotationManager> getFields(Class clazz) {
		List<CSVAnnotationManager> list = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(CSVReadColumn.class)) {
				int order = f.getAnnotation(CSVReadColumn.class).order();
				String header = f.getAnnotation(CSVReadColumn.class).header();
				boolean isNullable = f.getAnnotation(CSVReadColumn.class).isNullable();
				ReadWrapper readWrapper = getWrapper(f.getAnnotation(CSVReadColumn.class).wrapper());
				CSVAnnotationManager manager = new CSVAnnotationManager(order, header,
						new Holder(f, isNullable, readWrapper), clazz);

				list.add(manager);
			}
		}
		return list;
	}

	private List<CSVAnnotationManager> getMethods(Class clazz) {
		List<CSVAnnotationManager> list = new ArrayList<>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.isAnnotationPresent(CSVReadColumn.class)) {
				validateParameter(m);
				int order = m.getAnnotation(CSVReadColumn.class).order();
				String header = m.getAnnotation(CSVReadColumn.class).header();
				boolean isNullable = m.getAnnotation(CSVReadColumn.class).isNullable();
				ReadWrapper readWrapper = getWrapper(m.getAnnotation(CSVReadColumn.class).wrapper());
				CSVAnnotationManager manager = new CSVAnnotationManager(order, header,
						new Holder(m, isNullable, readWrapper), clazz);
				list.add(manager);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private ReadWrapper getWrapper(Class<? extends ReadWrapper> wrapper) {
		if (wrapper == null || wrapper == ReadWrapper.class)
			return null;
		ReadWrapper wrap =this.wrapperInstantiated.get(wrapper);
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
		classes.add(clazz);
		CSVReadComponent csv = clazz.getAnnotation(CSVReadComponent.class);
		if (csv != null && csv.inheritSuper()) {
			getClasses(clazz.getSuperclass(), classes);
		}

	}

	protected void validateParameter(Method m) {
		if (m.getParameterCount() != 1)
			throw new MethodParameterException("Setter must only have one parameter:" + m.getName());
	}
}
