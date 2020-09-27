package com.poole.demo.wrapper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poole.csv.wrappers.read.ReadWrapper;
import org.apache.commons.csv.CSVFormat;

import com.poole.csv.processor.CSVProcessor;

public class SimpleMain {
	public static void main(String[] args) {
		final String csv = "Marvin Nowell,19820511,234" + System.lineSeparator() + "Dillian Lamour,19960111,21";
		// The key of the map should be of the class you want handled by the
		// wrapper
		// class and the value be the wrapper class that does the handling.
		// You can put
		// the primitives(and their wrappers) as well
		Map<Class, ReadWrapper> m = new HashMap<>();
		m.put(Person.class, new PersonReadWrapper());
		CSVProcessor processor = new CSVProcessor(Pojo.class, m);
		List<Pojo> list = new ArrayList<>();
		try {

			list.addAll(processor.parse(new StringReader(csv), CSVFormat.DEFAULT));
		} catch (IOException e) {
		}
		list.forEach(System.out::println);
	}
}
