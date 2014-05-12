package visactor;

import java.util.List;

/**
 * A source of {@link Event}s.
 * 
 * @author Behrooz Nobakht
 * @since 1.0
 */
public interface EventSource {

	/**
	 * Accesses to all the events in the store.
	 * 
	 * @return the list of all events.
	 */
	List<Event> events();

	/**
	 * Filters the events from a specific point of time.
	 * 
	 * @param since
	 *            the origin of time that should be Epoch milli seconds
	 * @return the events that happened after {@code since}
	 */
	List<Event> events(long since);

}
