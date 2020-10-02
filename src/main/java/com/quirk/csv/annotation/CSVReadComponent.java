package com.quirk.csv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to specify how a class should be processed. By either column headers or the order they are found in the file
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface CSVReadComponent {

	/**
	 * Used to determine how to process the writing and reading for csv files
	 */
	CSVType type();

	/**
	 * If set to true then the cvs-parser will check the parent class for a {@link CSVReadComponent}.
	 * If found then it will process the {@link CSVReadBinding}
	 *
	 * @return boolean
	 */
	boolean inheritSuper() default false;
}
