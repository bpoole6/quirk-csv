package com.poole.csv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.poole.csv.wrappers.Wrapper;

/**
 * Used to annotate Methods and fields to denote they should receive values
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface CSVColumn {
	/**
	 * Used for processing paired with @see
	 * com.poole.csv.annotation.CSVComponent and has type set to @see
	 * com.poole.csv.annotation.CSVReaderType.NAMED
	 *
	 * @return the value in the csv where of header name is this value.
	 */
	String header() default "";

	/**
	 * Used for processing paired with @see
	 * com.poole.csv.annotation.CSVComponent and has type set to @see
	 * com.poole.csv.annotation.CSVReaderType.ORDER
	 *
	 * @return The value on the row of this position.
	 */
	int order() default 0;

	/**
	 *
	 * @return true if the value record of the row can be null and false when it
	 *         cannot be null
	 */
	boolean isNullable() default true;

	/**
	 * You can specify an inline wrapper class for processing. Make sure the
	 * wrapper class has an no-agrs constructor if it's used here. Be warned
	 * that this wrapper class will take precedence over global/default wrapper
	 * classes.
	 *
	 * @see com.poole.csv.wrappers.Wrapper
	 * @return Wrapper class used for handling the datatype its annotated to
	 */
	Class<? extends Wrapper> wrapper() default Wrapper.class;

}
