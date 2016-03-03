package com.qingruan.gateway;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class THTimeTask {
	
	private static Timer timer;
	private static TimerTask task;
	private static AirDownDataPackage air;
	public static void runTimeTask(){
		air = new AirDownDataPackage();
		timer = new Timer();
		task  = new TimerTask() {
			
			@Override
			public void run() {
				readValueAction();
			}

		};
		
		try {
			//long timeInterval = Long.valueOf(PropertiesUtils.readValue("thclient.read.timeinterval")) * 3000;
			timer.schedule(task, 3000,3000);
			System.out.println("心跳定时任务启动");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	public static void readValueAction() {
		try {
			ConcurrentHashMap<String, Channel> macChannel = CommonData.deviceChannels;
			for (Map.Entry<String, Channel> m : macChannel.entrySet()) {
				air.remoteTest(0, m.getKey());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public static void stopTimeTask(){
		if(task!=null){
			task.cancel();
		}
		
		if(timer!=null){
			timer.cancel();
		}
	}
}
