package com.poole.csv.processor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.AbstractCSVWriter;
import com.poole.csv.annotation.CSVWriteComponent;
import com.poole.csv.wrappers.read.ReadWrapper;
import com.poole.csv.wrappers.write.WriteWrapper;
import org.apache.commons.csv.CSVFormat;

import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVType;
import com.poole.csv.exception.MissingCSVComponent;

/**
 * Acts as a further layer of abstraction to determine which processor to use
 */
@SuppressWarnings("rawtypes")
public class CSVProcessor<T> {
    private AbstractCSVReadProcessor readProcessor;
    private AbstractCSVWriteProcessor writeProcessor;

    public List<T> parse(Reader reader) throws IOException {
        missingReadComponent();
        return readProcessor.read(reader, CSVFormat.DEFAULT);
    }
    public List<T> parse(Reader reader,  CSVFormat format) throws IOException {
        missingReadComponent();
        return readProcessor.read(reader, format);
    }

    public void write(List<T>objects, StringWriter sw, CSVFormat csvFormat) throws IOException {
        missingWriteComponent();
        writeProcessor.write(objects, sw,csvFormat);
    }
    public void write(List<T>objects, StringWriter sw) throws IOException {
        missingWriteComponent();
        writeProcessor.write(objects, sw,CSVFormat.DEFAULT);
    }

    public CSVProcessor(Class<T> clazz) {
        this(clazz,new HashMap<>(),new HashMap<>());
    }

    public CSVProcessor(Class<T> clazz, Map<Class, ReadWrapper> readWrapperMap, Map<Class, WriteWrapper> writeWrappersMap) {
        CSVReadComponent readComponent = clazz.getAnnotation(CSVReadComponent.class);
        CSVWriteComponent writeComponent = clazz.getAnnotation(CSVWriteComponent.class);
        if (readComponent != null) {
            if (readComponent.type() == CSVType.ORDER) {
                readProcessor = new CSVOrderReadProcessor<>(clazz, readWrapperMap);
            } else {
                readProcessor = new CSVNamedReadProcessor<>(clazz, readWrapperMap);
            }

        }
        if(writeComponent!=null){
            if (writeComponent.type() == CSVType.ORDER) {
                writeProcessor = new CSVOrderWriteProcessor<>(clazz, writeWrappersMap);
            } else {
                writeProcessor = new CSVNamedWriteProcessor<>(clazz, writeWrappersMap);
            }
        }
    }
    private void missingReadComponent(){
        if(readProcessor ==null) {
            throw new MissingCSVComponent("Missing " + CSVReadComponent.class + " annotation");
        }
    }
    private void missingWriteComponent(){
        if(writeProcessor ==null) {
            throw new MissingCSVComponent("Missing " + CSVWriteComponent.class + " annotation");
        }
    }
}
