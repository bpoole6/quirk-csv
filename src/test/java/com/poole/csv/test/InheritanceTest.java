package com.poole.csv.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.poole.csv.annotation.CSVReadColumn;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.processor.CSVProcessor;

public class InheritanceTest {

	@Test
	public void orderTest() throws IOException {
		final String str = "1,sts";
		ChildO1 o = new ChildO1();
		o.a = 1;
		o.b = "sts";
		CSVProcessor processor = new CSVProcessor(ChildO1.class);
		List<ChildO1> list = processor.parse(new StringReader(str));
		assertTrue(list.get(0).equals(o));

	}

	@Test
	public void namedTest() throws IOException {
		final String str = "a,b" + System.lineSeparator() + "1,sts";
		ChildN1 o = new ChildN1();
		o.a = 1;
		o.b = "sts";
		CSVProcessor processor = new CSVProcessor(ChildN1.class);
		List<ChildN1> list = processor.parse(new StringReader(str));
		assertTrue(list.get(0).equals(o));

	}

	@CSVReadComponent(type = CSVReaderType.ORDER)
	public static class ParentO1 {
		@CSVReadColumn(order = 0)
		Integer a;

	}

	@CSVReadComponent(type = CSVReaderType.ORDER, inheritSuper = true)
	public static class ChildO1 extends ParentO1 {
		String b;

		@CSVReadColumn(order = 1)
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

	@CSVReadComponent(type = CSVReaderType.NAMED)
	public static class ParentN1 {
		@CSVReadColumn(header = "a")
		Integer a;

	}

	@CSVReadComponent(type = CSVReaderType.NAMED, inheritSuper = true)
	public static class ChildN1 extends ParentN1 {
		String b;

		@CSVReadColumn(header = "b")
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