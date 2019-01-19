package com.poole.csv.exception;

public class MissingWrapperException extends CSVParserException {

	private static final long serialVersionUID = 1L;

	public MissingWrapperException(String message) {
		super(message);
	}

	public MissingWrapperException(String message, Throwable cause) {
		super(message, cause);
	}

}
