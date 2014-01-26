package org.cl.util.weld.test;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldJUnit4Runner extends BlockJUnit4ClassRunner {

	@SuppressWarnings("rawtypes")
	private final Class klass;
	private final Weld weld;
	private final WeldContainer container;

	public WeldJUnit4Runner(@SuppressWarnings("rawtypes") final Class klass) throws InitializationError {
		super(klass);

		this.klass = klass;
		this.weld = new Weld();
		this.container = weld.initialize();
	}

	@Override
	protected Object createTest() throws Exception {
		@SuppressWarnings("unchecked")
		final Object test = container.instance().select(klass).get();

		return test;
	}
}
