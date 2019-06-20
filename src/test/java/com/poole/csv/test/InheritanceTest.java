package com.poole.csv.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Test;

import com.poole.csv.annotation.CSVColumn;
import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.processor.CSVProcessor;
import com.poole.csv.wrappers.Wrapper;

public class InheritanceTest {

	@Test
	public void orderTest() throws IOException {
		final String str = "1,sts";
		ChildO1 o = new ChildO1();
		o.a = 1;
		o.b = "sts";
		CSVProcessor processor = new CSVProcessor();
		List<ChildO1> list = processor.parse(new StringReader(str), ChildO1.class);
		assertTrue(list.get(0).equals(o));

	}

	@Test
	public void namedTest() throws IOException {
		final String str = "a,b" + System.lineSeparator() + "1,sts";
		ChildN1 o = new ChildN1();
		o.a = 1;
		o.b = "sts";
		CSVProcessor processor = new CSVProcessor();
		List<ChildN1> list = processor.parse(new StringReader(str), ChildN1.class);
		assertTrue(list.get(0).equals(o));

	}

	@CSVComponent(type = CSVReaderType.ORDER)
	public static class ParentO1 {
		@CSVColumn(order = 0)
		Integer a;

	}

	@CSVComponent(type = CSVReaderType.ORDER, inheritSuper = true)
	public static class ChildO1 extends ParentO1 {
		String b;

		@CSVColumn(order = 1)
		public void setB(String b) {
			this.b = b;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ChildO1 other = (ChildO1) obj;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			else if (!a.equals(other.a))
				return false;
			return true;
		}

	}

	@CSVComponent(type = CSVReaderType.NAMED)
	public static class ParentN1 {
		@CSVColumn(header = "a")
		Integer a;

	}

	@CSVComponent(type = CSVReaderType.NAMED, inheritSuper = true)
	public static class ChildN1 extends ParentN1 {
		String b;

		@CSVColumn(header = "b")
		public void setB(String b) {
			this.b = b;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ChildN1 other = (ChildN1) obj;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			else if (!a.equals(other.a))
				return false;
			return true;
		}

	}
}