package com.quirk.csv.test;

import com.quirk.csv.annotation.*;
import com.quirk.csv.processor.CSVProcessor;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InheritanceTest {
	private final static String LINE_SEPARATOR = "\r\n";

	@Test
	public void orderTest() throws IOException {
		final String str = "1,sts"+LINE_SEPARATOR;
		ChildO1 o = new ChildO1();
		o.a = 1;
		o.b = "sts";
		CSVProcessor<ChildO1> processor = new CSVProcessor<>(ChildO1.class);
		List<ChildO1> list = processor.parse(new StringReader(str));
		assertEquals(list.get(0), o);
		StringWriter sw = new StringWriter();
		processor.write(list,sw);
		assertEquals(str,sw.toString());

	}

	@Test
	public void namedTest() throws IOException {
		final String str = "a,b" + LINE_SEPARATOR + "1,sts"+LINE_SEPARATOR;
		ChildN1 o = new ChildN1();
		o.a = 1;
		o.b = "sts";
		CSVProcessor processor = new CSVProcessor(ChildN1.class);
		List<ChildN1> list = processor.parse(new StringReader(str));
		assertEquals(list.get(0), o);

		StringWriter sw = new StringWriter();
		processor.write(list,sw);
		assertEquals(str,sw.toString());


	}

	@CSVReadComponent(type = CSVType.ORDER)
	@CSVWriteComponent(type = CSVType.ORDER)
	public static class ParentO1 {
		@CSVReadBinding(order = 0)
		@CSVWriteBinding(order = 0)
		Integer a;

	}

	@CSVReadComponent(type = CSVType.ORDER, inheritSuper = true)
	@CSVWriteComponent(type = CSVType.ORDER,inheritSuper = true)
	public static class ChildO1 extends ParentO1 {
		@CSVWriteBinding(order = 1)
		String b;

		@CSVReadBinding(order = 1)

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

	@CSVReadComponent(type = CSVType.NAMED)
	@CSVWriteComponent(type = CSVType.NAMED)
	public static class ParentN1 {
		@CSVReadBinding(header = "a")
		@CSVWriteBinding(header = "a",order = 0)
		Integer a;

	}

	@CSVReadComponent(type = CSVType.NAMED, inheritSuper = true)
	@CSVWriteComponent(type = CSVType.NAMED,inheritSuper = true,namedIsOrdered = true)
	public static class ChildN1 extends ParentN1 {
		@CSVWriteBinding(header = "b",order = 1)
		String b;

		@CSVReadBinding(header = "b")
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