package com.poole.csv.order;

import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVNumberOrder;
import com.poole.csv.annotation.CSVReaderType;

@CSVComponent(type=CSVReaderType.ORDER)
public class OrderDummy {

	
	@CSVNumberOrder(order = 0)
	String s;
	@CSVNumberOrder(order = 3)

	Integer i;
	
}
