package cn.edu.sjtu.se.kvstore.identify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Identifying implements Runnable {	

	private static BufferedReader br = null;
	private Map<String, Estimate> estimates = new HashMap<String, Estimate>();
	@SuppressWarnings("unchecked")
	/*private Map<String, Estimate> estimates = new TreeMap<String, Estimate>(new Comparator<String>(){
		@Override
		public int compare(String key1, String key2) {
			Estimate e1 = estimates.get(key1);
			Estimate e2 = estimates.get(key2);
			if(e1 != null){
				if(e2 != null)
					return Double.compare(e1.getFrequence(), e2.getFrequence());
				else
					return 1;
			}
			else {
				if(e2 != null)
					return -1;
				else
					return 0;
			}
		}
	});*/
	private final double A = 0.01;
	private final int K = 10;
	private static long startTime = 0;

	public void run() {
		try {
			br = new BufferedReader(new FileReader("/home/hadoop/kvstore/target/kvstore-1.0/logs/access.log"));
			String str = null;
			str = br.readLine();
			if (str != null) {
				String[] record = str.split(" ");
				startTime = Long.valueOf(record[0]);
				Estimate estimate = new Estimate(startTime, A);
				estimates.put(record[1], estimate);
			}
			while ((str = br.readLine()) != null) {
				String[] record = str.split(" ");
				String key = record[1];
				long current = Long.valueOf(record[0]);
				Estimate estimate = estimates.get(key);
				if (estimate == null) {
					estimate = new Estimate(current, A);
				} else {
					double frequence = A + estimate.getFrequence()
							* Math.pow(1 - A,current - estimate.getPreVisitTime());
					estimate.setPreVisitTime(current);
					estimate.setFrequence(frequence);
				}
				estimates.put(key, estimate);
			}
			
			Set<String> keySet = estimates.keySet();
			Object[] keys = keySet.toArray();
			Arrays.sort(keys,new Comparator<Object>(){
				@Override
				public int compare(Object key1, Object key2) {
					Estimate e1 = estimates.get((String)key1);
					Estimate e2 = estimates.get((String)key2);
					return Double.compare(e2.getFrequence(), e1.getFrequence());
				}
				
			});
			for(int j = 0;j < K && j < keys.length;j++){
				System.out.println(keys[j] + " " + estimates.get(keys[j]).getFrequence());
			}
			
			/*Set<Map.Entry<String, Estimate>> entries = estimates.entrySet();
			Iterator<Map.Entry<String, Estimate>> it = entries.iterator();
			int i = 0;
			while(it.hasNext() && (i++) < K){
				Map.Entry<String,Estimate> entry  = it.next();
				System.out.println(entry.getKey() + " " + entry.getValue() + "\n");
			}*/
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new Thread(new Identifying()).start();;
	}
}
