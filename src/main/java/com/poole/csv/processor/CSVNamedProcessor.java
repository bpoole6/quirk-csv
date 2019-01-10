package com.poole.csv.processor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVNamed;
import com.poole.csv.annotation.CSVNamed;

//TODO Add Tests
//TODO Allow for customer mapper for dataTypes
//TODO Make the custom mapper be able to be in the field annotation
//TODO Handle Null use cases
class CSVNamedProcessor {
	private final static Logger LOGGER = Logger.getLogger(CSVNamedProcessor.class.getName());

	public <T> List<T> parse(Reader reader, Class<T> clazz, CSVFormat format) throws FileNotFoundException, IOException {
			Map<String, Holder> map = getHolders(clazz);
			return read(reader, map, clazz, format);
	}

	private <T> List<T> read(Reader reader, Map<String, Holder> map, Class<T> clazz, CSVFormat format) throws IOException {
		List<T> list = new ArrayList<>();
		try(CSVParser parser = new CSVParser(reader, format);) {
			

			for (CSVRecord record : parser) {
				Object obj = clazz.newInstance();
				for (Entry<String, Holder> entry : map.entrySet()) {
					Holder hf = entry.getValue();
					String header = entry.getKey();
					
					try {
						hf.setValue(obj, record.get(header));
					} catch (IllegalArgumentException e) {
						LOGGER.log(Level.SEVERE, "Failed for header: "+header, e);
					} catch (InvocationTargetException e) {
						LOGGER.log(Level.SEVERE, "Failed for header: "+header, e);
					}

				}
				list.add((T) obj);

			}
		} catch (InstantiationException |IllegalAccessException e) {
			LOGGER.log(Level.SEVERE,"Could not create object. Check to make sure the you have a visible default constructor", e);
		} 
		return list;
	}

	private Map<String, Holder> getHolders(Class clazz) {
		Map<String, Holder> fieldMap = getFields(clazz);
		Map<String, Holder> methodMap = getMethods(clazz);
		Map<String, Holder> combined = combinedFieldMethod(fieldMap, methodMap);
		return combined;
	}

	private Map<String, Holder> getFields(Class clazz) {
		Map<String, Holder> map = new HashMap<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(CSVNamed.class)) {
				String header = f.getAnnotation(CSVNamed.class).header();
				boolean isNullable = f.getAnnotation(CSVNamed.class).isNullable();
				setMapEntry(map, header, new Holder(f, isNullable), clazz);
			}
		}
		return map;
	}

	private Map<String, Holder> getMethods(Class clazz) {
		Map<String, Holder> map = new HashMap<>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.isAnnotationPresent(CSVNamed.class)) {
				if (m.getParameterCount() != 1)
					throw new RuntimeException("Setter must only have one parameter:" + m.getName());
				String header = m.getAnnotation(CSVNamed.class).header();
				boolean isNullable = m.getAnnotation(CSVNamed.class).isNullable();
				setMapEntry(map, header, new Holder(m, isNullable), clazz);

			}
		}
		return map;
	}

	private void setMapEntry(Map<String, Holder> map, String header, Holder h, Class clazz) {
		if (map.get(header) != null)
			throw new RuntimeException("Non Unique Header in " + clazz + ".Header :" + header);
		map.put(header, h);
	}

	private Map<String, Holder> combinedFieldMethod(Map<String, Holder> fieldMap, Map<String, Holder> methodMap) {
		Set<String> repeats = new HashSet<>();
		Map<String, Holder> combined = new HashMap<>();
		fieldMap.forEach((k, v) -> {
			repeats.add(k);
			combined.put(k, v);
		});
		methodMap.forEach((k, v) -> {
			if (repeats.contains(k))
				throw new RuntimeException("Header#:" + k + " was both declared for both a field and method");
			combined.put(k, v);
		});

		return combined;
	}
}
