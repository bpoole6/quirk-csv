package com.poole.demo.inheritance;

import com.poole.csv.annotation.CSVReadBinding;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVType;
import com.poole.csv.annotation.CSVWriteBinding;

@CSVReadComponent(type = CSVType.ORDER)
public class PojoParent {
	@CSVWriteBinding(header = "name")
	private String name;

	@CSVReadBinding(order = 1)
	@CSVWriteBinding(header = "age")
	private Integer age;

	@CSVReadBinding(order = 2)
	@CSVWriteBinding(header = "money")
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
