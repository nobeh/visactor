package visactor;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import reactor.core.Reactor;

/**
 * The entry resource endpoint of visactor. This resource provides the
 * following sub-resources:
 * <ul>
 * <li> {@code index.html} is the index HTML file to view the
 * visualization.
 * <li> {@code /events} is the resource to take in events and publish
 * them to the visualization client.
 * </ul>
 * 
 * @see EventResource
 * @since 1.0
 * @author Behrooz Nobakht
 */
@Provider
@Path("")
public class VisactorResource {

	private final EventResource eventResource;

	/**
	 * Ctor.
	 * 
	 * @param reactor
	 *            the instance of {@link Reactor} to use.
	 * @param store
	 *            the event store instance.
	 */
	public VisactorResource(Reactor reactor, EventStore store) {
		this.eventResource = new EventResource(reactor, store);
	}

	/**
	 * The main index HTML resource.
	 * 
	 * @return JAX-RS response to represent {@code index.html}
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("index.html")
	public Response getIndex() {
		InputStream in = getClass().getClassLoader().getResourceAsStream("index.html");
		return Response.ok(in).build();
	}

	/**
	 * Delegates to {@link EventResource}.
	 * 
	 * @return an instance of {@link EventResource}
	 */
	@Path("events")
	public EventResource getEvents() {
		return eventResource;
	}

}
