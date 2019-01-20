package com.poole.csv;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.exception.MethodParameterException;
import com.poole.csv.exception.MissingCSVComponent;
import com.poole.csv.exception.MissingWrapperException;
import com.poole.csv.exception.NullableException;
import com.poole.csv.exception.UninstantiableException;
import com.poole.csv.exception.WrapperInstantiationException;
import com.poole.csv.processor.CSVProcessor;
import com.poole.csv.wrappers.Wrapper;

public class NegativeTest {
	@Test(expected = MissingCSVComponent.class)
	public void missingCSVComponent() throws IOException {
		CSVProcessor p = new CSVProcessor();
		p.parse(new StringReader("a,j"), O1.class);

	}

	@Test(expected = MissingWrapperException.class)

	public void missingWrapperTest() throws IOException {
		CSVProcessor p = new CSVProcessor();
		p.parse(new StringReader("a,j"), O2.class);
	}

	@Test(expected = UninstantiableException.class)
	public void nonPublicClassTestOrder() throws IOException {
		CSVProcessor p = new CSVProcessor();
		p.parse(new StringReader("a"), O3a.class);
	}

	@Test(expected = UninstantiableException.class)
	public void nonPublicClassTestNamed() throws IOException {
		CSVProcessor p = new CSVProcessor();
		p.parse(new StringReader("date" + System.lineSeparator() + "a"), O3b.class);
	}

	@Test(expected = WrapperInstantiationException.class)
	public void failedWrapperInstantiateTest() throws IOException {
		CSVProcessor p = new CSVProcessor();
		p.parse(new StringReader("a"), O4.class);
	}
	@Test(expected = MethodParameterException.class)
	public void tooManyArgsTest() throws IOException {
		CSVProcessor p = new CSVProcessor();
		p.parse(new StringReader("a"), O5.class);
	}
	@Test(expected = NullableException.class)
	public void nullableExTest() throws IOException {
		CSVProcessor p = new CSVProcessor();
		p.parse(new StringReader(","), O6.class);
	}
	
	public static class O1 {
	}

	@CSVComponent(type = CSVReaderType.ORDER)
	public static class O2 {
		@CSVColumn(order = 0)
		LocalDate date;
	}

	@CSVComponent(type = CSVReaderType.ORDER)
	protected static class O3a {
		@CSVColumn(order = 0)
		String date;
	}

	@CSVComponent(type = CSVReaderType.NAMED)
	protected static class O3b {
		@CSVColumn(header = "date")
		String date;
	}

	protected class W1 implements Wrapper {

		@Override
		public Object apply(String str) {
			return null;
		}

	}

	@CSVComponent(type = CSVReaderType.ORDER)
	public static class O4 {
		@CSVColumn(order = 0, wrapper = W1.class)
		String date;
	}
	
	@CSVComponent(type = CSVReaderType.ORDER)
	public static class O5 {
		String date;
		@CSVColumn(order = 0)
		public void setDate(String date, String failMe){
			
		}
	}
	@CSVComponent(type = CSVReaderType.ORDER)
	public static class O6 {
		@CSVColumn(order = 0,isNullable=false)
		String date;
	}
}
