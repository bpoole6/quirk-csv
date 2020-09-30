package com.poole.csv.processor;

import com.poole.csv.exception.UninstantiableException;
import com.poole.csv.wrappers.write.WriteWrapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Does the processing for CSVNamedWriteProcessor whose type uses NAMED
 */
@SuppressWarnings("rawtypes")
class CSVNamedWriteProcessor<T> extends AbstractCSVWriteProcessor<T> {

	private final static Logger LOGGER = Logger.getLogger(CSVNamedWriteProcessor.class.getName());

	public CSVNamedWriteProcessor(Class<T> parsedClazz,
                                  Map<Class, WriteWrapper> writeWrapperMap){
		super(parsedClazz,writeWrapperMap);
	}

	@Override
	protected void write(List<T> objects, StringWriter sw, CSVFormat format) throws IOException {

		CSVFormat csvFormat = setHeaders(format);
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

	private CSVFormat setHeaders(CSVFormat csvFormat) {
		String[] headers = new String[this.csvAnnotationManagers.size()];
		for(int i = 0;i<this.csvAnnotationManagers.size();i++){
			headers[i] = this.csvAnnotationManagers.get(i).getHeader();
		}
		return csvFormat.withHeader(headers);
	}

}
