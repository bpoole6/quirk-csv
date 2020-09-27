package com.poole.demo.wrapper;

import java.time.LocalDate;

import com.poole.csv.annotation.CSVReadColumn;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVReaderType;

@CSVReadComponent(type = CSVReaderType.ORDER)
public class Pojo {
	@CSVReadColumn(order = 0)
	private String name;

	@CSVReadColumn(order = 1, wrapper = LocalDateReadWrapper.class)
	private LocalDate dob;

	@CSVReadColumn(order = 2)
	private Person person;

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tDate Of Birth: " + dob + System.lineSeparator() + "\tId: "
				+ person.getId();
	}

}
