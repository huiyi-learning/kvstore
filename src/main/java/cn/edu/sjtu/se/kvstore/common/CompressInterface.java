package cn.edu.sjtu.se.kvstore.common;

public class CompressInterface {
	
	//delete cold data 
	public static boolean delete() {
		return true;
	}
	
	//transfer hot data to cold
	public static boolean convert() {
		return true;
	}
	
	//get cold data
	public static String get() {
		return "";
	}
}
