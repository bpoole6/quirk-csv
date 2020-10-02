package com.quirk.demo.inheritance;

import com.quirk.csv.annotation.*;

// Make sure inheritSuper is TRUE
@CSVReadComponent(type = CSVType.ORDER, inheritSuper = true)
@CSVWriteComponent(type = CSVType.NAMED, inheritSuper = true)
public class PojoChild extends PojoParent {
	@CSVReadBinding(order = 3)
	@CSVWriteBinding(header = "nationality")
	private String nationality;

	@Override
	public String toString() {

		return super.toString() + System.lineSeparator() + "\tNationality: " + nationality;
	}

}
