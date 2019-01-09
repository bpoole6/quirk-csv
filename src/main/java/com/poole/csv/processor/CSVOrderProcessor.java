package com.poole.csv.processor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVNumberOrder;
//TODO Add Tests
//TODO Allow for customer mapper for dataTypes
//TODO Make the custom mapper be able to be in the field annotation
//TODO Handle Null use cases
 class CSVOrderProcessor {
	public <T> List<T> parse(String str, Class<T> clazz) throws FileNotFoundException, IOException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return parse(str, clazz,CSVFormat.DEFAULT);
	}

	public <T> List<T> parse(String str, Class<T> clazz, CSVFormat format) throws FileNotFoundException, IOException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Annotation[] ann = clazz.getAnnotations();
		if (isCSVType(ann)) {
			Map<Integer, HolderField> map = getHolders(clazz);
			return read(str, map, clazz,format);
		} else {
			throw new RuntimeException();
		}
	}

	private <T> List<T> read(String str, Map<Integer, HolderField> map, Class<T> clazz, CSVFormat format)
			throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		CSVParser parser = new CSVParser(new FileReader(str), format);
		List<T> list = new ArrayList<>();
		for (CSVRecord record : parser) {
			Object obj = clazz.newInstance();
			for (Entry<Integer, HolderField> entry : map.entrySet()) {
				HolderField hf = entry.getValue();
				int order = entry.getKey();

				hf.setValue(obj, record.get(order));

			}
			list.add((T) obj);

		}
		parser.close();
		return list;
	}

	private Map<Integer, HolderField> getHolders(Class clazz) {
		Map<Integer, HolderField> fieldMap = getFields(clazz);
		Map<Integer, HolderField> methodMap = getMethods(clazz);
		Map<Integer, HolderField> combined = combinedFieldMethod(fieldMap, methodMap);
		return combined;
	}

	private Map<Integer, HolderField> getFields(Class clazz) {
		Map<Integer, HolderField> map = new HashMap<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(CSVNumberOrder.class)) {
				int order = f.getAnnotation(CSVNumberOrder.class).order();
				setMapEntry(map, order, new HolderField(f), clazz);
			}
		}
		return map;
	}

	private Map<Integer, HolderField> getMethods(Class clazz) {
		Map<Integer, HolderField> map = new HashMap<>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.isAnnotationPresent(CSVNumberOrder.class)) {
				if (m.getParameterCount() != 1)
					throw new RuntimeException("Setter must only have one parameter:" + m.getName());
				int order = m.getAnnotation(CSVNumberOrder.class).order();
				setMapEntry(map, order, new HolderField(m), clazz);

			}
		}
		return map;
	}

	private void setMapEntry(Map<Integer, HolderField> map, int order, HolderField h, Class clazz) {
		if (map.get(order) != null)
			throw new RuntimeException("Non Unique order number in " + clazz + ". Order#:" + order);
		map.put(order, h);
	}

	private Map<Integer, HolderField> combinedFieldMethod(Map<Integer, HolderField> fieldMap,
			Map<Integer, HolderField> methodMap) {
		Set<Integer> repeats = new HashSet<>();
		Map<Integer, HolderField> combined = new HashMap<>();
		fieldMap.forEach((k, v) -> {
			repeats.add(k);
			combined.put(k, v);
		});
		methodMap.forEach((k, v) -> {
			if (repeats.contains(k))
				throw new RuntimeException("Order#:" + k + " was both declared for both a field and method");
			combined.put(k, v);
		});

		return combined;
	}

	private boolean isCSVType(Annotation[] ann) {
		for (Annotation a : ann) {
			if (a.annotationType() == CSVComponent.class) {
				return true;
			}
		}
		return false;
	}
}
