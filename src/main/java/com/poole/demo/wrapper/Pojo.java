package com.poole.demo.wrapper;

import java.time.LocalDate;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;

@CSVComponent(type = CSVReaderType.ORDER)
public class Pojo {
	@CSVColumn(order = 0)
	private String name;

	@CSVColumn(order = 1, wrapper = LocalDateWrapper.class)
	private LocalDate dob;

	@CSVColumn(order = 2)
	private Person person;

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tDate Of Birth: " + dob + System.lineSeparator() + "\tId: "
				+ person.getId();
	}

}
