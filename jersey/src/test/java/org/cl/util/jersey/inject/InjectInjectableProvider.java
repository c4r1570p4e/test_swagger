package org.cl.util.jersey.inject;

import java.lang.reflect.Type;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Provider
public class InjectInjectableProvider implements InjectableProvider<Inject, Type> {

	public InjectInjectableProvider() {
		super();
	}

	@Override
	public ComponentScope getScope() {
		return ComponentScope.Singleton;
	}

	@Override
	public Injectable<Object> getInjectable(ComponentContext ic, Inject a, Type t) {

		if (!(t instanceof Class))
			return null;

		@SuppressWarnings("unchecked")
		Class<Object> clazz = ((Class<Object>) t);

		Set<Object> injectables = InjectableProviderContext.getInjectableObjects();

		for (Object injectable : injectables) {
			if (clazz.isAssignableFrom(injectable.getClass())) {
				return new InjectableObject(injectable);
			}
		}

		return null;
	}

	private static class InjectableObject implements Injectable<Object> {

		private Object object;

		public InjectableObject(Object o) {
			this.object = o;
		}

		@Override
		public Object getValue() {
			return this.object;
		}

	}

}
