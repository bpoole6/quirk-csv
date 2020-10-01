package com.poole.demo.inheritance;

import com.poole.csv.processor.CSVProcessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleMain {
	public static void main(String[] args) throws NoSuchMethodException, IOException {
		final String csv = "Marvin Nowell|34|20000.32|USA" + System.lineSeparator() + "Dillian Lamour|22|2499|France";
		CSVProcessor<PojoChild> processor = new CSVProcessor(PojoChild.class);
		try {
			//Reading
			CSVFormat format = CSVFormat.DEFAULT.withDelimiter('|');
			List<PojoChild> list = processor.parse(new StringReader(csv),format);
			list.forEach(System.out::println);

			System.out.println();

			//Writing
			StringWriter sw = new StringWriter();
			processor.write(list,sw);
			System.out.println(sw.toString());
		} catch (IOException e) {
		}


	}
}
