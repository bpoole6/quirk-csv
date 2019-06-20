package com.poole.csv.wrappers.defaults;

import java.util.HashMap;
import java.util.Map;

import com.poole.csv.wrappers.Wrapper;

/**
 * Contains the default Wrappers for all of the primitive types
 */
public class DefaultWrappers {
	@SuppressWarnings("rawtypes")
	public static Map<Class, Wrapper> getDefault() {
		Map<Class, Wrapper> map = new HashMap<>();
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
	 * Default Wrapper for @java.lang.Integer and int
	 *
	 */
	public static class IntDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			return Integer.valueOf(str);
		}
	}

	/**
	 * Default Wrapper for @java.lang.Byte and byte
	 *
	 */
	public static class ByteDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			return Byte.valueOf(str);
		}
	}

	/**
	 * Default Wrapper for @java.lang.Character and char
	 *
	 */
	public static class CharDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			if (str.length() != 1)
				throw new IllegalArgumentException("Conversion of string " + str + " to char failed");
			return Character.valueOf(str.charAt(0));
		}
	}

	/**
	 * Default Wrapper for @java.lang.Short and short
	 *
	 */
	public static class ShortDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			return Short.valueOf(str);
		}
	}

	/**
	 * Default Wrapper for @java.lang.Long and long
	 *
	 */
	public static class LongDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			return Long.valueOf(str);
		}
	}

	/**
	 * Default Wrapper for @java.lang.Float and float
	 *
	 */
	public static class FloatDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			return Float.valueOf(str);
		}
	}

	/**
	 * Default Wrapper for @java.lang.Double and double
	 *
	 */
	public static class DoubleDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			return Double.valueOf(str);
		}
	}

	/**
	 * Default Wrapper for @java.lang.Boolean and boolean
	 *
	 */
	public static class BooleanDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			return Boolean.valueOf(str);
		}
	}

	/**
	 * Default Wrapper for @java.lang.String and string Doing alot here
	 */
	public static class StringDefault implements Wrapper {

		@Override
		public Object apply(String str) {
			return str;
		}
	}
}
