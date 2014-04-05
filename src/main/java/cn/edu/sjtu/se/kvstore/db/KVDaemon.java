/**
 * @author Francis
 * @version 0.0.1
 *
 */
package cn.edu.sjtu.se.kvstore.db;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;
import java.io.IOException;
import java.io.FileInputStream;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.sjtu.se.kvstore.db.KVstore;
import cn.edu.sjtu.se.kvstore.identify.Identifying;
import cn.edu.sjtu.se.kvstore.utils.InputHandler;

/**
 * daemon class.
 */
public class KVDaemon implements Daemon {
	
	//create a timer to start the identifying thread.
	private static final Timer timer = new Timer();

	private static final Logger logger = LoggerFactory
			.getLogger(KVDaemon.class);

	/**
	 * create a fixed thread pool to handle all inputs. currently set to 2
	 * threads.
	 */
	private final int THREAD_NUMBER = 2;
	private final ExecutorService threadPool = Executors
			.newFixedThreadPool(THREAD_NUMBER);

	/**
	 * ServerSocket object to listen on port 8888
	 */
	private final int SOCKET_PORT = 8888;
	private ServerSocket listener;

	/**
	 * kvstore instance which handles all put and get operations
	 */
	private final KVstore store = new KVstore();

	public void init(DaemonContext context) throws DaemonInitException,
			Exception {
		logger.debug("Daemon init.");
	}

	public void start() throws Exception {
		logger.debug("Daemon startup.");

		// setup environment
		// envInit();

		try {
			listener = new ServerSocket(SOCKET_PORT);
		} catch (IOException ioe) {
			logger.debug("ServerSocket init error!");
		}
		
		//use timertask to set a timertask.
		//start to identifying data after 10mins;
		long delay = 1000*60;
		//identify data every 10mins;
		long period = 1000*60*2;
		timer.schedule(new Identifying(),delay,period);

		try {
			while (true) {
				threadPool.execute(new InputHandler(listener.accept(), store));
			}
		} catch (IOException ioe) {
			logger.debug("ExecutorService IO error!");
			threadPool.shutdown();
		} finally {
			threadPool.shutdown();
		}
	}

	public void stop() throws Exception {
		logger.debug("Deamon stop.");

		shutdownAndAwaitTermination();
		listener.close();
	}

	public void destroy() {
		logger.debug("Daemon destroy.");
	}

	/**
	 * The following method shuts down threadPool in two phases. First by
	 * calling shutdown to reject incoming tasks, and then calling shutdownNow,
	 * if neccessary, to cancel any lingering tasks.
	 */
	private void shutdownAndAwaitTermination() {
		// Disable new tasks from being submitted
		threadPool.shutdown();

		try {
			// Wait a while for existing tasks to terminate
			if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
				// cancel currently executing tasks
				threadPool.shutdownNow();
				// Wait a while for tasks to respond to being cancelled
				if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
					logger.error("Pool did not terminate.");
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			threadPool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}