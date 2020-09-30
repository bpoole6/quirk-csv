package com.poole.demo.wrapper;

import java.time.LocalDate;

import com.poole.csv.annotation.CSVReadBinding;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVType;

@CSVReadComponent(type = CSVType.ORDER)
public class Pojo {
	@CSVReadBinding(order = 0)
	private String name;

	@CSVReadBinding(order = 1, wrapper = LocalDateReadWrapper.class)
	private LocalDate dob;

	@CSVReadBinding(order = 2)
	private Person person;

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tDate Of Birth: " + dob + System.lineSeparator() + "\tId: "
				+ person.getId();
	}

}
