package com.qingruan.gateway;

import io.netty.channel.Channel;

public class AirDownDataPackage {
	
		
		public void remoteTest(int cmd,String macAddress){
			switch (cmd) {
			case 0:
				deviceStatusQuery(macAddress);
				break;
			case 1:
				deviceShutdown(macAddress);
				break;
			case 2:
				deviceStartup(macAddress);
				break;
			case 3:
				deviceFanSpeed(macAddress, 2);
				break;
			case 4:
				deviceFanSpeed(macAddress, 1);
				break;
			case 5:
				deviceTimerShutdown(macAddress, 2, 0);
				break;
			case 6:
				deviceFanSpeed(macAddress, 3);
				break;
			case 7:
				deviceHumidification(macAddress, 1);
				break;
			case 8:
				deviceHumidification(macAddress, 0);
				break;
			case 9:
				deviceMode(macAddress, 1);
				break;
			case 10:
				deviceMode(macAddress, 0);
				break;
			case 11:
				deviceAnion(macAddress, 1);
				break;
			case 12:
				deviceAnion(macAddress, 0);
				break;
			case 13:
				deviceUv(macAddress, 1);
				break;
			case 14:
				deviceUv(macAddress, 0);
				break;
			case 15:
				deviceChildLock(macAddress, 1);
				break;
			case 16:
				deviceChildLock(macAddress, 0);
				break;				
			case 17:
				deviceMode(macAddress, 2);
				break;
			default:
				break;
			}
		}
	
	
		public void deviceStatusQuery(String macAddress){
			packDataAndSendToDevice(macAddress, 0, 30, 0, 0);
		}
		
		public void deviceStartup(String macAddress){
			packDataAndSendToDevice(macAddress, 0, 31, 0, 0);
		}
		
		public void deviceShutdown(String macAddress){
			packDataAndSendToDevice(macAddress, 0, 32, 0, 0);
		}
		
		public void deviceFanSpeed(String macAddress,int speed){//speed 1~3
			packDataAndSendToDevice(macAddress, 0, 33, 0, speed);
		}
		
		public void deviceMode(String macAddress,int mode){ //0:自动模式 1:睡眠模式2：手动模式
			packDataAndSendToDevice(macAddress, 0, 34, 0, mode);
		}
		
		public void deviceTimerShutdown(String macAddress,int minute,int hour){//minute 0~59 hour 0~23
			packDataAndSendToDevice(macAddress, 0, 36, minute, hour);
		}
		
		public void deviceAnion(String macAddress,int onOrOff){//onOroff 0:关闭 1:�?��
			packDataAndSendToDevice(macAddress, 0, 37, 0, onOrOff);
		}
		
		public void deviceUv(String macAddress,int onOrOff){//onOroff 0:关闭 1:�?��
			packDataAndSendToDevice(macAddress, 0, 38, 0, onOrOff);
		}
		
		public void deviceHumidification(String macAddress,int onOrOff){//onOroff 0:关闭 1:�?��
			packDataAndSendToDevice(macAddress, 0, 39, 0, onOrOff);
		}
		
		public void deviceChildLock(String macAddress,int onOroff){//onOroff 0:关闭 1:�?��
			packDataAndSendToDevice(macAddress, 0, 40, 0, onOroff);
		}
		
		public void deviceReplaceFilter(String macAddress){
			packDataAndSendToDevice(macAddress, 0, 41, 0, 0);
		}
		
		//应答
		public void deviceReplaceFilterResult(String macAddress){
			
		}

	
		
		private void packDataAndSendToDevice(String macAddress,int reqResp,int cmd,int messageResolve,int messageData){
			
			StringBuilder builder = new StringBuilder();
			
			String header = EncoderUtil.hex2BinStr("AA", 8);
			builder.append(header);
			String length = EncoderUtil.hex2BinStr("13", 8);
			builder.append(length);
			String versionBin = "0001";
			builder.append(versionBin);
			String serialNumber = "000000000001";
			builder.append(serialNumber);
			String src_id = EncoderUtil.hex2BinStr("00000000", 32);
			builder.append(src_id);
			String dst_id = EncoderUtil.hex2BinStr("00000000", 32);
			builder.append(dst_id);
			String major_type = "0001";
			builder.append(major_type);
			String req_resp = Integer.toString(reqResp, 2);
			builder.append(req_resp);
			String minor_type = EncoderUtil.hex2BinStr(Integer.toHexString(cmd), 11);
			builder.append(minor_type);
			String reslove_0 = EncoderUtil.hex2BinStr("0000", 16);
			builder.append(reslove_0);
			String message_reslove = EncoderUtil.hex2BinStr(Integer.toHexString(messageResolve), 8);
			builder.append(message_reslove);
			String message = EncoderUtil.hex2BinStr(Integer.toHexString(messageData), 8);
			builder.append(message);
			byte[] messageBytes = EncoderUtil.binaryStrToBytes(builder.toString(), 18);
			int crc = 0;
			for (byte b : messageBytes) {			
				crc = crc + b;
			}
			crc = (~crc&0xff) + 1;
			String crcString = EncoderUtil.long2BinaryStr(crc, 8);
			builder.append(crcString);
			Channel channel = CommonData.deviceChannels.get(macAddress);
			//String test = "AA131000000000000000000010220000000100";
			//SendMessage.sendMsg(channel, test);
			SendMessage.sendByteData(channel, EncoderUtil.binaryStrToBytes(builder.toString(), 19));
			//Set<Entry<String, Channel>> channels = CommonData.deviceChannels.entrySet();
			//for (Entry<String, Channel> channel : channels) {
				//String msg ="AA131000000000000000000010200000000003";
				//String msg = "AA1210000000000000000000100A0000001A";
				//SendMessage.sendByteData(channel.getValue(), EncoderUtil.binaryStrToBytes(builder.toString(), 19));
				//SendMessage.sendMsg(channel.getValue(), msg);
			//}	
		}
}
