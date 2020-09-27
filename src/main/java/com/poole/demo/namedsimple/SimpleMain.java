package com.poole.demo.namedsimple;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.poole.csv.processor.CSVProcessor;

public class SimpleMain {
	public static void main(String[] args) {
		final String csv = "name,age,money" + System.lineSeparator() + "Marvin Nowell,34,20000.32"
				+ System.lineSeparator() + "Dillian Lamour,22,2499";
		CSVProcessor processor = new CSVProcessor(Pojo.class);
		List<Pojo> list = new ArrayList<>();
		try {
			list.addAll(processor.parse(new StringReader(csv)));
		} catch (IOException e) {
		}
		list.forEach(System.out::println);
	}
}
