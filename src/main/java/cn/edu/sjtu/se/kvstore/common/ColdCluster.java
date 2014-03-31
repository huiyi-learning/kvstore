/**
 * @author Francis
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

import java.util.Hashtable;

/**
 * data structure used to store cold data.
 */
public class ColdCluster {
  
  private Hashtable<String, String> data;

  public void setData() {
    this.data = data;
  }

  public Hashtable<String, String> getData() {
    return data;
  }

  public ColdCluster() {
    data = new Hashtable<String, String>();
  }

  public String get(String key) {
    return data.get(key);
  }

  public void put(String key, String value) {
    data.put(key, value);
  }


}