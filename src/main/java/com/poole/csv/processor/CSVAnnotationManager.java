package com.poole.csv.processor;

/**
 * This is a Manager class that holds the references of CSVReadBinding annotated
 * fields and methods. Validation further validation is handled elsewhere such
 * as CSVOrderProcessor and CSVNamedProcessor
 *
 */
@SuppressWarnings("rawtypes")
class CSVAnnotationManager {
	private int order;
	private String header;
	private ReadHolder readHolder;
	private Class parsedClazz;

	public CSVAnnotationManager(int order, String header, ReadHolder readHolder, Class parsedClazz) {
		super();
		this.order = order;
		this.header = header;
		this.readHolder = readHolder;
		this.parsedClazz = parsedClazz;
	}

	public int getOrder() {
		return order;
	}

	public String getHeader() {
		return header;
	}

	public ReadHolder getReadHolder() {
		return readHolder;
	}

	public Class getParsedClazz() {
		return parsedClazz;
	}

}
