package com.poole.csv.wrappers.read.defaults;

import com.poole.csv.wrappers.read.ReadWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the default {@link ReadWrapper} for all of the primitive types
 */
public class DefaultReadWrappers {
	@SuppressWarnings("rawtypes")
	public static Map<Class, ReadWrapper> getDefault() {
		Map<Class, ReadWrapper> map = new HashMap<>();
		map.put(Integer.class, new IntDefault());
		map.put(int.class, new IntDefault());
		map.put(Byte.class, new ByteDefault());
		map.put(byte.class, new ByteDefault());
		map.put(Character.class, new CharDefault());
		map.put(char.class, new CharDefault());
		map.put(Short.class, new ShortDefault());
		map.put(short.class, new ShortDefault());
		map.put(Long.class, new LongDefault());
		map.put(long.class, new LongDefault());
		map.put(Float.class, new FloatDefault());
		map.put(float.class, new FloatDefault());
		map.put(Double.class, new DoubleDefault());
		map.put(double.class, new DoubleDefault());
		map.put(Boolean.class, new BooleanDefault());
		map.put(boolean.class, new BooleanDefault());
		map.put(String.class, new StringDefault());
		return map;
	}

	/**
	 * Default implementation for @java.lang.Integer and int
	 *
	 */
	public static class IntDefault implements ReadWrapper<Integer> {

		@Override
		public Integer apply(String str) {
			return Integer.valueOf(str);
		}
	}

	/**
	 * Default implementation for {@link java.lang.Byte} and byte
	 *
	 */
	public static class ByteDefault implements ReadWrapper<Byte> {

		@Override
		public Byte apply(String str) {
			return Byte.valueOf(str);
		}
	}

	/**
	 * Default implementation for {@link java.lang.Character} and char
	 *
	 */
	public static class CharDefault implements ReadWrapper<Character> {

		@Override
		public Character apply(String str) {
			if (str.length() != 1)
				throw new IllegalArgumentException("Conversion of string " + str + " to char failed");
			return str.charAt(0);
		}
	}

	/**
	 * Default implementation for {@link java.lang.Short} and short
	 *
	 */
	public static class ShortDefault implements ReadWrapper<Short> {

		@Override
		public Short apply(String str) {
			return Short.valueOf(str);
		}
	}

	/**
	 * Default implementation for {@link java.lang.Long} and long
	 *
	 */
	public static class LongDefault implements ReadWrapper<Long> {

		@Override
		public Long apply(String str) {
			return Long.valueOf(str);
		}
	}

	/**
	 * Default implementation for {@link java.lang.Float} and float
	 *
	 */
	public static class FloatDefault implements ReadWrapper<Float> {

		@Override
		public Float apply(String str) {
			return Float.valueOf(str);
		}
	}

	/**
	 * Default implementation for {@link java.lang.Double} and double
	 *
	 */
	public static class DoubleDefault implements ReadWrapper<Double> {

		@Override
		public Double apply(String str) {
			return Double.valueOf(str);
		}
	}

	/**
	 * Default implementation for {@link java.lang.Boolean} and boolean
	 *
	 */
	public static class BooleanDefault implements ReadWrapper<Boolean> {

		@Override
		public Boolean apply(String str) {
			return Boolean.valueOf(str);
		}
	}

	/**
	 * Default implementation for {@link java.lang.String} and string Doing a lot here
	 */
	public static class StringDefault implements ReadWrapper<String> {

		@Override
		public String apply(String str) {
			return str;
		}
	}
}
