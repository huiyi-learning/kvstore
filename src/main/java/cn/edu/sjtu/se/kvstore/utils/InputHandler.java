/**
 * @author Francis
 * @version 0.0.1
 *
 */
package cn.edu.sjtu.se.kvstore.utils;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.sjtu.se.kvstore.db.KVstore;

public class InputHandler implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(InputHandler.class);

  private Socket socket;
  private BufferedReader in;
  private BufferedWriter out;

  private final KVstore store;

  public InputHandler(Socket socket, final KVstore store) {
    this.socket = socket;
    this.store = store;
  }

  // pass outputstream to KVStore
  public void run() {
    try {

      // Read data from client
      in = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));

      out = new BufferedWriter(
          new OutputStreamWriter(socket.getOutputStream()));

      while (true) {
        String line = in.readLine();
        if (line == null) {
          break;
        }

        // process client data & respond to client
        String[] params = line.split(" ");
        if (params[0].equals("get")) {
          String key = params[1];
          String value = store.get(key);
          out.write(value);
          out.newLine();
          out.flush();
        } else if (params[0].equals("put")) {
          String key = params[1];
          String value = params[2];
          store.put(key, value);
        } else {
          logger.debug("operations not supported with line: " + line);
        }
      }

      in.close();
      out.close();
    } catch (Exception ex) {
      logger.debug("server running error: " + ex.getMessage());
    } finally {
      // close BufferedReader
      if (in != null) {
        try {
          in.close();
        } catch (IOException ioein) {
          in = null;
          logger.debug("server couldn't close BufferedReader: " + ioein.getMessage());
        }
      }

      // close BufferedWriter
      if (out != null) {
        try {
          out.close();
        } catch (Exception ioeout) {
          out = null;
          logger.debug("server couldn't close BufferedWriter: " + ioeout.getMessage());
        }
      }

      // close socket
      if (socket != null) {  
        try {  
          socket.close();  
        } catch (Exception e) {  
          socket = null;  
          logger.debug("server couldn't close socket:" + e.getMessage());  
        }  
      }
    }
  }
}
