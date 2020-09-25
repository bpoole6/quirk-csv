package com.poole.demo.namedsimple;

import com.opencsv.bean.CsvBindByName;
import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;

@CSVComponent(type = CSVReaderType.NAMED)
public class Pojo {
	@CsvBindByName(column = "name")
	private String name;

	@CsvBindByName(column = "age")
	@CSVColumn(header = "age")
	private Integer age;

	@CsvBindByName(column = "money")
	@CSVColumn(header = "money")
	private Double money;

	@CSVColumn(header = "name")
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

}
