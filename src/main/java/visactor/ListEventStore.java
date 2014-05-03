package visactor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An in-memory implementation of {@link EventStore} using an
 * instance {@link ConcurrentSkipListSet}.
 * 
 * @see NavigableSet
 * @see ConcurrentSkipListSet
 * 
 * @author Behrooz Nobakht
 * @since 1.0
 */
public class ListEventStore implements EventStore {

	public static final Comparator<Event> COMPARATOR = (e1, e2) -> e1.getId()
			.compareTo(e2.getId());
	public static final Comparator<Event> TIME_COMPARATOR = (e1, e2) -> Long.valueOf(
			e1.getTimestamp() - e2.getTimestamp()).intValue();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final NavigableSet<Event> events = new ConcurrentSkipListSet<>(COMPARATOR);

	/** {@inheritDoc} */
	@Override
	public List<Event> events() {
		return sort(new ArrayList<>(events), TIME_COMPARATOR);
	}

	/** {@inheritDoc} */
	@Override
	public List<Event> events(final long since) {
		if (since <= 0) {
			return events();
		}
		List<Event> result = events.parallelStream()
				.filter(m -> m.getTimestamp() >= since).collect(Collectors.toList());
		return sort(result, TIME_COMPARATOR);
	}

	/** {@inheritDoc} */
	@Override
	public void add(Event event) {
		try {
			events.add(event);
			logger.info("Added event: {}", event);
		} catch (Exception e) {
			logger.error("Cannot add event {}: {}", event, e);
		}
	}

}
