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
import java.util.Properties;

import java.io.IOException;
import java.io.FileInputStream;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.sjtu.se.kvstore.db.KVstore;
import cn.edu.sjtu.se.kvstore.utils.InputHandler;
/**
 * daemon class.
 */
public class KVDaemon implements Daemon {

  private static final Logger logger = LoggerFactory.getLogger(KVDaemon.class);

  /** 
   * create a fixed thread pool to handle all inputs.
   * currently set to 2 threads.
   */
  private final int THREAD_NUMBER = 2;
  private final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_NUMBER);

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
    logger.debug("Deamon stop");

    shutdownAndAwaitTermination();
    listener.close();
  }

  public void destroy() {
    logger.debug("Daemon destroy.");
  }

  /**
   * The following method shuts down threadPool in two phases.
   * First by calling shutdown to reject incoming tasks,
   * and then calling shutdownNow, if neccessary, to cancel any lingering tasks.
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

  /**
   * environment setup
   */
  /*
  private void envInit() {
    initLog4j();
    initSpring();
  }

  private void initLog4j() {
    String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    String baseDir = jarPath.substring(0, jarPath.lastIndexOf("lib"));
    String configPath = baseDir + "conf/log4j.properties";

    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream(configPath));
      prop.setProperty("log4j.appender.FILE.File", baseDir + prop.getProperty("log4j.appender.FILE.File"));
      PropertyConfigurator.configure(prop);
      System.out.println("Logging into " + baseDir + "logs/kvstore.log");
    } catch (IOException ioe) {
      System.out.println("log4j init failed.");
      ioe.printStackTrace();
    }
  }

  private void initSpring() {

  }
  */
}