package com.quirk.csv.exception;

public class MissingCSVComponent extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingCSVComponent(String message) {
		super(message);
	}

	public MissingCSVComponent(String message, Throwable cause) {
		super(message, cause);
	}
}
