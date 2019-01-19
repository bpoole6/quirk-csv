package com.poole.demo.inheritance;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;

// Make sure inheritSuper is TRUE
@CSVComponent(type = CSVReaderType.ORDER, inheritSuper = true)
public class PojoChild extends PojoParent {
	@CSVColumn(order = 3)
	private String nationality;

	@Override
	public String toString() {

		return super.toString() + System.lineSeparator() + "\tNationality: " + nationality;
	}

}
