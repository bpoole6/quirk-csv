package com.poole.csv.processor;

import java.io.FileNotFoundException;
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

import org.apache.commons.csv.CSVFormat;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.exception.MethodParameterException;
import com.poole.csv.exception.WrapperInstantiationException;
import com.poole.csv.wrappers.Wrapper;
import com.poole.csv.wrappers.defaults.DefaultWrappers;

/**
 * Has most of the logic for processing CSVComponent annotated classes
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractCSVProcessor {
	Map<Class<Wrapper>, Wrapper> wrapperInstantiated;
	private final static Logger LOGGER = Logger.getLogger(AbstractCSVProcessor.class.getName());

	private List<String> errors;

	public final <T> List<T> parse(Reader reader, Class<T> parsedClazz, CSVFormat format,
			Map<Class, Wrapper> wrapperMap) throws FileNotFoundException, IOException {
		List<Class> classes = new ArrayList<>();
		Map<Class, Wrapper> setValueMap = getWrapperMethodMap(wrapperMap);
		getClasses(parsedClazz, classes);
		List<CSVAnnotationManager> list = getHolders(classes);
		return read(reader, list, parsedClazz, format, setValueMap);
	}

	private Map<Class, Wrapper> getWrapperMethodMap(Map<Class, Wrapper> wrapperMap) {
		Map<Class, Wrapper> map = new HashMap<>(DefaultWrappers.getDefault());
		if (wrapperMap != null) {
			map.putAll(wrapperMap);
		}
		return map;
	}

	protected abstract <T> List<T> read(Reader reader2, List<CSVAnnotationManager> list, Class parsedClazz2,
			CSVFormat format2, Map<Class, Wrapper> setValueMap) throws IOException;

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
			if (f.isAnnotationPresent(CSVColumn.class)) {
				int order = f.getAnnotation(CSVColumn.class).order();
				String header = f.getAnnotation(CSVColumn.class).header();
				boolean isNullable = f.getAnnotation(CSVColumn.class).isNullable();
				Wrapper wrapper = getWrapper(f.getAnnotation(CSVColumn.class).wrapper());
				CSVAnnotationManager manager = new CSVAnnotationManager(order, header, isNullable,
						new Holder(f, isNullable, wrapper), clazz, null);

				list.add(manager);
			}
		}
		return list;
	}

	private List<CSVAnnotationManager> getMethods(Class clazz) {
		List<CSVAnnotationManager> list = new ArrayList<>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.isAnnotationPresent(CSVColumn.class)) {
				validateParameter(m);
				int order = m.getAnnotation(CSVColumn.class).order();
				String header = m.getAnnotation(CSVColumn.class).header();
				boolean isNullable = m.getAnnotation(CSVColumn.class).isNullable();
				Wrapper wrapper = getWrapper(m.getAnnotation(CSVColumn.class).wrapper());
				CSVAnnotationManager manager = new CSVAnnotationManager(order, header, isNullable,
						new Holder(m, isNullable, wrapper), clazz, null);
				list.add(manager);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private Wrapper getWrapper(Class<? extends Wrapper> wrapper) {
		if (wrapper == null || wrapper == Wrapper.class)
			return null;
		Wrapper wrap = getWrapperInstantiated().get(wrapper);
		if (wrap == null) {
			try {
				wrap = wrapper.newInstance();
				getWrapperInstantiated().put((Class<Wrapper>) wrapper, wrap);

				return wrap;
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.log(Level.SEVERE, "Make sure your Wrapper class has a public no args constructor", e);
				throw new WrapperInstantiationException("Wrapper class does not have public no args constructor");
			}

		}
		return wrap;
	}

	protected <T> void getClasses(Class<T> clazz, List<Class> classes) {
		classes.add(clazz);
		CSVComponent csv = clazz.getAnnotation(CSVComponent.class);
		if (csv != null && csv.inheritSuper()) {
			getClasses(clazz.getSuperclass(), classes);
		}

	}

	protected void validateParameter(Method m) {
		if (m.getParameterCount() != 1)
			throw new MethodParameterException("Setter must only have one parameter:" + m.getName());
	}

	protected List<String> getErrors() {
		if (this.errors == null)
			this.errors = new ArrayList<>();
		return this.errors;
	}

	protected Map<Class<Wrapper>, Wrapper> getWrapperInstantiated() {
		if (this.wrapperInstantiated == null)
			this.wrapperInstantiated = new HashMap<>();
		return wrapperInstantiated;
	}

}
