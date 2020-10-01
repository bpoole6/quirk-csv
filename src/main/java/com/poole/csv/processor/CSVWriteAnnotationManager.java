package com.poole.csv.processor;

import com.poole.csv.exception.MissingWrapperException;
import com.poole.csv.wrappers.write.WriteWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

/**
 * This is a Manager class that holds the references of CSVWriteColumn annotated
 * fields and methods. Validation further validation is handled elsewhere such
 * as {@link CSVOrderWriteProcessor} and {@link CSVNamedWriteProcessor}
 *
 */
@SuppressWarnings("rawtypes")
class CSVWriteAnnotationManager {

	private int order;
	private String header;
	private Field field;
	private Method method;
	private WriteWrapper writeWrapper;

	@SuppressWarnings("rawtypes")
	private Class type;

	public CSVWriteAnnotationManager(int order, String header,Field field, WriteWrapper writeWrapper) {
		super();
		this.order = order;
		this.header = header;
		this.field = field;
		this.writeWrapper = writeWrapper;
		this.type = field.getType();
	}
	public CSVWriteAnnotationManager(int order, String header, Method method, WriteWrapper writeWrapper) {
		super();
		this.order = order;
		this.header = header;
		this.writeWrapper = writeWrapper;
		this.method = method;
	}

	public int getOrder() {
		return order;
	}

	public String getHeader() {
		return header;
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
			getValue =(value) -> value!=null?value.toString():"";
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
