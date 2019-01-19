package com.poole.demo.inheritance;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;

@CSVComponent(type = CSVReaderType.ORDER)
public class PojoParent {
	private String name;

	@CSVColumn(order = 1)
	private Integer age;

	@CSVColumn(order = 2)
	private Double money;

	@CSVColumn(order = 0)
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

}
