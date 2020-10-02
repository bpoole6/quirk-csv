package com.quirk.demo.ordersimple;

import com.quirk.csv.annotation.*;

@CSVReadComponent(type = CSVType.ORDER)
@CSVWriteComponent(type = CSVType.ORDER)
public class Pojo {
	@CSVWriteBinding(order = 3)
	private String name;

	@CSVReadBinding(order = 1)
	@CSVWriteBinding(order = 2)
	private Integer age;

	@CSVReadBinding(order = 2)
	@CSVWriteBinding(order = 1)
	private Double money;

	@CSVReadBinding(order = 0)
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

}
