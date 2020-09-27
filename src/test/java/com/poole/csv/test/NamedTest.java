package com.poole.csv.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.poole.csv.annotation.CSVReadColumn;
import com.poole.csv.annotation.CSVReadComponent;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.exception.NamedParserException;
import com.poole.csv.exception.UninstantiableException;
import com.poole.csv.processor.CSVProcessor;

public class NamedTest {
	

	@Test()
	public void NumberFormatExTest() throws IOException {

		CSVProcessor p = new CSVProcessor(O1.class);
		List<O1> o1s = p.parse(new StringReader("a,b,c" + System.lineSeparator() + "a,j,u"));
		O1 o1 = new O1();
		o1.s = "a";
		o1.j = 0;
		assertTrue(o1.equals(o1s.get(0)));
	}

	@Test(expected = UninstantiableException.class)
	public void UninstantiableExTest() throws IOException {

		CSVProcessor p = new CSVProcessor(O2.class);
		List<O2> o1 = p.parse(new StringReader("a,b,c" + System.lineSeparator() + "a,j,u"));

	}
	@Test(expected = NamedParserException.class)
	public void NamedParserExceptionTest() throws IOException {

		CSVProcessor p = new CSVProcessor(O3.class);
		 p.parse(new StringReader("a,b,c" + System.lineSeparator() + "a,j,u"));

	}

	@CSVReadComponent(type = CSVReaderType.NAMED)
	public static class O1 {

		@CSVReadColumn(header = "a")
		String s;
		@CSVReadColumn(header = "bb")
		int i;
		int j;

		@CSVReadColumn(header = "c")
		public void setJ(int j) {
			this.j = j;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + i;
			result = prime * result + j;
			result = prime * result + ((s == null) ? 0 : s.hashCode());
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
			if (i != other.i)
				return false;
			if (j != other.j)
				return false;
			if (s == null) {
				if (other.s != null)
					return false;
			} else if (!s.equals(other.s))
				return false;
			return true;
		}

	}

	@CSVReadComponent(type = CSVReaderType.NAMED)
	private static class O2 {

		@CSVReadColumn(header = "a")
		String s;
		@CSVReadColumn(header = "bb")
		int i;

	}
	@CSVReadComponent(type = CSVReaderType.NAMED)
	private static class O3 {

		@CSVReadColumn(header = "a")
		String s;
		@CSVReadColumn(header = "a")
		int i;

	}
}
