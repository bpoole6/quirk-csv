package com.poole.csv.processor;

import com.poole.csv.exception.MissingWrapperException;
import com.poole.csv.wrappers.write.WriteWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

/**
 * Holder has the logic to determine which wrapper to use when processing
 * datatypes on specific fields and/or methods
 */
public class WriteHolder {
	private Field field;
	private Method method;
	private WriteWrapper writeWrapper;

	@SuppressWarnings("rawtypes")
	private Class type;

	public WriteHolder(Field field, WriteWrapper writeWrapper) {
		this.field = field;
		this.writeWrapper = writeWrapper;
		this.type = field.getType();

	}

	public WriteHolder(Method method, WriteWrapper writeWrapper) {
		this.method = method;
		this.writeWrapper = writeWrapper;
		this.type = method.getParameterTypes()[0];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getValue(Object obj, Map<Class, WriteWrapper> getValueMap)
			throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
		Function<Object,String> getValue = null;

		if (this.writeWrapper != null) {
			getValue = (value) -> this.writeWrapper.apply(value);
		} else if (getValueMap.get(type) != null) {
			getValue = (value) -> getValueMap.get(type).apply(value);
		}  else {
			throw new MissingWrapperException("Do not have a write wrapper class to handle Type" + type);
		}

		if (field != null) {
			field.setAccessible(true);
			Object val = field.get(obj);
			return getValue.apply(val);
		} else if (method != null) {
			method.setAccessible(true);
			Object val = method.invoke(obj);
			return getValue.apply(val);
		}
		return "";
	}

}
