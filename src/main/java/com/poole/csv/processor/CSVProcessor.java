package com.poole.csv.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;

import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.exception.MissingCSVComponent;
import com.poole.csv.wrappers.Wrapper;

/**
 * Acts as a further layer of abstraction to determine which processor to use
 */
@SuppressWarnings("rawtypes")
public class CSVProcessor<T> {

	public CSVProcessor(Class<T> clazz,Map<Class, Wrapper> wrapperMap){

	}
	public <T> List<T> parse(Reader reader, Class<T> clazz) throws IOException {
		return parse(reader, clazz, CSVFormat.DEFAULT, new HashMap<>());
	}

	public <T> List<T> parse(Reader reader, Class<T> clazz, CSVFormat format, Map<Class, Wrapper> wrapperMap)
			throws FileNotFoundException, IOException {
		try {
			CSVComponent component = clazz.getAnnotation(CSVComponent.class);

			if (component != null) {
				if (clazz.getAnnotation(CSVComponent.class).type() == CSVReaderType.ORDER) {
					return new CSVOrderProcessor().parse(reader, clazz, format, wrapperMap);
				} else {
					format = format.withFirstRecordAsHeader();
					return new CSVNamedProcessor().parse(reader, clazz, format, wrapperMap);
				}

			} else {
				throw new MissingCSVComponent("Missing " + CSVComponent.class + " annotation");
			}
		} finally {
			try {
				if (reader != null && reader.ready())
					reader.close();
			} catch (Exception e) {

			}
		} 
	}
}
