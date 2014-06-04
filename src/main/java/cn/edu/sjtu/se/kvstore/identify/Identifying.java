package cn.edu.sjtu.se.kvstore.identify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.sjtu.se.kvstore.common.CompressInterface;
import cn.edu.sjtu.se.kvstore.db.KVstore;

public class Identifying extends TimerTask {

	private static final Logger logger = LoggerFactory
			.getLogger(Identifying.class);

	private static BufferedReader br = null;
	private Map<String, Estimate> estimates = new HashMap<String, Estimate>();
	/*
	 * private Map<String, Estimate> estimates = new TreeMap<String,
	 * Estimate>(new Comparator<String>(){
	 * 
	 * @Override public int compare(String key1, String key2) { Estimate e1 =
	 * estimates.get(key1); Estimate e2 = estimates.get(key2); if(e1 != null){
	 * if(e2 != null) return Double.compare(e1.getFrequence(),
	 * e2.getFrequence()); else return 1; } else { if(e2 != null) return -1;
	 * else return 0; } } });
	 */
	private static final double A = 0.01;
	private final int K = 30;
	private static long startTime = System.currentTimeMillis() / 1000;;
	private static final HashSet<String> scannedFiles = new HashSet<String>();
	
	private KVstore store = null;

	public Identifying(KVstore store){
		this.store = store;
	}
	
	public void run() {
		File accessDir = new File(System.getProperty("distribution.dir")+"/logs/accessed");
		logger.info("start to run");
		//File accessDir = new File("/home/hadoop/kvstore/target/kvstore-1.0/logs/accessed");
		File[] accessFiles = accessDir.listFiles();
		//if(accessFiles != null)
		//	logger.info(String.valueOf(accessFiles.length));
		try {
			if (accessFiles != null)
				for (int i = 0; i < accessFiles.length; i++)
					if (scannedFiles.contains(accessFiles[i].getName()) == false) {

						br = new BufferedReader(new FileReader(accessFiles[i]));
						String str = null;
						while ((str = br.readLine()) != null) {
							String[] record = str.split(" ");
							String key = record[1];
							long current = Long.valueOf(record[0]);
							Estimate estimate = estimates.get(key);
							if (estimate == null) {
								estimate = new Estimate(current, A);
							} else {
								double frequence = A
										+ estimate.getFrequence()
										* Math.pow(
												1 - A,
												current
														- estimate
																.getPreVisitTime());
								estimate.setPreVisitTime(current);
								estimate.setFrequence(frequence);
							}
							estimates.put(key, estimate);
						}

						scannedFiles.add(accessFiles[i].getName());
					}

			Set<String> keySet = estimates.keySet();
			Object[] keys = keySet.toArray();
			Arrays.sort(keys, new Comparator<Object>() {
				@Override
				public int compare(Object key1, Object key2) {
					Estimate e1 = estimates.get((String) key1);
					Estimate e2 = estimates.get((String) key2);
					return Double.compare(e2.getFrequence(), e1.getFrequence());
				}

			});
			
			//logger.info("keys.length = " + keys.length);
			
			//现在的hot data map
			Map<String,String> hotMap = store.getHot();		
			//logger.info("hotMap size = " + hotMap.size());
			//现在的cold data set
			Set<String> coldSet = store.getCold();
			//logger.info("coldSet size = " + coldSet.size());

			//变成hot data的cold data的key，需要从cold data中解压
			Set<String> hotInColdSet = new HashSet<String>();
			
			//需要压缩的cold data，该cold data现在在hot data中
			Map<String,String> toCompressColdMap = new HashMap<String,String>();
			
			//需要删除的cold data
			Set<String> toDeleteColdKeys = store.getRm();			
			
			
			for (int j = 0; j < K && j < keys.length; j++) {
				// System.out.println(keys[j] + " " +
				// estimates.get(keys[j]).getFrequence());
//				HotCluster.add((String) keys[j]);
				String hotKey = (String)keys[j];
				if(coldSet.contains(hotKey))
					hotInColdSet.add(hotKey);
			}

			for (int j = K; j < keys.length; j++) {
				//ColdCluster.add((String) keys[j]);
				/*
				 * logger.info(keys[j] + " " +
				 * estimates.get(keys[j]).getFrequence());
				 */
				String coldKey = (String)keys[j];
				if(hotMap.containsKey(coldKey))
					toCompressColdMap.put(coldKey,hotMap.get(coldKey));					
			}

			//displayDataCluster();
			/*logger.info("hotInColdSet size = " + hotInColdSet.size());
			logger.info("toCompressColdMap size = " + toCompressColdMap.size());
			logger.info("toDeleteColdKeys size = " + toDeleteColdKeys.size());*/
			
			CompressInterface<String,String> ci = new CompressInterface<String,String>();
			
			//delete
			ci.delete(toDeleteColdKeys);
			store.clearRm();
			
			//logger.info("finish delete");
			
			//decompress
			Map<String,String> compressedHotData = ci.convertColdToHot(hotInColdSet);
			
			//logger.info("finish convertColdToHot");
			
			//compress
			Iterator<String> it = toCompressColdMap.keySet().iterator();
			long beforeMemSize = 0;
			while(it.hasNext()){
				String key = it.next();
				String value = toCompressColdMap.get(key);
				beforeMemSize += key.length() + value.length();
			}

			if(beforeMemSize != 0)
			{

				logger.info("before compress: " + beforeMemSize/1024 + "KB");
				
				long afterMemSize = ci.testConvertHotToCold(toCompressColdMap);
				
				//logger.info("finish convertHotToCold");	
				logger.info("after compress: " + afterMemSize/1024 + "KB");
				
				
				logger.info("compress rate:  " + (1 - ((double)afterMemSize/beforeMemSize)));
			}			

			store.moveCold2Hot(compressedHotData);			
			store.moveHot2Cold(toCompressColdMap.keySet());
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.info("file not found!");
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("access file error!");
		}
	}

/*	public void displayDataCluster() {
		logger.info("======================");
		logger.info("host data:");

		Set<String> hotKeySet = HotCluster.getHotSet();
		Iterator<String> hotKeys = hotKeySet.iterator();
		while (hotKeys.hasNext()) {
			String hotKey = hotKeys.next();
			logger.info(hotKey + " " + estimates.get(hotKey).getFrequence());
		}

		logger.info("======================");
		logger.info("cold data:");

		Set<String> coldKeySet = ColdCluster.getColdSet();
		Iterator<String> coldKeys = coldKeySet.iterator();
		while (coldKeys.hasNext()) {
			String coldKey = coldKeys.next();
			logger.info(coldKey + " " + estimates.get(coldKey).getFrequence());
		}

		logger.info("======================");
	}
*/
}
