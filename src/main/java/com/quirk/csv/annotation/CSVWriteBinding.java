package com.quirk.csv.annotation;

import com.quirk.csv.wrappers.write.WriteWrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate Methods and fields to denote they should write values
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface CSVWriteBinding {
	/**
	 * Used for processing paired with
	 * {@link com.quirk.csv.annotation.CSVWriteComponent} and has type set to @see
	 * com.poole.csv.annotation.CSVReaderType.NAMED
	 *
	 * @return the value in the csv where of header name is this value.
	 */
	String header() default "";

	/**
	 * Used for processing paired with
	 * {@link com.quirk.csv.annotation.CSVWriteComponent} and has type set to
	 * {@link com.quirk.csv.annotation.CSVType#ORDER}
	 *
	 * @return The value on the row of this position.
	 */
	int order() default 0;

	/**
	 * You can specify an inline wrapper class for processing. Make sure the
	 * wrapper class has an no-agrs constructor if it's used here. Be warned
	 * that this wrapper class will take precedence over global/default WriteWrapper
	 * classes.
	 *
	 * {@link WriteWrapper}
	 * @return WriteWrapper class used for handling the datatype its annotated to
	 */
	Class<? extends WriteWrapper> wrapper() default WriteWrapper.class;

}
