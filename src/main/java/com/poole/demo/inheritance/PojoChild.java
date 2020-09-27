package com.poole.demo.inheritance;

import com.poole.csv.annotation.CSVReadColumn;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVReaderType;

// Make sure inheritSuper is TRUE
@CSVReadComponent(type = CSVReaderType.ORDER, inheritSuper = true)
public class PojoChild extends PojoParent {
	@CSVReadColumn(order = 3)
	private String nationality;

	@Override
	public String toString() {

		return super.toString() + System.lineSeparator() + "\tNationality: " + nationality;
	}

}
