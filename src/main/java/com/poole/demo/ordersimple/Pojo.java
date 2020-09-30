package com.poole.demo.ordersimple;

import com.poole.csv.annotation.CSVReadBinding;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVType;

@CSVReadComponent(type = CSVType.ORDER)
public class Pojo {
	private String name;

	@CSVReadBinding(order = 1)
	private Integer age;

	@CSVReadBinding(order = 2)
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
