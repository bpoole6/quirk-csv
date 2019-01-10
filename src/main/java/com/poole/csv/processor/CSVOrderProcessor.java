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
import com.poole.csv.annotation.CSVNumberOrder;
//TODO Make sure that if they do fields then they have the proper setter that goes along with it

//TODO Add Tests
//TODO Allow for customer mapper for dataTypes
//TODO Make the custom mapper be able to be in the field annotation
//TODO Handle Null use cases
class CSVOrderProcessor {
	private final static Logger LOGGER = Logger.getLogger(CSVOrderProcessor.class.getName());

	public <T> List<T> parse(Reader reader, Class<T> clazz, CSVFormat format) throws FileNotFoundException, IOException {
			Map<Integer, Holder> map = getHolders(clazz);
			return read(reader, map, clazz, format);
	}

	private <T> List<T> read(Reader reader, Map<Integer, Holder> map, Class<T> clazz, CSVFormat format) throws  IOException {
		List<T> list = new ArrayList<>();
		try(CSVParser parser = new CSVParser(reader, format);) {
			

			for (CSVRecord record : parser) {
				Object obj = clazz.newInstance();
				for (Entry<Integer, Holder> entry : map.entrySet()) {
					Holder hf = entry.getValue();
					int order = entry.getKey();
					
					try {
						hf.setValue(obj, record.get(order));
					} catch (IllegalArgumentException e) {
						LOGGER.log(Level.SEVERE, "Failed for order#: "+order, e);
					} catch (InvocationTargetException e) {
						LOGGER.log(Level.SEVERE, "Failed for order#: "+order, e);
					}catch(ArrayIndexOutOfBoundsException e){
						LOGGER.log(Level.WARNING, "Order#: "+order+" exceeds the number of values for the row", e);
					}catch(IllegalAccessException e){
						LOGGER.log(Level.WARNING, "Order#: "+order+"Make sure that if you are you have a ", e);

					}

				}
				list.add((T) obj);

			}
		} catch (InstantiationException |IllegalAccessException e) {
			LOGGER.log(Level.SEVERE,"Could not create object. Check to make sure the you have a visible default constructor", e);
		} 
		return list;
	}

	private Map<Integer, Holder> getHolders(Class clazz) {
		Map<Integer, Holder> fieldMap = getFields(clazz);
		Map<Integer, Holder> methodMap = getMethods(clazz);
		Map<Integer, Holder> combined = combinedFieldMethod(fieldMap, methodMap);
		return combined;
	}

	private Map<Integer, Holder> getFields(Class clazz) {
		Map<Integer, Holder> map = new HashMap<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(CSVNumberOrder.class)) {
				int order = f.getAnnotation(CSVNumberOrder.class).order();
				boolean isNullable = f.getAnnotation(CSVNumberOrder.class).isNullable();
				setMapEntry(map, order, new Holder(f, isNullable), clazz);
			}
		}
		return map;
	}

	private Map<Integer, Holder> getMethods(Class clazz) {
		Map<Integer, Holder> map = new HashMap<>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.isAnnotationPresent(CSVNumberOrder.class)) {
				if (m.getParameterCount() != 1)
					throw new RuntimeException("Setter must only have one parameter:" + m.getName());
				int order = m.getAnnotation(CSVNumberOrder.class).order();
				boolean isNullable = m.getAnnotation(CSVNumberOrder.class).isNullable();
				setMapEntry(map, order, new Holder(m, isNullable), clazz);

			}
		}
		return map;
	}

	private void setMapEntry(Map<Integer, Holder> map, int order, Holder h, Class clazz) {
		if (map.get(order) != null)
			throw new RuntimeException("Non Unique order number in " + clazz + ". Order#:" + order);
		map.put(order, h);
	}

	private Map<Integer, Holder> combinedFieldMethod(Map<Integer, Holder> fieldMap, Map<Integer, Holder> methodMap) {
		Set<Integer> repeats = new HashSet<>();
		Map<Integer, Holder> combined = new HashMap<>();
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
}
