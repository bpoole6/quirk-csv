package com.poole.demo.inheritance;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.poole.csv.processor.CSVProcessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class SimpleMain {
	public static void main(String[] args) throws NoSuchMethodException, IOException {
		final String csv = "Marvin Nowell,34,20000.32,USA" + System.lineSeparator() + "Dillian Lamour,22,2499,France";
		CSVProcessor processor = new CSVProcessor(PojoChild.class);
		List<PojoParent> list = new ArrayList<>();
		try {
			list.addAll(processor.parse(new StringReader(csv)));// In
																					// CSVReadComponent
		} catch (IOException e) {
		}
		list.forEach(System.out::println);
		Method method = CSVPrinter.class.getMethod("printRecord");
		StringWriter sw = new StringWriter();
		CSVPrinter p = new CSVPrinter(sw, CSVFormat.DEFAULT);

	}
}
