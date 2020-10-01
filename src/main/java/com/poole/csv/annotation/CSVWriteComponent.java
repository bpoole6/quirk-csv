package com.poole.csv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to specify how a class should be processed when writing the csv. By either column headers or the order they are found in the file
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface CSVWriteComponent {


	/**
	 * Used to determine how to process the writing and reading for csv files
	 */
	CSVType type();


	/**
	 * If {@link CSVType#NAMED} is then setting this to true will order the columns based on the order specified by {@link CSVWriteBinding#order()}
	 */
	boolean namedIsOrdered() default false;

	/**
	 * If set to true then the cvs-parser will check the parent class for a {@link CSVWriteComponent}.
	 * If found then it will process the {@link CSVWriteBinding}
	 *
	 * @return boolean
	 */
	boolean inheritSuper() default false;
}
