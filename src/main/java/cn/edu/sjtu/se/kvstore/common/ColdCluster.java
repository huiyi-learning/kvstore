/**
 * @author Francis
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

/**
 * data structure used to store cold data.
 */
public class ColdCluster {
  
  private Hashtable<Integer, String> data;

  public void setData() {
    this.data = data;
  }

  public Hashtable<Integer, String> getData() {
    return data;
  }

  public ColdCluster() {
    data = new Hashtable<Integer, String>();
  }

  public String get(Integer key) {
    return data.get(key);
  }

  public void put(Integer key, String value) {
    data.put(key, value);
  }


}