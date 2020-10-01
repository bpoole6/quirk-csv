package com.poole.demo.bigprocessing;

import com.poole.csv.annotation.*;

@CSVReadComponent(type = CSVType.NAMED)
@CSVWriteComponent(type = CSVType.ORDER)
public class Pojo {
	@CSVWriteBinding(order = 0)
	private String name;

	@CSVWriteBinding(order = 1)
	@CSVReadBinding(header = "age")
	private Integer age;

	@CSVWriteBinding(order = 2)
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
