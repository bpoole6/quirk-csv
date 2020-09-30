package com.poole.csv.test;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;

import org.junit.Test;

import com.poole.csv.annotation.CSVReadBinding;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVType;
import com.poole.csv.exception.MethodParameterException;
import com.poole.csv.exception.MissingCSVComponent;
import com.poole.csv.exception.MissingWrapperException;
import com.poole.csv.exception.NullableException;
import com.poole.csv.exception.UninstantiableException;
import com.poole.csv.exception.WrapperInstantiationException;
import com.poole.csv.processor.CSVProcessor;
import com.poole.csv.wrappers.read.ReadWrapper;

public class NegativeTest {
	@Test(expected = MissingCSVComponent.class)
	public void missingCSVComponent() throws IOException {
		CSVProcessor p = new CSVProcessor(O1.class);
		p.parse(new StringReader("a,j"));

	}

	@Test(expected = MissingWrapperException.class)

	public void missingWrapperTest() throws IOException {
		CSVProcessor p = new CSVProcessor(O2.class);
		p.parse(new StringReader("a,j"));
	}

	@Test(expected = UninstantiableException.class)
	public void nonPublicClassTestOrder() throws IOException {
		CSVProcessor p = new CSVProcessor(O3a.class);
		p.parse(new StringReader("a"));
	}

	@Test(expected = UninstantiableException.class)
	public void nonPublicClassTestNamed() throws IOException {
		CSVProcessor p = new CSVProcessor(O3b.class);
		p.parse(new StringReader("date" + System.lineSeparator() + "a"));
	}

	@Test(expected = WrapperInstantiationException.class)
	public void failedWrapperInstantiateTest() throws IOException {
		CSVProcessor p = new CSVProcessor(O4.class);
		p.parse(new StringReader("a"));
	}
	@Test(expected = MethodParameterException.class)
	public void tooManyArgsTest() throws IOException {
		CSVProcessor p = new CSVProcessor(O5.class);
		p.parse(new StringReader("a"));
	}
	@Test(expected = NullableException.class)
	public void nullableExTest() throws IOException {
		CSVProcessor p = new CSVProcessor(O6.class);
		p.parse(new StringReader(","));
	}
	
	public static class O1 {
	}

	@CSVReadComponent(type = CSVType.ORDER)
	public static class O2 {
		@CSVReadBinding(order = 0)
		LocalDate date;
	}

	@CSVReadComponent(type = CSVType.ORDER)
	protected static class O3a {
		@CSVReadBinding(order = 0)
		String date;
	}

	@CSVReadComponent(type = CSVType.NAMED)
	protected static class O3b {
		@CSVReadBinding(header = "date")
		String date;
	}

	protected class W1 implements ReadWrapper {

		@Override
		public Object apply(String str) {
			return null;
		}

	}

	@CSVReadComponent(type = CSVType.ORDER)
	public static class O4 {
		@CSVReadBinding(order = 0, wrapper = W1.class)
		String date;
	}
	
	@CSVReadComponent(type = CSVType.ORDER)
	public static class O5 {
		String date;
		@CSVReadBinding(order = 0)
		public void setDate(String date, String failMe){
			
		}
	}
	@CSVReadComponent(type = CSVType.ORDER)
	public static class O6 {
		@CSVReadBinding(order = 0,isNullable=false)
		String date;
	}
}
