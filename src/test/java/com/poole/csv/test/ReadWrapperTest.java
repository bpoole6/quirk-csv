package com.poole.csv.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.poole.csv.wrappers.read.ReadWrapper;
import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import com.poole.csv.annotation.CSVReadBinding;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVType;
import com.poole.csv.processor.CSVProcessor;

public class ReadWrapperTest {

	@Test
	public void inlineWrapperTest() throws IOException {
		final String str = "date,dob" + System.lineSeparator() + "19900511,19900511";
		CSVProcessor<W1> processor = new CSVProcessor<>(W1.class);
		W1 w1 = processor.parse(new StringReader(str)).get(0);
		assertTrue(w1.date.equals(LocalDate.of(1990, 05, 11)));
	}

	@Test
	public void globalWrapperTest() throws IOException {
		final String str = "date" + System.lineSeparator() + "19900511";
		Map<Class, ReadWrapper> m = new HashMap<>();
		m.put(LocalDate.class, new LocalDateReadWrapper());
		CSVProcessor<W2> processor = new CSVProcessor<>(W2.class, m,new HashMap<>());
		W2 w2 = processor.parse(new StringReader(str), CSVFormat.DEFAULT).get(0);
		assertTrue(w2.date.equals(LocalDate.of(1990, 05, 11)));
	}

	/**
	 * Make sure that inline is used of global Wrappers
	 * 
	 * @throws IOException
	 */
	@Test
	public void inlineOverGlobalTest() throws IOException {
		final String str = "date" + System.lineSeparator() + "19900511";
		Map<Class, ReadWrapper> m = new HashMap<>();
		m.put(LocalDate.class, new LocalDateReadWrapperFail());
		CSVProcessor<W3> processor = new CSVProcessor<>(W3.class,m,new HashMap<>());
		W3 w3 = processor.parse(new StringReader(str), CSVFormat.DEFAULT).get(0);
		assertTrue(w3.date.equals(LocalDate.of(1990, 05, 11)));
	}

	@CSVReadComponent(type = CSVType.NAMED)
	public static class W1 {

		LocalDate date;
		@CSVReadBinding(header = "dob", wrapper = LocalDateReadWrapper.class)
		LocalDate dob;

		@CSVReadBinding(header = "date", wrapper = LocalDateReadWrapper.class)
		public void setDate(LocalDate date) {
			this.date = date;
		}
	}

	@CSVReadComponent(type = CSVType.NAMED)
	public static class W2 {

		LocalDate date;

		@CSVReadBinding(header = "date")
		public void setDate(LocalDate date) {
			this.date = date;
		}
	}

	@CSVReadComponent(type = CSVType.NAMED)
	public static class W3 {

		LocalDate date;

		@CSVReadBinding(header = "date", wrapper = LocalDateReadWrapper.class)
		public void setDate(LocalDate date) {
			this.date = date;
		}
	}

	public static class LocalDateReadWrapper implements ReadWrapper {

		@Override
		public Object apply(String str) {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
			try {
				return LocalDate.parse(str, df);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}

	}

	public static class LocalDateReadWrapperFail implements ReadWrapper {

		@Override
		public Object apply(String str) {
			throw new RuntimeException();

		}
	}
}