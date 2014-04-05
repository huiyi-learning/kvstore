/**
 * @author Francis
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * data structure used to store cold data.
 */
public class ColdCluster {
  
  /*private Hashtable<String, String> data;

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
  }*/

	//set store the keys of cold data
	private static Set<String> set = new HashSet<String>();

	public static Set<String> getColdSet() {
		return set;
	}

	/*public ColdCluster() {
		this.set = new HashSet<String>();
	}*/
	
	public static void clear(){
		set.clear();
	}
	
	public static void add(String data){
		set.add(data);
	}
	

}