package com.poole.csv.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;

import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;

//TODO Fix Exeception handling
public class CSVProcessor {
	public <T> List<T> parse(String str, Class<T> clazz) throws FileNotFoundException, IOException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return parse(str, clazz, CSVFormat.DEFAULT);
	}

	public <T> List<T> parse(String str, Class<T> clazz, CSVFormat format) throws FileNotFoundException, IOException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Annotation[] ann = clazz.getAnnotations();
		Annotation a = isCSVType(ann);
		if (a != null) {
			if (clazz.getAnnotation(CSVComponent.class).type() == CSVReaderType.ORDER) {
				return new CSVOrderProcessor().parse(str, clazz, format);
			} else {
				return null;// TODO add by column header
			}

		} else {
			throw new RuntimeException();
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
