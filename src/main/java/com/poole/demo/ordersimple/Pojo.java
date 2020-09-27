package com.poole.demo.ordersimple;

import com.poole.csv.annotation.CSVReadColumn;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVReaderType;

@CSVReadComponent(type = CSVReaderType.ORDER)
public class Pojo {
	private String name;

	@CSVReadColumn(order = 1)
	private Integer age;

	@CSVReadColumn(order = 2)
	private Double money;

	@CSVReadColumn(order = 0)
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

}
