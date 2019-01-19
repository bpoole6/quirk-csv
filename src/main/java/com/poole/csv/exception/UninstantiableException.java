package com.poole.csv.exception;

public class UninstantiableException extends CSVParserException {

	private static final long serialVersionUID = 1L;

	public UninstantiableException(String message) {
		super(message);
	}

	public UninstantiableException(String message, Throwable cause) {
		super(message, cause);
	}

}
