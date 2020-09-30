package com.poole.csv.processor;

/**
 * This is a Manager class that holds the references of CSVWriteBinding annotated
 * fields and methods. Validation further validation is handled elsewhere such
 * as CSVOrderProcessor and CSVNamedProcessor
 *
 */
@SuppressWarnings("rawtypes")
class CSVWriteAnnotationManager {
	private int order;
	private String header;
	private WriteHolder writeHolder;
	private Class parsedClazz;

	public CSVWriteAnnotationManager(int order, String header, WriteHolder writeHolder, Class parsedClazz) {
		super();
		this.order = order;
		this.header = header;
		this.writeHolder = writeHolder;
		this.parsedClazz = parsedClazz;
	}

	public int getOrder() {
		return order;
	}

	public String getHeader() {
		return header;
	}

	public WriteHolder getWriteHolder() {
		return writeHolder;
	}

	public Class getParsedClazz() {
		return parsedClazz;
	}

}
