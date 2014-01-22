package org.cl.util.jersey.inject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InjectableProviderContext extends InjectInjectableProvider {

	private static Set<Object> injectableObjects = new HashSet<Object>();

	public static void init(Object... injectables) {
		reset();
		put(injectables);
	}

	public static void put(Object... injectables) {
		for (Object injectable : injectables) {
			injectableObjects.add(injectable);
		}
	}

	public static <T>  T getInjectableObjectForClass(Class<T> clazz) {
		for (Object injectable : injectableObjects) {
			if (clazz.isAssignableFrom(injectable.getClass())) {
				return (T)injectable;
			}
		}
		return null;
	}

	public static Set<Object> getInjectableObjects() {
		return Collections.unmodifiableSet(injectableObjects);
	}

	public static void reset() {
		injectableObjects.clear();
	}

}
