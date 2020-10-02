package com.quirk.csv.test;

import com.quirk.csv.exception.*;
import org.junit.Test;

public class ExceptionTest {

	@Test(expected = NamedParserException.class)
	public void namedParserException() {
		throw new NamedParserException("Test", new Exception());
	}

	@Test(expected = OrderParserException.class)
	public void orderParserException() {
		throw new OrderParserException("Test", new Exception());
	}

	@Test(expected = NullableException.class)
	public void nullableException() {
		throw new NullableException("Test", new Exception());
	}

	@Test(expected = WrapperInstantiationException.class)
	public void wrapperInstantiationException() {
		throw new WrapperInstantiationException("Test", new Exception());
	}

	@Test(expected = MissingCSVComponent.class)
	public void missingCSVComponent() {
		throw new MissingCSVComponent("Test", new Exception());
	}

	@Test(expected = MethodParameterException.class)
	public void methodParameterException() {
		throw new MethodParameterException("Test", new Exception());
	}
	@Test(expected = MissingWrapperException.class)
	public void missingWrapperException() {
		throw new MissingWrapperException("Test", new Exception());
	}

	@Test(expected = UninstantiableException.class)
	public void uninstantiableException() {
		throw new UninstantiableException("Test");
	}
}
