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
import com.poole.csv.wrappers.read.ReadWrapper;

/**
 * Does the processing for CSVReadComponent whose type uses ORDER
 */
@SuppressWarnings("rawtypes")
class CSVOrderReadProcessor<T> extends AbstractCSVReadProcessor {

	private final static Logger LOGGER = Logger.getLogger(CSVOrderReadProcessor.class.getName());
	public CSVOrderReadProcessor(Class<T> parsedClazz,
								 Map<Class, ReadWrapper> wrapperMap){
		super(parsedClazz,wrapperMap);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected List<T> read(Reader reader, CSVFormat format) throws IOException {
		List<T> items = new ArrayList<>();
		Map<Integer, ReadHolder> map = getMapFromManager(this.csvAnnotationManagers);

		try (CSVParser parser = new CSVParser(reader, format);) {
			for (CSVRecord record : parser) {
				Object obj = parsedClazz.newInstance();
				for (Entry<Integer, ReadHolder> entry : map.entrySet()) {
					ReadHolder hf = entry.getValue();
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

	private Map<Integer, ReadHolder> getMapFromManager(List<CSVAnnotationManager> list) {
		Map<Integer, ReadHolder> map = new HashMap<>();
		for (CSVAnnotationManager man : list) {
			map.put(man.getOrder(), man.getReadHolder());
		}
		return map;
	}
}
