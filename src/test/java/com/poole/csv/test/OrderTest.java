package com.poole.csv.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.poole.csv.annotation.CSVReadBinding;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVType;
import com.poole.csv.exception.OrderParserException;
import com.poole.csv.exception.UninstantiableException;
import com.poole.csv.processor.CSVProcessor;

public class OrderTest {

	@Test()
	public void outOfBoundsNumberFormatExTest() throws IOException {
		CSVProcessor p = new CSVProcessor(O1.class);
		List<O1> o1s = p.parse(new StringReader("a,j,NAMED,dd"));
		O1 o1 = new O1();
		o1.s = "a";
		o1.j = 0;
		o1.type= CSVType.NAMED;
		assertTrue(o1.equals(o1s.get(0)));
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
	public static class O1 {

		
		@CSVReadBinding(order = 0)
		String s;
		@CSVReadBinding(order = 4)
		int i;
		int j;
		@CSVReadBinding(order=2)
        CSVType type;
		@CSVReadBinding(order=3)
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
