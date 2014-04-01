package cn.edu.sjtu.se.kvstore.identify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Identifying implements Runnable {

	private static BufferedReader br = null;
	@SuppressWarnings("unchecked")
	private TreeMap<String, Estimate> estimates = new TreeMap<String, Estimate>(new Comparator(){

		@Override
		public int compare(Object o1, Object o2) {
			Estimate e1 = (Estimate)o1;
			Estimate e2 = (Estimate)o2;
			return Double.compare(e1.getFrequence(),e2.getFrequence());
		}
		
	});
	private final double A = 0.01;
	private final int K = 10;
	private long startTime = 0;

	public void run() {
		try {
			br = new BufferedReader(new FileReader("/home/hadoop/access"));
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
			
			
			Set<Map.Entry<String,Estimate>> set = estimates.entrySet();
			Iterator<Map.Entry<String,Estimate>> it = set.iterator();
			while(it.hasNext()){
				Map.Entry<String,Estimate> s = it.next();
				System.out.println(s.getKey() + " " + s.getValue());
			}
			
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
