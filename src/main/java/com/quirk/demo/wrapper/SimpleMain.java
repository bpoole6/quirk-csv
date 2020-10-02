package com.quirk.demo.wrapper;

import com.quirk.csv.processor.CSVProcessor;
import com.quirk.csv.wrappers.read.ReadWrapper;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		CSVProcessor<Pojo> processor = new CSVProcessor<>(Pojo.class, m,new HashMap<>());
		try {

			List<Pojo> list = processor.parse(new StringReader(csv), CSVFormat.DEFAULT);
			list.forEach(System.out::println);

			System.out.println();

			StringWriter sw = new StringWriter();
			processor.write(list,sw);

		} catch (IOException e) {
		}

	}
}
