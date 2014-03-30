/**
 * @author Francis
 * @version 0.0.1
 *
 */
package cn.edu.sjtu.se.kvstore.db;

import java.io.IOException;
import java.io.FileInputStream;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class KVstore {

  public static void main(String[] args) {
    KVstore kv = new KVstore();
  }

  /**
   * constructor
   */
  public KVstore() {
    init();
  }

  /**
   * environment setup
   */
  private void init() {
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
}
