package com.poole.demo.namedsimple;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;

@CSVComponent(type = CSVReaderType.NAMED)
public class Pojo {
	private String name;

	@CSVColumn(header = "age")
	private Integer age;

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
