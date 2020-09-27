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

public class PrimitiveTest {

	@Test
	public void primitiveTest() throws IOException {
		Primitive p = new Primitive();
		p.intW = new Integer(1);
		p.intS = 1;
		p.byteW = new Byte("2");
		p.byteS = 2;
		p.charW = new Character('c');
		p.charS = 'c';
		p.shortW = new Short((short) 4);
		p.shortS = 4;
		p.longW = new Long(6);
		p.longS = 6;
		p.floatW = new Float(5.4);
		p.floatS = 5.4F;
		p.doubleW = new Double(5.4);
		p.doubleS = 5.4;
		p.booleanW = new Boolean(true);
		p.booleanS = true;
		p.stringW = "stringLife";
		p.type = CSVReaderType.NAMED;
		final String str = "1,1,2,2,c,c,4,4,6,6,5.4,5.4,5.4,5.4,true,true,stringLife,NAMED";
		CSVProcessor processor = new CSVProcessor(Primitive.class);
		List<Primitive> list = processor.parse(new StringReader(str));
		System.out.println(list);
		assertTrue(p.equals(list.get(0)));
	}

	@CSVReadComponent(type = CSVReaderType.ORDER)
	public static class Primitive {
		@CSVReadColumn(order = 0)
		Integer intW;
		@CSVReadColumn(order = 1)
		int intS;
		@CSVReadColumn(order = 2)
		Byte byteW;
		@CSVReadColumn(order = 3)
		byte byteS;
		@CSVReadColumn(order = 4)
		Character charW;
		@CSVReadColumn(order = 5)
		char charS;
		@CSVReadColumn(order = 6)
		Short shortW;
		@CSVReadColumn(order = 7)
		short shortS;
		@CSVReadColumn(order = 8)
		Long longW;
		@CSVReadColumn(order = 9)
		long longS;
		@CSVReadColumn(order = 10)
		Float floatW;
		@CSVReadColumn(order = 11)
		float floatS;
		@CSVReadColumn(order = 12)
		Double doubleW;
		@CSVReadColumn(order = 13)
		double doubleS;
		@CSVReadColumn(order = 14)
		Boolean booleanW;
		@CSVReadColumn(order = 15)
		boolean booleanS;
		@CSVReadColumn(order = 16)
		String stringW;
		CSVReaderType type;

		@CSVReadColumn(order = 17)
		public void setType(CSVReaderType type) {
			this.type = type;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (booleanS ? 1231 : 1237);
			result = prime * result + ((booleanW == null) ? 0 : booleanW.hashCode());
			result = prime * result + byteS;
			result = prime * result + ((byteW == null) ? 0 : byteW.hashCode());
			result = prime * result + charS;
			result = prime * result + ((charW == null) ? 0 : charW.hashCode());
			long temp;
			temp = Double.doubleToLongBits(doubleS);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((doubleW == null) ? 0 : doubleW.hashCode());
			result = prime * result + Float.floatToIntBits(floatS);
			result = prime * result + ((floatW == null) ? 0 : floatW.hashCode());
			result = prime * result + intS;
			result = prime * result + ((intW == null) ? 0 : intW.hashCode());
			result = prime * result + (int) (longS ^ (longS >>> 32));
			result = prime * result + ((longW == null) ? 0 : longW.hashCode());
			result = prime * result + shortS;
			result = prime * result + ((shortW == null) ? 0 : shortW.hashCode());
			result = prime * result + ((stringW == null) ? 0 : stringW.hashCode());
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
			Primitive other = (Primitive) obj;
			if (booleanS != other.booleanS)
				return false;
			if (booleanW == null) {
				if (other.booleanW != null)
					return false;
			} else if (!booleanW.equals(other.booleanW))
				return false;
			if (byteS != other.byteS)
				return false;
			if (byteW == null) {
				if (other.byteW != null)
					return false;
			} else if (!byteW.equals(other.byteW))
				return false;
			if (charS != other.charS)
				return false;
			if (charW == null) {
				if (other.charW != null)
					return false;
			} else if (!charW.equals(other.charW))
				return false;
			if (Double.doubleToLongBits(doubleS) != Double.doubleToLongBits(other.doubleS))
				return false;
			if (doubleW == null) {
				if (other.doubleW != null)
					return false;
			} else if (!doubleW.equals(other.doubleW))
				return false;
			if (Float.floatToIntBits(floatS) != Float.floatToIntBits(other.floatS))
				return false;
			if (floatW == null) {
				if (other.floatW != null)
					return false;
			} else if (!floatW.equals(other.floatW))
				return false;
			if (intS != other.intS)
				return false;
			if (intW == null) {
				if (other.intW != null)
					return false;
			} else if (!intW.equals(other.intW))
				return false;
			if (longS != other.longS)
				return false;
			if (longW == null) {
				if (other.longW != null)
					return false;
			} else if (!longW.equals(other.longW))
				return false;
			if (shortS != other.shortS)
				return false;
			if (shortW == null) {
				if (other.shortW != null)
					return false;
			} else if (!shortW.equals(other.shortW))
				return false;
			if (stringW == null) {
				if (other.stringW != null)
					return false;
			} else if (!stringW.equals(other.stringW))
				return false;
			if (type != other.type)
				return false;
			return true;
		}

	}
}
