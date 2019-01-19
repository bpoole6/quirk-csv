package com.poole.csv.processor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

import com.poole.csv.exception.MissingWrapperException;
import com.poole.csv.exception.NullableException;
import com.poole.csv.wrappers.Wrapper;

/**
 * Holder has the logic to determine which wrapper to use when processing
 * datatypes on specific fields and/or methods
 */
public class Holder {
	private Field field;
	private Method method;
	private Wrapper wrapper;
	private boolean isNullable;
	@SuppressWarnings("rawtypes")
	private Class type;
	private boolean isEnum = false;

	public Holder(Field field, boolean isNullable, Wrapper wrapper) {
		this.field = field;
		this.isNullable = isNullable;
		this.wrapper = wrapper;
		this.type = field.getType();
		if (this.type.isEnum())
			isEnum = true;
	}

	public Holder(Method method, boolean isNullable, Wrapper wrapper) {
		this.method = method;
		this.isNullable = isNullable;
		this.wrapper = wrapper;
		this.type = method.getParameterTypes()[0];
		if (this.type.isEnum())
			isEnum = true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setValue(Object obj, String value, Map<Class, Wrapper> setValueMap)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Supplier<?> setValue = null;

		if (this.wrapper != null) {
			setValue = () -> {
				return this.wrapper.apply(value);
			};
		} else if (setValueMap.get(type) != null) {
			setValue = () -> {
				return setValueMap.get(type).apply(value);
			};
		} else if (setValueMap.get(type) == null && isEnum) {
			setValue = () -> {
				return Enum.valueOf(this.type, value);
			};
		} else {
			throw new MissingWrapperException("Do not have a wrapper class to handle Type" + type);
		}

		if (field != null) {
			field.setAccessible(true);
			checkNullable(value, "Field: " + field.getName() + " of class "
					+ field.getDeclaringClass().getCanonicalName() + " cannot be null");
			if (!isNulled(value))
				field.set(obj, setValue.get());
		} else if (method != null) {
			checkNullable(value, "Method: " + method.getName() + " of class "
					+ method.getDeclaringClass().getCanonicalName() + " cannot be null");
			method.setAccessible(true);
			if (!isNulled(value))
				method.invoke(obj, setValue.get());
		}

	}

	public interface Function<T, R> {
		R apply(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	}

	private void checkNullable(String value, String msg) {
		if (isNulled(value) && !isNullable)
			throw new NullableException(msg);
	}

	private boolean isNulled(String value) {
		if (value == null || value.trim().isEmpty()) {
			return true;
		}
		return false;
	}
}
