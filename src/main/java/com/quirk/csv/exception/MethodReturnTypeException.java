package com.quirk.csv.exception;

public class MethodReturnTypeException extends CSVParserException {

	private static final long serialVersionUID = 1L;

	public MethodReturnTypeException(String message) {
		super(message);
	}

	public MethodReturnTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
