package com.poole.csv.processor;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.poole.csv.exception.OrderParserException;
import com.poole.csv.exception.UninstantiableException;
import com.poole.csv.wrappers.Wrapper;

/**
 * Does the processing for CSVComponent whose type uses ORDER
 */
@SuppressWarnings("rawtypes")
class CSVOrderProcessor<T> extends AbstractCSVProcessor {

	private final static Logger LOGGER = Logger.getLogger(CSVOrderProcessor.class.getName());
	public CSVOrderProcessor(Class<T> parsedClazz,
							 Map<Class, Wrapper> wrapperMap){
		super(parsedClazz,wrapperMap);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected List<T> read(Reader reader, CSVFormat format) throws IOException {
		List<T> items = new ArrayList<>();
		Map<Integer, Holder> map = getMapFromManager(this.csvAnnotationManagers);

		try (CSVParser parser = new CSVParser(reader, format);) {
			for (CSVRecord record : parser) {
				Object obj = parsedClazz.newInstance();
				for (Entry<Integer, Holder> entry : map.entrySet()) {
					Holder hf = entry.getValue();
					int order = entry.getKey();

					try {
						hf.setValue(obj, record.get(order), setValueMap);
					} catch (IllegalArgumentException e) {
						LOGGER.log(Level.SEVERE, "Failed for order#: " + order, e);
					} catch (InvocationTargetException e) {
						LOGGER.log(Level.SEVERE, "Failed for order#: " + order, e);
					} catch (ArrayIndexOutOfBoundsException e) {
						LOGGER.log(Level.WARNING, "Order#: " + order + " exceeds the number of values for the row", e);
					} 

				}
				items.add((T) obj);

			}
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE,
					"Could not create object. Check to make sure the you have a visible default constructor", e);
			throw new UninstantiableException(
					"Could not create object. Check to make sure the you have a visible default constructor", e);
		}
		return items;
	}

	private Map<Integer, Holder> getMapFromManager(List<CSVAnnotationManager> list) {
		Map<Integer, Holder> map = new HashMap<>();
		List<String> errors = new ArrayList<>();
		for (CSVAnnotationManager man : list) {
			Holder holder = map.get(man.getOrder());
			if (holder != null)
				errors.add("Non Unique Order# in " + man.getParsedClazz() + ".Order# :" + man.getOrder());
			else {
				map.put(man.getOrder(), man.getHolder());
			}
		}
		if (errors.size() > 0) {
			throw new OrderParserException(errors.toString());
		}
		return map;
	}
}
