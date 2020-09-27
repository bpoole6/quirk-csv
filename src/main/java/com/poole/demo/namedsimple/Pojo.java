package com.poole.demo.namedsimple;

import com.opencsv.bean.CsvBindByName;
import com.poole.csv.annotation.CSVReadColumn;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVReaderType;

@CSVReadComponent(type = CSVReaderType.NAMED)
public class Pojo {
	@CsvBindByName(column = "name")
	private String name;

	@CsvBindByName(column = "age")
	@CSVReadColumn(header = "age")
	private Integer age;

	@CsvBindByName(column = "money")
	@CSVReadColumn(header = "money")
	private Double money;

	@CSVReadColumn(header = "name")
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

}
