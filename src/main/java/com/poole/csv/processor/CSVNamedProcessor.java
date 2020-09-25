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

import com.poole.csv.exception.NamedParserException;
import com.poole.csv.exception.UninstantiableException;
import com.poole.csv.wrappers.Wrapper;

/**
 * Does the processing for CSVComponent whose type uses NAMED
 */
@SuppressWarnings("rawtypes")
class CSVNamedProcessor extends AbstractCSVProcessor {

	private final static Logger LOGGER = Logger.getLogger(CSVNamedProcessor.class.getName());

	@Override
	protected List read(Reader reader2, List list, Class parsedClazz2, CSVFormat format2, Map setValueMap) throws IOException {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> List<T> read(Reader reader, List<CSVAnnotationManager> list, Class parsedClazz, CSVFormat format,
			Map<Class, Wrapper> setValueMap) throws IOException {
		List<T> items = new ArrayList<>();
		Map<String, Holder> map = getMapFromManager(list);
		if (getErrors().size() > 0) {
			throw new NamedParserException(getErrors().toString());
		}
		try (CSVParser parser = new CSVParser(reader, format);) {

			for (CSVRecord record : parser) {

				Object obj = parsedClazz.newInstance();
				for (Entry<String, Holder> entry : map.entrySet()) {
					Holder hf = entry.getValue();
					String header = entry.getKey();
					try {
						hf.setValue(obj, record.get(header), setValueMap);
					} catch (IllegalArgumentException | InvocationTargetException e) {
						LOGGER.log(Level.SEVERE, "Failed for header: " + header, e);
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

	private Map<String, Holder> getMapFromManager(List<CSVAnnotationManager> list) {
		Map<String, Holder> map = new HashMap<>();
		for (CSVAnnotationManager man : list) {
			Holder holder = map.get(man.getHeader());
			if (holder != null)
				getErrors().add("Non Unique Header in " + man.getParsedClazz() + ".Header :" + man.getParsedClazz());
			else {
				map.put(man.getHeader(), man.getHolder());
			}
		}
		return map;
	}


}
