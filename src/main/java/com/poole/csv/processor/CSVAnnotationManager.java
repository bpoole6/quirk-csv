package com.poole.csv.processor;

import com.poole.csv.wrappers.Wrapper;

/**
 * This is a Manager class that holds the references of CSVColumn annotated
 * fields and methods. Validation further validation is handled elsewhere such
 * as CSVOrderProcessor and CSVNamedProcessor
 *
 */
@SuppressWarnings("rawtypes")
public class CSVAnnotationManager {
	private int order;
	private String header;
	private boolean isNullable;
	private Holder holder;
	private Class parsedClazz;
	private Class<Wrapper> wrapper;

	public CSVAnnotationManager(int order, String header, boolean isNullable, Holder holder, Class parsedClazz,
			Class<Wrapper> wrapper) {
		super();
		this.order = order;
		this.header = header;
		this.isNullable = isNullable;
		this.holder = holder;
		this.parsedClazz = parsedClazz;
		this.wrapper = wrapper;
	}

	public int getOrder() {
		return order;
	}

	public String getHeader() {
		return header;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public Holder getHolder() {
		return holder;
	}

	public Class getParsedClazz() {
		return parsedClazz;
	}

	public Class<Wrapper> getWrapper() {
		return wrapper;
	}

}
