package com.poole.demo.namedsimple;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.poole.csv.processor.CSVProcessor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SimpleMain2 {
	public static void main(String[] args) {
		StringJoiner sj = new StringJoiner(System.lineSeparator());
		for (int i= 0; i<1_000_000;i++){
			sj.add("Marvin Nowell,34,20000.32");
		}
		String csv = "name,age,money" + System.lineSeparator() + sj.toString();

		CSVProcessor processor = new CSVProcessor(Pojo.class);
		List<Pojo> list = new ArrayList<>();
		try {
			list.addAll(processor.parse(new StringReader(csv)));
			StringWriter sw = new StringWriter();
			processor.write(list,sw);
			//System.out.println(sw.toString());
		} catch (IOException e) {
		}
		//list.forEach(System.out::println);

	}
}
