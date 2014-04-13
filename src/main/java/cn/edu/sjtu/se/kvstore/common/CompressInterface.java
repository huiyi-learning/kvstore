/**
 * @author HC
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.*;

public class CompressInterface<K,V> {
	
	private static final Logger logger = LoggerFactory.getLogger(CompressInterface.class);
	
	private static final Map<String, String> compressdata;
	
	static {
		compressdata = new HashMap<String, String>();
	}
	
	//delete cold data 
	//根据传入的coldKeys，将对应的压缩cold data的values删除
	public boolean delete(Set<K> coldKeys) {
		try {
			for (K iterator : coldKeys) {
				removeData(iterator.toString());
			}
		}
		catch(Exception e) {
			logger.debug("Compressinterface delete error: " + e.toString());
			return false;
		}
		return true;
	}
	
	//transfer hot data to cold, namely compress data
	//hotDatas表示已经变成cold data的hot data,将对应的values进行压缩
	public boolean convertHotToCold(Map<K,V> hotDatas) {
		String key = Joiner.on("|").join(hotDatas.keySet());
		String value = null;
		try {
			value = ZipUtil.compress(Joiner.on("|").join(hotDatas.values()));
		} catch (IOException e) {
			logger.debug("Compressinterface convertHotToCold error: " + e.toString());
		}
		compressdata.put(key, value);
		return true;
	}
	
	//transfer hot data to cold, namely decompress data
	//coldKeys表示已经变成hot data的cold data的key，将对应的values解压出来，返回对应的K/V
	public Map<K,V> convertColdToHot(Set<K> coldKeys) {
		Map<K, V> rec = new HashMap<K, V>();
		try {
			for (K key : coldKeys) {
				for(String sp : compressdata.keySet()) {
					if(sp.contains(key.toString())) {
						//rec.put(, );
						break;
					}
				}
			}
		}
		catch(Exception e) {
			logger.debug("Compressinterface convertColdToHot error: " + e.toString());
			return null;
		}
		return rec;
	}
	
	private void removeData(String key) {
		
	}
}
