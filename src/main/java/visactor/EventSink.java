package visactor;

/**
 * A sink to use {@link Event}s.
 * 
 * @author Behrooz Nobakht
 * @since 1.0
 */
public interface EventSink {

	/**
	 * Store a new event.
	 * 
	 * @param event
	 *            the new event.
	 */
	void use(Event event);

}
