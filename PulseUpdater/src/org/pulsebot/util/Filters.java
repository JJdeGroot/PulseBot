package org.pulsebot.util;

import java.util.Arrays;
import java.util.LinkedList;

public class Filters {
	
	@SafeVarargs
	public static <E> Filter<E> add(final Filter<E> filter, final Filter<E>... filters) {
		if (filters != null && filters.length > 0) {
			return new Filter<E>() {
				public boolean accept(E obj) {
					for (Filter<E> f : filters) {
						if (f != null && f.accept(obj)) {
							return filter == null || filter.accept(obj);
						}
					}
					return false;
				}
			};
		}
		return filter;
	}
	
}
