package com.quirk.demo.threadsafe;

import com.quirk.csv.annotation.*;

@CSVReadComponent(type = CSVType.NAMED)
@CSVWriteComponent(type = CSVType.NAMED,namedIsOrdered = true)
public class Pojo {
	@CSVWriteBinding(header = "name",order = 0)
	private String name;

	@CSVWriteBinding(header = "age",order = 1)
	@CSVReadBinding(header = "age")
	private Integer age;

	@CSVWriteBinding(header = "money",order = 2)
	@CSVReadBinding(header = "money")
	private Double money;

	@CSVReadBinding(header = "name")
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

}
