/**
 * @author Francis
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.utils;

import java.util.BitSet;

public class SimpleBloomFilter {
  private static final int SIZE = 2 << 30;
  private static final int[] seeds = new int[] {5, 7, 11, 13, 31, 37, 61};
  private BitSet bits;
  private SimpleHash[] simpleHash;

  public SimpleBloomFilter() {
    simpleHash = new SimpleHash[seeds.length];
    for (int i = 0; i < seeds.length; ++i) {
      simpleHash[i] = new SimpleHash(SIZE, seeds[i]);
    }
  }

  public void add(String value) {
    for (SimpleHash sh : simpleHash) {
      bits.set(sh.hash(value), true);
    }
  }

  public boolean contains(String value) {
    if (value == null) {
      return false;
    }

    boolean ret = true;
    for (SimpleHash sh : simpleHash) {
      ret = ret && bits.get(sh.hash(value));
    }

    return ret;
  } 

  // inner class
  public static class SimpleHash {
    private int cap;
    private int seed;

    public SimpleHash(int cap, int seed) {
      this.cap = cap;
      this.seed = seed;
    }

    public int hash(String value) {
      int result = 0;
      int len = value.length();
      for (int i = 0; i < len; ++i) {
        result = seed * result + value.charAt(i);
      }

      return (cap - 1) & result;
    }
  }
}