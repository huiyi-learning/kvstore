/**
 * @author Francis
 * @version 0.0.1
 *
 */
package cn.edu.sjtu.se.kvstore.db;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
	
	String value = data.get(key);  
	 
	if(value != null) {
	    long current = System.currentTimeMillis()/1000;
	    logger.info(current + " " + key);
	}
	  
    return value;
  }

  public void put(String key, String value) {
    data.put(key, value);
  }
  
  public void moveHot2Cold(Set<String> keys) {
    data.moveHot2Cold(keys);
  }

  public void moveCold2Hot(Map<String, String> items) {
    data.moveCold2Hot(items);
  }

  public ConcurrentHashMap<String, String> getHot() {
    return data.getHot();
  }

  public Set<String> getCold() {
    return data.getCold();
  }

  public Set<String> getRm() {
    return data.getRm();
  }
  
  public void clearRm() {
	  data.clearRm();
  }
  
}
