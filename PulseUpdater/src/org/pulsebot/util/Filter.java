package org.pulsebot.util;

/**
 * Filter is a basic interface which allows dynamic qualifying of objects.
 * @param <E> the type of object to provide a filter for.
 */
public interface Filter<E> {
	/**
	 * Determines if the given object should qualify.
	 * @param obj - the object to test qualification.
	 * @return <tt>true</tt> if the object qualified, otherwise <tt>false</tt>.
	 */
	public boolean accept(E obj);
}