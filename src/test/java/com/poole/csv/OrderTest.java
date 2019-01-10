package com.poole.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVNumberOrder;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.order.OrderDummy;
import com.poole.csv.processor.CSVProcessor;

public class OrderTest {
	// Test when a record goes out of bounds
	// Test when a non-nullable is null
	// Test to make sure a nullable can be nulled
	// Test when two fields hold the same order
	// Test when two methods hold the same order
	// Test when a field and a method hold the same order
	// Test when a complex datatype is used instead of primitives and their
	// wrappers
	//

	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void outOfBoundsTest() throws IOException {
		
		
		CSVProcessor p=new CSVProcessor();
		p.parse( new StringReader("a,4"), OrderDummy.class);
	}
}
