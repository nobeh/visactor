package visactor;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class for visactor that can be used as the main class. It
 * provides two methods: {@link #start()} and {@link #stop()} in case
 * used otherwise.
 *
 * Starting visactor can take advantage of the following system
 * properties:
 * <ul>
 * <li>{@code host} is the host name on which visactor serves on.
 * <li>{@code port} is the port to which visactor binds.
 * <li>{@code visactor} that is a boolean value. If {@code true}, then
 * demo events are published in visactor every second on a network of 10
 * nodes.
 * </ul>
 * 
 * @since 1.0
 * @author Behrooz Nobakht
 */
public class Visactor {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String host;
	private final Integer port;
	private final URI uri;
	private final ResourceConfig configuration;
	private Server server;

	/**
	 * Ctor.
	 * 
	 * @param properties
	 *            the properties that include the start-up parameters.
	 * @throws UnknownHostException
	 *             thrown if the parameter for host specifies an invalid
	 *             host name for visactor.
	 */
	public Visactor(Properties properties) throws UnknownHostException {
		host = properties
				.getProperty("host", InetAddress.getLocalHost().getCanonicalHostName());
		port = Integer.valueOf(properties.getProperty("port", "8080"));
		uri = UriBuilder.fromUri("http://" + host).port(port).build();
		configuration = ResourceConfig.forApplication(new VisactorResourceConfig());
	}

	/**
	 * Starts visactor.
	 * 
	 * @throws Exception
	 *             if the server is already started or starting.
	 * @see Server#start()
	 */
	public synchronized void start() throws Exception {
		if (server != null && (server.isStarted() || server.isStarting())) {
			throw new IllegalStateException("Server is either starting or ready to use at: "
					+ uri);
		}
		if (server == null) {
			server = JettyHttpContainerFactory.createServer(uri, configuration, true);
		} else {
			server.start();
		}
		logger.info("visactor started on: {}", uri);
	}

	/**
	 * Stops visactor.
	 * 
	 * @throws Exception
	 *             if the server is already stopped or not available.
	 * @see Server#stop()
	 */
	public synchronized void stop() throws Exception {
		if (server == null || server.isStopping() || server.isStopped()) {
			throw new IllegalStateException("Server already stopped at: " + uri);
		}
		server.stop();
		logger.info("visactor stopped on: {}", uri);
	}

	public static void main(String[] args) throws Exception {
		new Visactor(new Properties(System.getProperties())).start();
	}

}
