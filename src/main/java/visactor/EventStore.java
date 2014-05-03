package visactor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An interface to abstract the operations that are expected on a
 * collection of events.
 * 
 * @author Behrooz Nobakht
 * @since 1.0
 */
public interface EventStore {

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

	/**
	 * Store a new event.
	 * 
	 * @param event
	 *            the new event.
	 */
	void add(Event event);

	/**
	 * Sorts the the events with the provided comparator.
	 * 
	 * @param events
	 *            the events to be sorted
	 * @return the sorted events
	 */
	default List<Event> sort(List<Event> events, Comparator<Event> cmp) {
		Collections.sort(events, cmp);
		return events;
	}

}