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
public interface EventStore extends EventSource, EventSink {

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