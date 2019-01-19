package com.poole.csv.exception;

public class OrderParserException extends CSVParserException {

	private static final long serialVersionUID = 1L;

	public OrderParserException(String message) {
		super(message);
	}

	public OrderParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
