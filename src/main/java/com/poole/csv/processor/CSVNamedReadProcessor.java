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
import com.poole.csv.wrappers.read.ReadWrapper;

/**
 * Does the processing for CSVReadComponent whose type uses NAMED
 */
@SuppressWarnings("rawtypes")
class CSVNamedReadProcessor<T> extends AbstractCSVReadProcessor<T> {

	private final static Logger LOGGER = Logger.getLogger(CSVNamedReadProcessor.class.getName());

	public CSVNamedReadProcessor(Class<T> parsedClazz,
								 Map<Class, ReadWrapper> readWrapperMap){
		super(parsedClazz,readWrapperMap);
	}

	protected List<T> read(Reader reader, CSVFormat format) throws IOException {
		List<T> items = new ArrayList<>();
		format = format.withFirstRecordAsHeader();
		Map<String, ReadHolder> map = getMapFromManager(this.csvAnnotationManagers);
		try (CSVParser parser = new CSVParser(reader, format);) {

			for (CSVRecord record : parser) {

				Object obj = parsedClazz.newInstance();
				for (Entry<String, ReadHolder> entry : map.entrySet()) {
					ReadHolder hf = entry.getValue();
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


	private Map<String, ReadHolder> getMapFromManager(List<CSVAnnotationManager> list) {
		Map<String, ReadHolder> map = new HashMap<>();
		for (CSVAnnotationManager man : list) {
			map.put(man.getHeader(), man.getReadHolder());
		}
		return map;
	}

}
