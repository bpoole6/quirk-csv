package com.quirk.demo.namedsimple;

import com.quirk.csv.processor.CSVProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class SimpleMain {
	public static void main(String[] args) {
		final String csv = "name,age,money" + System.lineSeparator() + "Marvin Nowell,34,20000.32"
				+ System.lineSeparator() + "Dillian Lamour,22,2499";
		CSVProcessor<Pojo> processor = new CSVProcessor<>(Pojo.class);
		try {
			// Reading
			List<Pojo> list = processor.parse(new StringReader(csv));
			list.forEach(System.out::println);

			System.out.println();

			// Writing
			StringWriter sw = new StringWriter();
			processor.write(list,sw);
			System.out.println(sw.toString());
		} catch (IOException e) {
		}

	}
}
