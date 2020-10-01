package com.poole.demo.ordersimple;

import com.poole.csv.processor.CSVProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class SimpleMain {
	public static void main(String[] args) {
		final String csv = "Marvin Nowell,34,20000.32" + System.lineSeparator() + "Dillian Lamour,22,2499";
		CSVProcessor<Pojo> processor = new CSVProcessor<>(Pojo.class);

		try {
			//Reading
			List<Pojo> list = processor.parse(new StringReader(csv));
			list.forEach(System.out::println);

			System.out.println();

			//Writing
			StringWriter sw = new StringWriter();
			processor.write(list,sw);

		} catch (IOException e) {
		}

	}
}
