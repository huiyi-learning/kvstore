/**
 * @author Francis
 * @version 0.0.1
 *
 */
package cn.edu.sjtu.se.kvstore.db;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

import org.apache.log4j.Logger;
/**
 * daemon class.
 */
public class KVDaemon implements Daemon {

  private static final Logger logger = Logger.getLogger(KVDaemon.class);

  public void init(DaemonContext context) throws DaemonInitException,
      Exception {
    logger.debug("Daemon initialized with arguments {}.");
  }

  public void start() throws Exception {
    logger.info("Starting up daemon.");
  }

  public void stop() throws Exception {
    logger.info("Stopping daemon.");
  }

  public void destroy() {
    logger.info("Destroying daemon.");
  }
}