package com.quirk.csv.test;

import com.quirk.csv.annotation.*;
import com.quirk.csv.exception.OrderParserException;
import com.quirk.csv.exception.UninstantiableException;
import com.quirk.csv.processor.CSVProcessor;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderTest {

	private final static String LINE_SEPARATOR = "\r\n";

	@Test()
	public void positiveOrderTest() throws IOException {
		CSVProcessor<O1> p = new CSVProcessor<>(O1.class);
		String csv = "a,0,NAMED,d,0"+LINE_SEPARATOR;
		List<O1> o1s = p.parse(new StringReader(csv));
		O1 o1 = new O1();
		o1.s = "a";
		o1.j = 0;
		o1.i = 0;
		o1.type= CSVType.NAMED;
		o1.c = 'd';
		assertEquals(o1, o1s.get(0));

		StringWriter sw = new StringWriter();
		p.write(o1s,sw);
		assertEquals(csv,sw.toString());
	}

	@Test(expected = UninstantiableException.class)
	public void UninstantiableExTest() throws IOException {

		CSVProcessor p = new CSVProcessor(O2.class);
		List<O2> o1 = p.parse(new StringReader("a,j,NAMED"));

	}
	@Test(expected = OrderParserException.class)
	public void repeatedOrderTest() throws IOException {

		CSVProcessor p = new CSVProcessor(O4.class);
		p.parse(new StringReader("a,j,NAMED"));

	}
	@CSVReadComponent(type = CSVType.ORDER)
	@CSVWriteComponent(type = CSVType.ORDER)
	public static class O1 {

		
		@CSVReadBinding(order = 0)
		@CSVWriteBinding(order = 0)
		String s;
		@CSVReadBinding(order = 4)
		@CSVWriteBinding(order = 4)
		int i;
		@CSVWriteBinding(order = 1)
		int j;
		@CSVReadBinding(order=2)
		@CSVWriteBinding(order=2)
        CSVType type;

		@CSVReadBinding(order=3)
		@CSVWriteBinding(order=3)
		char c;
		@CSVReadBinding(order = 1)
		public void setJ(int j) {
			this.j = j;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + c;
			result = prime * result + i;
			result = prime * result + j;
			result = prime * result + ((s == null) ? 0 : s.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
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
			O1 other = (O1) obj;
			if (c != other.c)
				return false;
			if (i != other.i)
				return false;
			if (j != other.j)
				return false;
			if (s == null) {
				if (other.s != null)
					return false;
			} else if (!s.equals(other.s))
				return false;
			if (type != other.type)
				return false;
			return true;
		}
		

		

	}

	@CSVReadComponent(type = CSVType.ORDER)
	private static class O2 {
		@CSVReadBinding(order = 0)
		String s;
		@CSVReadBinding(order = 1, isNullable = false)
		Integer i;
	}

	@CSVReadComponent(type = CSVType.ORDER)
	private static class O3 {
		@CSVReadBinding(order = 0)
		String s;
		@CSVReadBinding(order = 1, isNullable = false)
		Integer i;
	}
	@CSVReadComponent(type = CSVType.ORDER)
	private static class O4 {
		@CSVReadBinding(order = 0)
		String s;
		@CSVReadBinding(order = 0)
		Integer i;
	}
}
