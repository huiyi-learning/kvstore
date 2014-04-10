/**
 * @author Francis
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Data<K, V> {

  private ConcurrentHashMap<K, V> hot;

  public ConcurrentHashMap<K, V> getHot() {
    return hot;
  }

  private Set<K> cold;

  public Set<K> getCold() {
    return cold;
  }

  public Data() {
    hot = new ConcurrentHashMap<K, V>();
    cold = Collections.synchronizedSet(new HashSet<K>());
  }

  public V get(K key) {
    if (hot.containsKey(key)) {
      return hot.get(key);
    } else {
      if (cold.contains(key)) {
        // uncompress and return
      } else {
        // return empty value
        return null;
      }
    }

    return null;
  }

  /**
   * @return
   * the previous value associated with key, or null if there was no mapping for key
   */
  public V put(K key, V value) {
    return hot.put(key, value);
  }

  public boolean containsKey(K key) {
    return hot.containsKey(key) || cold.contains(key);
  }

  public V remove(K key) {
    if (hot.containsKey(key)) {
      return hot.remove(key);
    } else {
      // delete compressed data associated with key
    }

    return null;
  }

  public void clear() {
    hot.clear();
    cold.clear();
  }

  public int size() {
    return hot.size() + cold.size();
  }

  public void moveHot2Cold(List<String> keys) {

  }

  public void moveCold2Hot(Map<String, String> items) {
    
  }
}
