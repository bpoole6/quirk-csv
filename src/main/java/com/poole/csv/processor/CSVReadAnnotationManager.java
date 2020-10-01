package com.poole.csv.processor;

import com.poole.csv.exception.MissingWrapperException;
import com.poole.csv.exception.NullableException;
import com.poole.csv.wrappers.read.ReadWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This is a Manager class that holds the references of CSVReadBinding annotated
 * fields and methods. Validation further validation is handled elsewhere such
 * as {@link CSVOrderReadProcessor} and {@link CSVNamedReadProcessor}
 *
 */
@SuppressWarnings("rawtypes")
class CSVReadAnnotationManager {
	private int order;
	private String header;

	private Field field;
	private Method method;
	private ReadWrapper readWrapper;
	private boolean isNullable;
	@SuppressWarnings("rawtypes")
	private Class type;
	private boolean isEnum = false;

	public CSVReadAnnotationManager(int order, String header, Field field, boolean isNullable, ReadWrapper readWrapper) {
		super();
		this.order = order;
		this.header = header;
		this.field = field;
		this.isNullable = isNullable;
		this.readWrapper = readWrapper;
		this.type = field.getType();
		if (this.type.isEnum())
			isEnum = true;
	}
	public CSVReadAnnotationManager(int order, String header,Method method, boolean isNullable, ReadWrapper readWrapper) {
		super();
		this.order = order;
		this.header = header;
		this.method = method;
		this.isNullable = isNullable;
		this.readWrapper = readWrapper;
		this.type = method.getParameterTypes()[0];
		if (this.type.isEnum())
			isEnum = true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setValue(Object obj, String value, Map<Class, ReadWrapper> setValueMap)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Supplier<Object> setValue = null;

		if (this.readWrapper != null) {
			setValue = () -> {
				return this.readWrapper.apply(value);
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

	public int getOrder() {
		return order;
	}

	public String getHeader() {
		return header;
	}


}
