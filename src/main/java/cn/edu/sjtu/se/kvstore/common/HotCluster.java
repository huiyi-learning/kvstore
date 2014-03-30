/**
 * @author Francis
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

import java.util.Hashtable;

/**
 * data structure used to store hot data.
 */
public class HotCluster {

  private Hashtable<Integer, String> data;

  public void setData() {
    this.data = data;
  }

  public Hashtable<Integer, String> getData() {
    return data;
  }

  public HotCluster() {
    data = new Hashtable<Integer, String>();
  }

  public String get(Integer key) {
    return data.get(key);
  }

  public void put(Integer key, String value) {
    data.put(key, value);
  }

}