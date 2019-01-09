package com.poole.csv.processor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;

public class HolderField {
	private Field field;
	private Method method;
	private Function<String, ?> setValue;

	public HolderField(Field field) {
		this.field = field;
		setConverter(field.getType());
	}

	public HolderField(Method method) {
		this.method = method;
		setConverter(method.getParameterTypes()[0]);

		method.getParameterTypes()[0].isEnum();

	}

	private void setConverter(Class type) {
		if (type.isEnum()) {
			Function<Class, Function<String, ?>> func;
			func = (Class clazz) -> {
				return (String value) -> {
					return Enum.valueOf(clazz, value);
				};
			};
			this.setValue = func.apply(type);

		} else if (type == int.class || type == Integer.class) {
			this.setValue = (String s) -> Integer.valueOf(s);
		} else if (type == byte.class || type == Byte.class) {
			this.setValue = (String s) -> Byte.valueOf(s);
		} else if (type == char.class || type == Character.class) {
			this.setValue = (String s) -> {
				if (s.length() != 1)
					throw new RuntimeException("Conversion of string " + s + " to char failed");
				return Character.valueOf(s.charAt(0));
			};
		} else if (type == short.class || type == Short.class) {
			this.setValue = (String s) -> Short.valueOf(s);
		} else if (type == long.class || type == Long.class) {
			this.setValue = (String s) -> Long.valueOf(s);
		} else if (type == float.class || type == Float.class) {
			this.setValue = (String s) -> Float.valueOf(s);
		} else if (type == double.class || type == Double.class) {
			this.setValue = (String s) -> Double.valueOf(s);
		} else if (type == boolean.class || type == Boolean.class) {
			this.setValue = (String s) -> Boolean.valueOf(s);
		} else if (type == String.class ) {
			this.setValue = (String s) ->s;
		}
	}

	public void setValue(Object obj, String value)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (field != null)
			field.set(obj, this.setValue.apply(value));
		else if (method != null)
			method.invoke(obj, this.setValue.apply(value));
	}
}
