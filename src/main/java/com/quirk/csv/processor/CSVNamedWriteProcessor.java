package com.quirk.csv.processor;

import com.quirk.csv.wrappers.write.WriteWrapper;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Does the processing for {@link CSVNamedWriteProcessor} whose type uses NAMED
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
		doWrite(objects,sw,csvFormat,LOGGER);
	}

	private CSVFormat setHeaders(CSVFormat csvFormat) {
		String[] headers = new String[this.csvAnnotationManagers.size()];
		for(int i = 0;i<this.csvAnnotationManagers.size();i++){
			headers[i] = this.csvAnnotationManagers.get(i).getHeader();
		}
		return csvFormat.withHeader(headers);
	}

}
