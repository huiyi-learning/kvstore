/**
 * @author Francis
 * @version 0.0.1
 *
 */
package cn.edu.sjtu.se.kvstore.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.sjtu.se.kvstore.common.Data;

public class KVstore {

  private static final Logger logger = LoggerFactory.getLogger(KVstore.class);

  private Data<String, String> data;
  /*
  private HotCluster hot;
  private ColdCluster cold;
  */
  public KVstore() {
    data = new Data<String, String>();
  }

  public String get(String key) {
    return data.get(key);
  }

  public String put(String key, String value) {
    return data.put(key, value);
  }
}
