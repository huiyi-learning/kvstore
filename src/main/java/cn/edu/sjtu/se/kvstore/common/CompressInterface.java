/**
 * @author HC
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.VariableElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.*;
import com.google.common.collect.Lists;

public class CompressInterface<K,V> {
	
	private static final Logger logger = LoggerFactory.getLogger(CompressInterface.class);
	
	private static final Map<String, String> compressdata;
	
	static {
		compressdata = new HashMap<String, String>();
	}
	
	public long getMemSize(){
		
		Iterator<String> it = compressdata.keySet().iterator();
		long memSize = 0;
		while(it.hasNext()){
			String key = it.next();
			String value = compressdata.get(key);
			memSize += key.length() + value.length();
		}
		return memSize;
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
	//@SuppressWarnings("unchecked")
	public Map<K,V> convertColdToHot(Set<K> coldKeys) {
		Map<K, V> rec = new HashMap<K, V>();
		try {
			for (K key : coldKeys) {
				for(String sp : compressdata.keySet()) {
					if(sp.contains(key.toString())) {
						rec.put(key, (V)sp.split("-")[Arrays.asList(sp.split(",")).indexOf(key)]);
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
		try {
			for(String sp : compressdata.keySet()) {
				if(sp.contains(key.toString())) {
					String valueString = ZipUtil.decompress(compressdata.get(sp));
					List<String> items = Lists.newArrayList(Splitter.on(",").split(valueString));
					items.remove(Arrays.asList(sp.split(",")).indexOf(key));
					String val = Joiner.on("|").join(items);
					compressdata.put(sp, ZipUtil.compress(val));
					break;
				}
			}	
		} catch (IOException e) {
			logger.debug("Compressinterface removeData error: " + e.toString());
		}
	}
}
