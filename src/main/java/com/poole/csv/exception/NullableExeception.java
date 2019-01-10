package com.poole.csv.exception;

public class NullableExeception extends RuntimeException {

	
	private static final long serialVersionUID = 1L;


	public NullableExeception() {
	        super();
	    }

	   
	    public NullableExeception(String message) {
	        super(message);
	    }

	    
	    public NullableExeception(String message, Throwable cause) {
	        super(message, cause);
	    }

	   
	    public NullableExeception(Throwable cause) {
	        super(cause);
	    }

	   
	    protected NullableExeception(String message, Throwable cause,
	                               boolean enableSuppression,
	                               boolean writableStackTrace) {
	        super(message, cause, enableSuppression, writableStackTrace);
	    }

}
