package visactor;

import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;

/**
 * The JAX-RS {@link ResourceConfig} for visactor. It also enables
 * Jersey {@link SseFeature} and uses {@link VisactorResource} as the
 * resource instance.
 * 
 * @since 1.0
 * @see VisactorResource
 * @author Behrooz Nobakht
 */
public class VisactorResourceConfig extends ResourceConfig {

	private final Reactor reactor;
	private final EventStore store;

	public VisactorResourceConfig() {
		this.reactor = Reactors.reactor(new Environment(), Environment.RING_BUFFER);
		this.store = new ListEventStore();

		register(SseFeature.class);
		registerInstances(new VisactorResource(reactor, store));
	}

}
