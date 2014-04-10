package cn.sjtu.edu.se.kvstore;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import cn.edu.sjtu.se.kvstore.common.ZipUtil;
import cn.edu.sjtu.se.kvstore.db.KVstore;

public class CompressingTest {

	private StringBuffer compressedData = new StringBuffer();
//	private StringBuffer compressingData = new StringBuffer();
	private HashMap<String,Integer> lengths = new HashMap<String,Integer>();
	private KVstore store = null;
	
	public CompressingTest(KVstore store){
		this.store = store;
	}
	
	@Test
	public void test() {
		try {
			StringBuffer compressingData = new StringBuffer(ZipUtil.decompress(compressedData.toString()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
