package com.quirk.csv.exception;

public class NamedParserException extends CSVParserException {

	private static final long serialVersionUID = 1L;

	public NamedParserException(String message) {
		super(message);
	}

	public NamedParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
