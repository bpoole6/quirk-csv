package com.poole.demo.wrapper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;

import com.poole.csv.processor.CSVProcessor;
import com.poole.csv.wrappers.Wrapper;

public class SimpleMain {
	public static void main(String[] args) {
		final String csv = "Marvin Nowell,19820511,234" + System.lineSeparator() + "Dillian Lamour,19960111,21";
		CSVProcessor processor = new CSVProcessor();
		List<Pojo> list = new ArrayList<>();
		try {
			// The key of the map should be of the class you want handled by the
			// wrapper
			// class and the value be the wrapper class that does the handling.
			// You can put
			// the primitives(and their wrappers) as well
			Map<Class, Wrapper> m = new HashMap<>();
			m.put(Person.class, new PersonWrapper());
			list.addAll(processor.parse(new StringReader(csv), Pojo.class, CSVFormat.DEFAULT, m));
		} catch (IOException e) {
		}
		list.forEach(System.out::println);
	}
}
