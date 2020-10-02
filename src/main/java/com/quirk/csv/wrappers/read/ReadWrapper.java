package com.quirk.csv.wrappers.read;

/**
 * Interface used to handle datatype transformations. You can find how this is
 * being used by looking at com.poole.csv.wrappers.read.defaults.DefaultWrappers to
 * see how the primitives are being handled.
 *
 *
 */
public interface ReadWrapper<T> {
	/**
	 * Be sure to handle all exceptions. An java.lang.IllegalArgumentException
	 * can be thrown that will allow the process to continue.
	 *
	 * @param str
	 * @return an object after transforming
	 */
	T apply(String str);
}
