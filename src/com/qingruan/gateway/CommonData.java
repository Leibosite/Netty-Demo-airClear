package com.qingruan.gateway;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.util.internal.ConcurrentSet;

public class CommonData {
	
	
	//盛放和设备连接的通道
	public static ConcurrentHashMap<String, Channel> deviceChannels = new ConcurrentHashMap<String, Channel>();
	
	//恒温恒湿展柜的连接�?�?
	public static ConcurrentHashMap<String, Channel> thChannels = new ConcurrentHashMap<String, Channel>();
	
	//包序号缓�?
	public static ConcurrentHashMap<String, Integer> thSerialNumber = new ConcurrentHashMap<String, Integer>();
	
	//缓存上传上来的数据，防止粘包
	public static ConcurrentHashMap<Channel, StringBuilder> msgCache = new ConcurrentHashMap<Channel, StringBuilder>();
	
	/*//缓存传感器下发的任务
	public static ConcurrentHashMap<String,ConcurrentLinkedQueue<DownDataPackage>> downTaskMap = new ConcurrentHashMap<String,ConcurrentLinkedQueue<DownDataPackage>>();
	
	//缓存中继的macAddressList
	public static ConcurrentHashMap<String,DownDataPackage>  macAddressList = new ConcurrentHashMap<String, DownDataPackage>();
	
	//缓存下发恒温恒湿展柜的命�?
	public static ConcurrentLinkedQueue<ThDownDataPackage> ThDownDataQueue = new ConcurrentLinkedQueue<ThDownDataPackage>();
	
	//缓存websocket和浏览器之间的�?信�?�?
	public static List<Channel> browserChannels = new ArrayList<Channel>();
	
	//恒温恒湿展柜下发的命令，如果收到回复比较包序号如果相同则任务下发成功，如果下发失败，超时10秒从新下发，下发三次不成功就告警
	public static ConcurrentHashMap<String,ConcurrentLinkedQueue<ThDownDataPackage>> thDownTaskMap = new ConcurrentHashMap<String,ConcurrentLinkedQueue<ThDownDataPackage>>();
	*/
	public static ConcurrentHashMap<String, Thread>  connectThreads= new ConcurrentHashMap<String, Thread>();
	
	public static ConcurrentSet<Channel> modbusChannelSet = new ConcurrentSet<Channel>();
	
	public static String connectGateWaySuccessIp = "";
	
	public static Channel connectThClientChannel;
	
	public static int airCleanerPort = -1;
	
	//SendCmdType 是发送的命令类型  reciveCmdValue 是接收到命令的预测值
	public enum SendCmdType {ONLINE,FANSPEED,MODE,TIMER_SHUTDOWN,ANION,UV,HUMIDIFICATION,CHILDLOCK}
	
	public static int reciveCmdValue = -1;

	public static SendCmdType sendcmdtype = null;
	
	public static String IP = "";
	public static int port = -1;
	
	
	public static int getSerialNumber(String clientAddress){
		int serialNumber = 0;
		if(thSerialNumber.containsKey(clientAddress)){
			serialNumber = thSerialNumber.get(clientAddress); 
			if(serialNumber>=255){
				serialNumber = 0;
			}
		}
		return serialNumber;
	}
	
	public static void setSerialNumber(String clientAddress,Integer number){
		thSerialNumber.put(clientAddress, number);
	}
	
	/*public static ConcurrentLinkedQueue<ThDownDataPackage> getThDownTaskQueue(String clientAddress){
		if(!thDownTaskMap.containsKey(clientAddress)){
			ConcurrentLinkedQueue<ThDownDataPackage> queue = new ConcurrentLinkedQueue<ThDownDataPackage>();
			thDownTaskMap.put(clientAddress, queue);
		}		
		return thDownTaskMap.get(clientAddress);
	}*/
	
	public static String getChannelIpAddress(Channel ch){
		InetSocketAddress insocket = (InetSocketAddress) ch.remoteAddress();
		String clientAddress = "";
		if(insocket!=null){
			String clientIP = insocket.getHostString();
			int clientPort = insocket.getPort();
			clientAddress = clientIP+":"+clientPort;
		}	
		return clientAddress;
	}
	
	public static int getLocalIpAddressPort(Channel ch){
		InetSocketAddress insocket = (InetSocketAddress) ch.localAddress();
		int localPort = -1;
		if(insocket!=null){
			localPort = insocket.getPort();
		}
		return localPort;
	}
	
	public static Channel getChannelByIpAddress(String ipAddress){
		if(thChannels.containsKey(ipAddress)){
			return thChannels.get(ipAddress);
		}
		return null;
	}
	
	public static int getAirCleanerPort(){
		try {
			if(airCleanerPort== -1){
				 airCleanerPort = Integer.valueOf(PropertiesUtils.readValue("gateway.port"));
			}
			return airCleanerPort;
		} catch (Exception e) {
			
			return -1;
		}	
	}
}
