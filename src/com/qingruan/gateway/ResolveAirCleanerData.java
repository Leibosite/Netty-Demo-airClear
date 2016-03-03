package com.qingruan.gateway;

import io.netty.channel.Channel;


public class ResolveAirCleanerData {
	
	public static void resolveAirCleanerData(Channel channel,String msg){
		
		try {
			String macAddress = msg.substring(0, 12);
			String msgData    = msg.substring(12);
			int length = msgData.length();
			if(length==34*2){
				String data = msgData.substring(32, length-2);
				
				int online = Integer.valueOf(data.substring(0, 2),16);
				int fanSpeed = Integer.valueOf(data.substring(2, 4),16);
				int mode = Integer.valueOf(data.substring(4, 6),16);
				int anion = Integer.valueOf(data.substring(6, 8),16);
				int uv   = Integer.valueOf(data.substring(8, 10), 16);
				int timer_shutdown_min = Integer.valueOf(data.substring(10, 12), 16);
				int timer_shutdown_hour = Integer.valueOf(data.substring(12, 14), 16);
				int humidification = Integer.valueOf(data.substring(14, 16), 16);
				int childlock = Integer.valueOf(data.substring(16, 18), 16);
				
				int air_quality = Integer.valueOf(data.substring(18, 22), 16);
				int temperature = Integer.valueOf(data.substring(22, 24), 16);
				//System.out.println(data.substring(22,24));
				//int TVOC = Integer.valueOf(data.substring(24, 26), 16);
				int humidity = Integer.valueOf(data.substring(26, 28), 16);
				//int fliter_life = Integer.valueOf(data.substring(28, 32), 16);
				//int error_code = Integer.valueOf(data.substring(32, 34), 16);
				
				//打印接收到的消息
				System.out.println("PM2.5:"+air_quality*1.0);
				System.out.println("temperature:"+(temperature-128));
				System.out.println("humidity:"+humidity);
				
				if(CommonData.sendcmdtype != null && CommonData.reciveCmdValue != -1)
				{
					switch(CommonData.sendcmdtype){
					case ONLINE:
						if(CommonData.reciveCmdValue == online && online == 1)
							System.out.println("开启操作成功");
						else if(CommonData.reciveCmdValue == online && online == 0)
							System.out.println("关闭操作成功");
						break;
					case FANSPEED:
						if(CommonData.reciveCmdValue == fanSpeed && fanSpeed == 1)
							System.out.println("已将风速开到1档");
						else if(CommonData.reciveCmdValue == fanSpeed && fanSpeed == 2)
							System.out.println("已将风速开到2档");
						else if(CommonData.reciveCmdValue == fanSpeed && fanSpeed == 3)
							System.out.println("已将风速开到3档");
						break;
					case MODE:
						if(CommonData.reciveCmdValue == mode && mode == 1)
							System.out.println("已将模式切换到睡眠状态");
						else if(CommonData.reciveCmdValue == mode && mode == 0)
							System.out.println("已将模式切换到自动状态");
						else if(CommonData.reciveCmdValue == mode && mode == 2)
							System.out.println("已将模式切换到手动状态");
						break;
					case ANION:
						if(CommonData.reciveCmdValue == anion && anion == 1)
							System.out.println("负离子已开启");
						else if(CommonData.reciveCmdValue == anion && anion == 0)
							System.out.println("负离子已关闭");
						break;
					case UV:
						if(CommonData.reciveCmdValue == uv && uv == 1)
							System.out.println("紫外灯已开启");
						else if(CommonData.reciveCmdValue == uv && uv == 0)
							System.out.println("紫外灯已关闭");
						break;
					case HUMIDIFICATION:
						if(CommonData.reciveCmdValue == humidification && humidification == 1)
							System.out.println("加湿已开启");
						else if(CommonData.reciveCmdValue == humidification && humidification == 0)
							System.out.println("加湿已关闭");
						break;
					case CHILDLOCK:
						if(CommonData.reciveCmdValue == childlock && childlock == 1)
							System.out.println("童锁已开启");
						else if(CommonData.reciveCmdValue == childlock && childlock == 0)
							System.out.println("童锁已关闭");
						break;
					case TIMER_SHUTDOWN:
						if(CommonData.reciveCmdValue == timer_shutdown_min && timer_shutdown_min == 2)
							System.out.println("定时2分钟关闭已设置");
						break;
					default:
						CommonData.reciveCmdValue = -1;
						CommonData.sendcmdtype = null;	
						break;
					}
				}
				System.out.flush();
			}
		} catch (Exception e) {
			System.out.println(ExceptionLog.getErrorStack(e));
		}	
	}
}
