package com.poole.csv.order;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;

@CSVComponent(type = CSVReaderType.ORDER)
public class OrderDummy {

	@CSVColumn(order = 0)
	String s;
	@CSVColumn(order = 3)

	Integer i;

}
