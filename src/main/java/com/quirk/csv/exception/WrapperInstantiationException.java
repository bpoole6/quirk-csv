package com.quirk.csv.exception;

public class WrapperInstantiationException extends CSVParserException {

	private static final long serialVersionUID = 1L;

	public WrapperInstantiationException(String message) {
		super(message);
	}

	public WrapperInstantiationException(String message, Throwable cause) {
		super(message, cause);
	}

}
