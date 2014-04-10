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
/*
  private HotCluster hot;
  private ColdCluster cold;
*/
  
  private Data datas;
  
  public KVstore() {
    //hot = new HotCluster();
	  
	  datas = new Data();
  }

  public String get(String key) {
    //return hot.get(key);
	  
	  return datas.get(key);
  }

  public void put(String key, String value) {
    //hot.put(key, value);
	  
	  datas.put(key, value);
  }
}
