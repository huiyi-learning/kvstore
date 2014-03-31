/**
 * @author Francis
 * @version 0.0.1
 *
 */
package cn.edu.sjtu.se.kvstore.utils;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.sjtu.se.kvstore.db.KVstore;

public class InputHandler implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(InputHandler.class);

  private final Socket socket;

  private final KVstore store;

  public InputHandler(Socket socket, final KVstore store) {
    this.socket = socket;
    this.store = store;
  }

  // pass outputstream to KVStore
  public void run() {
    try {
      BufferedReader in = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));

      // Get message from the client, line by line
      // and then convert them to <key,value> pairs
      while (true) {
        String line = in.readLine();
        if (line == null) {
          break;
        }

        store.process(line);
      }
    } catch (IOException ioe) {
      logger.info("Socket Read Error");
    } finally {
      try {
        socket.close();
      } catch (IOException fioe) {
        logger.info("couldn't close this socket.");
      }
    }
  }
}
