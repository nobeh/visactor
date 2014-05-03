package visactor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A simple base for visactor event abstraction that is presented by
 * (id, timestamp, source, target).
 * 
 * @author Behrooz Nobakht
 * @since 1.0
 */
@XmlRootElement
public class Event implements Serializable {

	private static final long serialVersionUID = -6969088901063773872L;

	private String id = UUID.randomUUID().toString();
	private long timestamp = Instant.now().toEpochMilli();
	private String source;
	private String target;

	/**
	 * Ctor.
	 */
	public Event() {
	}

	/**
	 * @return the ID of the event
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the timestamp of the event
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the source of the event
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return the target of the event
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param id
	 *            the new ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param timestamp
	 *            the new timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param source
	 *            the new source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @param target
	 *            the new target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Event == false) {
			return false;
		}
		return hashCode() == obj.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "(id=" + getId() + ", time=" + getTimestamp() + ", source=" + getSource()
				+ ", target=" + getTarget() + ")";
	}

}
