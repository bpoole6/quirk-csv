package com.quirk.csv.exception;

public class MethodParameterException extends CSVParserException {

	private static final long serialVersionUID = 1L;

	public MethodParameterException(String message) {
		super(message);
	}

	public MethodParameterException(String message, Throwable cause) {
		super(message, cause);
	}

}
