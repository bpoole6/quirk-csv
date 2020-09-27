package com.poole.csv.wrappers.write.defaults;

import com.poole.csv.wrappers.write.WriteWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the default WriteWrappers for all of the primitive types
 */
public class DefaultWriteWrappers {
	@SuppressWarnings("rawtypes")
	public static Map<Class, WriteWrapper> getDefault() {
		Map<Class, WriteWrapper> map = new HashMap<>();
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
	 * Default WriteWrapper for @java.lang.Integer and int
	 *
	 */
	public static class IntDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}
	}

	/**
	 * Default WriteWrapper for @java.lang.Byte and byte
	 *
	 */
	public static class ByteDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}
	}

	/**
	 * Default WriteWrapper for @java.lang.Character and char
	 *
	 */
	public static class CharDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}

	}

	/**
	 * Default WriteWrapper for @java.lang.Short and short
	 *
	 */
	public static class ShortDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}
	}

	/**
	 * Default WriteWrapper for @java.lang.Long and long
	 *
	 */
	public static class LongDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}
	}

	/**
	 * Default WriteWrapper for @java.lang.Float and float
	 *
	 */
	public static class FloatDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}
	}

	/**
	 * Default WriteWrapper for @java.lang.Double and double
	 *
	 */
	public static class DoubleDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}
	}

	/**
	 * Default WriteWrapper for @java.lang.Boolean and boolean
	 *
	 */
	public static class BooleanDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}
	}

	/**
	 * Default WriteWrapper for @java.lang.String and string Doing alot here
	 */
	public static class StringDefault implements WriteWrapper {

		@Override
		public String apply(Object obj) {
			return obj.toString();
		}

	}
}
