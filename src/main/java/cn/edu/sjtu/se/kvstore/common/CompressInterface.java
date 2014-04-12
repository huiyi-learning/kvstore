package cn.edu.sjtu.se.kvstore.common;

import java.util.Map;
import java.util.Set;

public class CompressInterface<K,V> {
	
	//delete cold data 
	//根据传入的coldKeys，将对应的压缩cold data的values删除
	public boolean delete(Set<K> coldKeys) {
		return true;
	}
	
	//transfer hot data to cold, namely compress data
	//hotDatas表示已经变成cold data的hot data,将对应的values进行压缩
	public boolean convertHotToCold(Map<K,V> hotDatas) {
		return true;
	}
	
	//transfer hot data to cold, namely decompress data
	//coldKeys表示已经变成hot data的cold data的key，将对应的values解压出来，返回对应的K/V
	public Map<K,V> convertColdToHot(Set<K> coldKeys) {
		return null;
	}
	
	//这个函数没有必要
	//get cold data
	public static String get() {
		return "";
	}
}
