package com.poole.csv.exception;

public class NullableException extends CSVParserException {

	private static final long serialVersionUID = 1L;

	public NullableException(String message) {
		super(message);
	}

	public NullableException(String message, Throwable cause) {
		super(message, cause);
	}
}
