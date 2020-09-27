package com.poole.csv.processor;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poole.csv.wrappers.read.ReadWrapper;
import org.apache.commons.csv.CSVFormat;

import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.exception.MissingCSVComponent;

/**
 * Acts as a further layer of abstraction to determine which processor to use
 */
@SuppressWarnings("rawtypes")
public class CSVProcessor<T> {
    private final AbstractCSVProcessor processor;

    public List<T> parse(Reader reader) throws IOException {
        return processor.read(reader, CSVFormat.DEFAULT);
    }
    public List<T> parse(Reader reader,  CSVFormat format) throws IOException {
        return processor.read(reader, format);
    }

    public CSVProcessor(Class<T> clazz) {
        this(clazz,new HashMap<>());
    }

    public CSVProcessor(Class<T> clazz, Map<Class, ReadWrapper> wrapperMap) {
        CSVReadComponent component = clazz.getAnnotation(CSVReadComponent.class);
        if (component != null) {
            if (clazz.getAnnotation(CSVReadComponent.class).type() == CSVReaderType.ORDER) {
                processor = new CSVOrderProcessor(clazz, wrapperMap);
            } else {
                processor = new CSVNamedProcessor(clazz, wrapperMap);
            }

        } else {
            throw new MissingCSVComponent("Missing " + CSVReadComponent.class + " annotation");
        }

    }
}
