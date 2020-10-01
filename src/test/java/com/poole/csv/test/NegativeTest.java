package com.poole.csv.test;

import com.poole.csv.annotation.*;
import com.poole.csv.exception.*;
import com.poole.csv.processor.CSVProcessor;
import com.poole.csv.wrappers.read.ReadWrapper;
import com.poole.csv.wrappers.write.WriteWrapper;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;

public class NegativeTest {
	@Test(expected = MissingCSVComponent.class)
	public void missingReadCSVComponent() throws IOException {
		CSVProcessor<O1> p = new CSVProcessor<>(O1.class);
		p.parse(new StringReader("a,j"));
	}
	@Test(expected = MissingCSVComponent.class)
	public void missingWriteCSVComponent() throws IOException {
		CSVProcessor<O1> p = new CSVProcessor<>(O1.class);
		p.write(new ArrayList<>(),new StringWriter());
	}

	@Test(expected = MissingWrapperException.class)

	public void missingWrapperTest() throws IOException {
		CSVProcessor<O2> p = new CSVProcessor<>(O2.class);
		p.parse(new StringReader("a,j"));
	}

	@Test(expected = UninstantiableException.class)
	public void nonPublicClassTestOrder() throws IOException {
		CSVProcessor<O3a> p = new CSVProcessor<>(O3a.class);
		p.parse(new StringReader("a"));
	}

	@Test(expected = UninstantiableException.class)
	public void nonPublicClassTestNamed() throws IOException {
		CSVProcessor<O3b> p = new CSVProcessor<>(O3b.class);
		p.parse(new StringReader("date" + System.lineSeparator() + "a"));
	}

	@Test(expected = WrapperInstantiationException.class)
	public void failedReadWrapperInstantiateTest() throws IOException {
		CSVProcessor<O4> p = new CSVProcessor<>(O4.class);
	}

	@Test(expected = WrapperInstantiationException.class)
	public void failedWriteWrapperInstantiateTest() throws IOException {
		CSVProcessor<O4b> p = new CSVProcessor<>(O4b.class);
	}
	@Test(expected = MethodParameterException.class)
	public void tooReadManyArgsTest() throws IOException {
		CSVProcessor<O5> p = new CSVProcessor<>(O5.class);
		p.parse(new StringReader("a"));
	}
	@Test(expected = MethodParameterException.class)
	public void tooWriteManyArgsTest() throws IOException {
		CSVProcessor<O5b> p = new CSVProcessor<>(O5b.class);
	}
	@Test(expected = MethodReturnTypeException.class)
	public void returnTypeVoidWriteTest() throws IOException {
		CSVProcessor<O5c> p = new CSVProcessor<>(O5c.class);
		System.out.println();
	}
	@Test(expected = NullableException.class)
	public void nullableExTest() throws IOException {
		CSVProcessor<O6> p = new CSVProcessor<>(O6.class);
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

	protected class W1 implements ReadWrapper<String> {

		@Override
		public String apply(String str) {
			return null;
		}

	}
	protected class W1Write implements WriteWrapper<String> {

		@Override
		public String apply(String str) {
			return null;
		}

	}

	@CSVReadComponent(type = CSVType.ORDER)
	public static class O4 {
		@CSVReadBinding(order = 0, wrapper = W1.class)
		String date;
	}
	@CSVWriteComponent(type = CSVType.ORDER)
	public static class O4b {
		@CSVWriteBinding(order = 0, wrapper = W1Write.class)
		String date;
	}
	
	@CSVReadComponent(type = CSVType.ORDER)
	public static class O5 {
		String date;
		@CSVReadBinding(order = 0)
		public void setDate(String date, String failMe){
			
		}
	}
	@CSVWriteComponent(type = CSVType.ORDER)
	public static class O5b {
		String date;
		@CSVWriteBinding(order = 0)
		public String getDate(String date){
			return null;
		}
	}
	@CSVWriteComponent(type = CSVType.ORDER)
	public static class O5c {
		String date;
		@CSVWriteBinding(order = 0)
		public void getDate(){

		}
	}
	@CSVReadComponent(type = CSVType.ORDER)
	public static class O6 {
		@CSVReadBinding(order = 0,isNullable=false)
		String date;
	}
}
