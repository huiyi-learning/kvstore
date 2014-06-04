/**
 * @author Francis
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Data<K, V> {

  private ConcurrentHashMap<K, V> hot;
  private Set<K> cold;
  private Set<K> rm;  // remove list, note: only remove cold set according to this set.

  public ConcurrentHashMap<K, V> getHot() {
    return hot;
  }

  public Set<K> getCold() {
    return cold;
  }

  public Set<K> getRm() {
    return rm;
  }

  public Data() {
    hot = new ConcurrentHashMap<K, V>();
    cold = Collections.synchronizedSet(new HashSet<K>());
    rm = new HashSet<K>();
  }

  public V get(K key) {
    if (hot.containsKey(key)) {
      return hot.get(key);
    } else {
      if (cold.contains(key)) {
        // uncompress and return
        CompressInterface ci = new CompressInterface();
        HashSet<K> coldSet = new HashSet<K>();
        coldSet.add(key);
        Map<K,V> result = ci.convertColdToHot(coldSet);
        return result.get(key);
      } else {
        // return empty value
        return null;
      }
    }

    //return null;
  }

  /**
   * @return
   * the previous value associated with key, or null if there was no mapping for key
   */
  public void put(K key, V value) {
    if (value == null) { // delete operation
      if (hot.containsKey(key)) {
        hot.remove(key);
      } else if (cold.contains(key)) {
        rm.add(key);
      }
    } else { // insert or update
      hot.put(key, value);
      if (cold.contains(key)) {
        rm.add(key);
      }
    }
  }

  public boolean containsKey(K key) {
    return hot.containsKey(key) || cold.contains(key);
  }

  public void clear() {
    hot.clear();
    cold.clear();
  }

  public int size() {
    return hot.size() + cold.size() - rm.size();
  }

  public void moveHot2Cold(Set<K> keys) {
	  Iterator<K> it = keys.iterator();
	  while (it.hasNext()) {
		  K key = it.next();
		  cold.add(key);
		  if (hot.containsKey(key)) {
			  hot.remove(key);
		  }
	  }
  }

  public void moveCold2Hot(Map<K, V> items) {
	  hot.putAll(items);
	  Set<K> keys = items.keySet();
	  Iterator<K> it = keys.iterator();
	  while (it.hasNext()) {
		  K key = it.next();
		  cold.remove(key);
	  }
  }
  
  public void clearRm(){
	  rm.clear();
  }
}
