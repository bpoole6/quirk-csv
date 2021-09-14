package com.quirk.csv.processor;

import com.quirk.csv.annotation.CSVReadComponent;
import com.quirk.csv.annotation.CSVType;
import com.quirk.csv.annotation.CSVWriteComponent;
import com.quirk.csv.exception.MissingCSVComponent;
import com.quirk.csv.wrappers.read.ReadWrapper;
import com.quirk.csv.wrappers.write.WriteWrapper;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Acts as a further layer of abstraction to determine which processors to use
 */
@SuppressWarnings("rawtypes")
public class CSVProcessor<T> {
    private AbstractCSVReadProcessor<T> readProcessor;
    private AbstractCSVWriteProcessor<T> writeProcessor;

    public List<T> parse(Reader reader) throws IOException {
        missingReadComponent();
        return readProcessor.read(reader, CSVFormat.DEFAULT);
    }
    public List<T> parse(Reader reader,  CSVFormat format) throws IOException {
        missingReadComponent();
        return readProcessor.read(reader, format);
    }

    public void process(Reader reader, Consumer<T> consumer) throws IOException {
        missingReadComponent();
        readProcessor.process(reader, CSVFormat.DEFAULT, consumer);
    }
    public void process(Reader reader,  CSVFormat format, Consumer<T> consumer) throws IOException {
        missingReadComponent();
        readProcessor.process(reader, format, consumer);
    }

    public void write(List<T>objects, Appendable appendable, CSVFormat csvFormat) throws IOException {
        missingWriteComponent();
        writeProcessor.write(objects, appendable,csvFormat);
    }
    public void write(List<T>objects, Appendable appendable) throws IOException {
        missingWriteComponent();
        writeProcessor.write(objects, appendable,CSVFormat.DEFAULT);
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
