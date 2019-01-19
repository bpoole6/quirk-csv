package com.poole.demo.inheritance;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.poole.csv.processor.CSVProcessor;

public class SimpleMain {
	public static void main(String[] args) {
		final String csv = "Marvin Nowell,34,20000.32,USA" + System.lineSeparator() + "Dillian Lamour,22,2499,France";
		CSVProcessor processor = new CSVProcessor();
		List<PojoParent> list = new ArrayList<>();
		try {
			list.addAll(processor.parse(new StringReader(csv), PojoChild.class));// In
																					// CSVComponent
		} catch (IOException e) {
		}
		list.forEach(System.out::println);
	}
}
