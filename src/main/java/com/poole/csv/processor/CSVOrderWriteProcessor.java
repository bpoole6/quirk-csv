package com.poole.csv.processor;

import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVType;
import com.poole.csv.annotation.CSVWriteBinding;
import com.poole.csv.annotation.CSVWriteComponent;
import com.poole.csv.exception.UninstantiableException;
import com.poole.csv.wrappers.write.WriteWrapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Does the processing for CSVReadComponent whose type uses NAMED
 */
@SuppressWarnings("rawtypes")
class CSVOrderWriteProcessor<T> extends AbstractCSVWriteProcessor<T> {

	private final static Logger LOGGER = Logger.getLogger(CSVOrderWriteProcessor.class.getName());

	public CSVOrderWriteProcessor(Class<T> parsedClazz,
                                  Map<Class, WriteWrapper> writeWrapperMap){
		super(parsedClazz,writeWrapperMap);
	}

	@Override
	protected void write(List<T> objects, StringWriter sw, CSVFormat format) throws IOException {
		CSVFormat csvFormat = format.withFirstRecordAsHeader();
		try (CSVPrinter printer = new CSVPrinter(sw, csvFormat);) {
			for(T obj : objects){
				List<String>values = new ArrayList<>();
				for(CSVWriteAnnotationManager cwm: this.csvAnnotationManagers){
					values.add(cwm.getWriteHolder().getValue(obj,this.setValueMap));
				}
				printer.printRecord(values);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.log(Level.SEVERE,
					"Could not extract value", e);
			throw new UninstantiableException(
					"Could not extract value", e);
		}
	}
}
