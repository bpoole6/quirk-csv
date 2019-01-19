package com.poole.csv.exception;

public class CSVParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CSVParserException(String message) {
		super(message);
	}

	public CSVParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
