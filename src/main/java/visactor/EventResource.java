package visactor;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.Reactor;
import reactor.event.selector.Selectors;

/**
 * The JAX-RS endpoint for events which are in two cases:
 * <ul>
 * <li> {@code /events/in} takes in new events, stores them and publishes
 * them
 * <li> {@code /events/out} is a {@link SseFeature#SERVER_SENT_EVENTS}
 * using server-sent events to publish new events to client.
 * </ul>
 *
 * @see SseFeature
 * @see SseBroadcaster
 * @since 1.0
 * @author Behrooz Nobakht
 */
@Provider
public class EventResource {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final EventStore store;
	private final Reactor reactors;
	private final SseBroadcaster broadcaster;
	private final AtomicLong lastEventId;

	/**
	 * Ctor.
	 * 
	 * @param reactor
	 *            the instance of reactor to use
	 * @param store
	 *            the instance of event store to use
	 */
	public EventResource(Reactor reactor, EventStore store) {
		this.broadcaster = new SseBroadcaster();
		this.store = store;
		this.reactors = reactor;
		this.lastEventId = new AtomicLong(-1);

		this.reactors.on(Selectors.object("out"), (reactor.event.Event<Long> lastEventId) -> {
			broadcast(lastEventId.getData().longValue());
		});
		this.reactors.on(Selectors.object("in"), (reactor.event.Event<Event> e) -> {
			store.use(e.getData());
			broadcast(this.lastEventId.longValue());
		});

		if (Boolean.getBoolean("visactor.demo")) {
			logger.warn("visactor demo mode is enabled.");
			startDemoEvents();
		}
	}

	/**
	 * Retrieves the events since the last event ID that is possibly in
	 * the request headers. After the first call, the last event ID is
	 * internally stored in this resource to keep track.
	 * 
	 * @see SseFeature#LAST_EVENT_ID_HEADER
	 * 
	 * @param lastEventId
	 *            the last event ID as specified in
	 *            {@link SseFeature#LAST_EVENT_ID_HEADER}
	 * @return an instance of {@link EventOutput} that asynchronously
	 *         streams chunked events to the subscribed clients.
	 */
	@GET
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	@Path("source")
	public EventOutput getEvents(
			@HeaderParam(SseFeature.LAST_EVENT_ID_HEADER) @DefaultValue("-1") String lastEventId) {
		final EventOutput output = new EventOutput();
		this.reactors.notify("out", reactor.event.Event.wrap(Long.valueOf(lastEventId)));
		this.broadcaster.add(output);
		return output;
	}

	/**
	 * Takes in a new event and publishes it to the clients.
	 * 
	 * @param e
	 *            the instance of event input as a JSON object.
	 * @return a JAX-RS response with {@link Status#CREATED}
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("sink")
	public Response putEvent(Event e) {
		this.reactors.notify("in", reactor.event.Event.wrap(e));
		return Response.status(Status.CREATED).entity(e.getId()).build();
	}

	/**
	 * Publishes the events since the last event ID.
	 * 
	 * @param lastEventId
	 *            the ID of the event that last was published.
	 * @return the new last event ID.
	 */
	protected long broadcast(long lastEventId) {
		OutboundEvent.Builder oeb = new OutboundEvent.Builder();
		List<Event> events = store.events(lastEventId);
		oeb.name("event");
		long newLastEventId = events.get(events.size() - 1).getTimestamp();
		oeb.id(Long.toString(newLastEventId));
		oeb.reconnectDelay(1000);
		oeb.mediaType(MediaType.APPLICATION_JSON_TYPE);
		oeb.data(Event.class, events);
		try {
			OutboundEvent event = oeb.build();
			broadcaster.broadcast(event);
			logger.info("Flushed SSE event: {}", event);
			return this.lastEventId.getAndSet(newLastEventId);
		} catch (Exception x) {
			logger.error("Failed flushing SSE event: {}", x);
			return this.lastEventId.get();
		}
	}

	/**
	 * Start publishing demo events asynchronously. The ctor
	 * {@link #EventResource(Reactor, MessageEventStore)} uses system
	 * property {@code visactor.demo} to check if this mode is enabled
	 * and calls this method.
	 */
	protected void startDemoEvents() {
		final Random r = new Random(System.currentTimeMillis());
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		Runnable demoEvents = () -> {
			Event e = new Event();
			e.setSource("node-" + r.nextInt(10));
			e.setTarget("node-" + r.nextInt(10));
			putEvent(e);
		};
		ses.scheduleWithFixedDelay(demoEvents, 0, 2000, TimeUnit.MILLISECONDS);
	}
}
