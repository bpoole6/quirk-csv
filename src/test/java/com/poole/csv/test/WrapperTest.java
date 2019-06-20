package com.poole.csv.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.processor.CSVProcessor;
import com.poole.csv.wrappers.Wrapper;

public class WrapperTest {

	@Test
	public void inlineWrapperTest() throws IOException {
		final String str = "date,dob" + System.lineSeparator() + "19900511,19900511";
		CSVProcessor processor = new CSVProcessor();
		W1 w1 = processor.parse(new StringReader(str), W1.class).get(0);
		assertTrue(w1.date.equals(LocalDate.of(1990, 05, 11)));
	}

	@Test
	public void globalWrapperTest() throws IOException {
		final String str = "date" + System.lineSeparator() + "19900511";
		CSVProcessor processor = new CSVProcessor();
		Map<Class, Wrapper> m = new HashMap<>();
		m.put(LocalDate.class, new LocalDateWrapper());
		W2 w2 = processor.parse(new StringReader(str), W2.class, CSVFormat.DEFAULT, m).get(0);
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
		CSVProcessor processor = new CSVProcessor();
		Map<Class, Wrapper> m = new HashMap<>();
		m.put(LocalDate.class, new LocalDateWrapperFail());
		W3 w3 = processor.parse(new StringReader(str), W3.class, CSVFormat.DEFAULT, m).get(0);
		assertTrue(w3.date.equals(LocalDate.of(1990, 05, 11)));
	}

	@CSVComponent(type = CSVReaderType.NAMED)
	public static class W1 {

		LocalDate date;
		@CSVColumn(header = "dob", wrapper = LocalDateWrapper.class)
		LocalDate dob;

		@CSVColumn(header = "date", wrapper = LocalDateWrapper.class)
		public void setDate(LocalDate date) {
			this.date = date;
		}
	}

	@CSVComponent(type = CSVReaderType.NAMED)
	public static class W2 {

		LocalDate date;

		@CSVColumn(header = "date")
		public void setDate(LocalDate date) {
			this.date = date;
		}
	}

	@CSVComponent(type = CSVReaderType.NAMED)
	public static class W3 {

		LocalDate date;

		@CSVColumn(header = "date", wrapper = LocalDateWrapper.class)
		public void setDate(LocalDate date) {
			this.date = date;
		}
	}

	public static class LocalDateWrapper implements Wrapper {

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

	public static class LocalDateWrapperFail implements Wrapper {

		@Override
		public Object apply(String str) {
			throw new RuntimeException();

		}
	}
}