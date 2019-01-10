package com.poole.csv.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.util.List;

import org.apache.commons.csv.CSVFormat;

import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;

//TODO Fix Exeception handling
public class CSVProcessor {
	public <T> List<T> parse(Reader reader, Class<T> clazz) throws FileNotFoundException, IOException {
		return parse(reader, clazz, CSVFormat.DEFAULT);
	}

	public <T> List<T> parse(Reader reader, Class<T> clazz, CSVFormat format)
			throws FileNotFoundException, IOException {
		try {
			Annotation[] ann = clazz.getAnnotations();
			Annotation a = isCSVType(ann);

			if (a != null) {
				if (clazz.getAnnotation(CSVComponent.class).type() == CSVReaderType.ORDER) {
					return new CSVOrderProcessor().parse(reader, clazz, format);
				} else {
					format = format.withFirstRecordAsHeader();
					return new CSVNamedProcessor().parse(reader, clazz, format);// TODO
																				// add
																				// by
																				// column
																				// header
				}

			} else {
				throw new RuntimeException();
			}
		} finally {
			try {
				if (reader != null && reader.ready())
					reader.close();
			} catch (Exception e) {

			}
		}
	}

	private Annotation isCSVType(Annotation[] ann) {
		for (Annotation a : ann) {
			if (a.annotationType() == CSVComponent.class) {
				return a;
			}
		}
		return null;
	}
}
