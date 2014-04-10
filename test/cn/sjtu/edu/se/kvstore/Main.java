package cn.sjtu.edu.se.kvstore;

import java.util.Timer;
import java.util.TimerTask;

import cn.edu.sjtu.se.kvstore.identify.Identifying;

public class Main {
	public static void main(String[] args) {
		/*
		 * Calendar c = Calendar.getInstance();
		 * System.out.println(c.get(Calendar.MINUTE));
		 */

		// new Thread(new Identifying()).start();

		Timer timer = new Timer();
		class MyTask extends TimerTask {
			private int flag = 0;

			@Override
			public void run() {
				System.out.println(flag++);
			}

		}
		// timer.schedule(new MyTask(), 1000, 3000);
//		timer.schedule(new Identifying(), 1000, 3000);

		/*
		 * File accessDir = new File(
		 * "/home/hadoop/kvstore/target/kvstore-1.0/logs/accessed"); File[]
		 * accessFiles = accessDir.listFiles(); for(int i = 0;i <
		 * accessFiles.length;i++) System.out.println(accessFiles[i].getName());
		 */
	}
}
