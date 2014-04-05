package cn.edu.sjtu.se.kvstore.db;

import java.util.Hashtable;

public class Data {
	private Hashtable<String, String> data;

	  public void setData() {
	    this.data = data;
	  }

	  public Hashtable<String, String> getData() {
	    return data;
	  }

	  public Data() {
	    data = new Hashtable<String, String>();
	  }

	  public String get(String key) {
	    return data.get(key);
	  }

	  public void put(String key, String value) {
	    data.put(key, value);
	  }
}
