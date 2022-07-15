package com.quirk.csv.test;

import com.quirk.csv.annotation.*;
import com.quirk.csv.exception.NamedParserException;
import com.quirk.csv.exception.UninstantiableException;
import com.quirk.csv.processor.CSVProcessor;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NamedTest {
	
	private final static String LINE_SEPARATOR = "\r\n";

	@Test()
	public void NumberFormatExTest() throws IOException {

		CSVProcessor<O1> p = new CSVProcessor<>(O1.class);
		String csv = "a,b,c" + LINE_SEPARATOR + "a,0,1"+LINE_SEPARATOR;
		List<O1> o1s = p.parse(new StringReader(csv));
		O1 o1 = new O1();
		o1.s = "a";
		o1.j = 1;
		o1.i= 0;
		assertEquals(o1, o1s.get(0));

		StringWriter sw = new StringWriter();
		p.write(o1s,sw);
		assertEquals(csv,sw.toString());
	}

	@Test(expected = UninstantiableException.class)
	public void readUninstantiableExTest() throws IOException {

		CSVProcessor<O2> p = new CSVProcessor<>(O2.class);
		List<O2> o2 = p.parse(new StringReader("a,b,c" + LINE_SEPARATOR + "a,j,u"));

	}
	@Test(expected = NamedParserException.class)
	public void readNamedParserExceptionTest() throws IOException {

		CSVProcessor<O3> p = new CSVProcessor<>(O3.class);
		 p.parse(new StringReader("a,b,c" + LINE_SEPARATOR + "a,j,u"));
	}


	@Test(expected = NamedParserException.class)
	public void writeNamedParserExceptionTest() throws IOException {

		CSVProcessor<O3> p = new CSVProcessor<>(O3.class);
		O3 o = new O3();
		o.i=3;
		o.s="s";
		p.write(Arrays.asList(o),new StringWriter());
	}

	@Test(expected =NamedParserException.class)
	public void duplicateOrderAssignmentTest() throws IOException{
		CSVProcessor<O4> p = new CSVProcessor<>(O4.class);
	}
	@CSVReadComponent(type = CSVType.NAMED)
	@CSVWriteComponent(type = CSVType.NAMED, namedIsOrdered = true)
	public static class O1 {

		@CSVReadBinding(header = "a")
		@CSVWriteBinding(header = "a",order = 0)
		String s;
		@CSVReadBinding(header = "b")
		@CSVWriteBinding(header = "b",order = 1)
		int i;
		int j;

		@CSVReadBinding(header = "c")
		public void setJ(int j) {
			this.j = j;
		}

		@CSVWriteBinding(header = "c",order = 2)
		public int getJ() {
			return j;
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

	@CSVReadComponent(type = CSVType.NAMED)
	@CSVWriteComponent(type = CSVType.NAMED)
	private static class O2 {

		@CSVReadBinding(header = "a")
		@CSVWriteBinding(header = "a")
		String s;

		@CSVReadBinding(header = "bb")
		@CSVWriteBinding(header = "bb")
		int i;

	}
	@CSVReadComponent(type = CSVType.NAMED)
	@CSVWriteComponent(type = CSVType.NAMED)
	private static class O3 {

		@CSVReadBinding(header = "a")
		@CSVWriteBinding(header = "a")
		String s;
		@CSVReadBinding(header = "a")
		@CSVWriteBinding(header = "a")
		int i;

	}

	@CSVReadComponent(type = CSVType.NAMED)
	@CSVWriteComponent(type = CSVType.NAMED,namedIsOrdered = true)
	private static class O4 {

		@CSVReadBinding(header = "a")
		@CSVWriteBinding(header = "a",order = 0)
		String s;

		@CSVReadBinding(header = "bb")
		@CSVWriteBinding(header = "bb",order = 0)
		int i;

	}
}
