package com.qingruan.gateway;

import static io.netty.buffer.Unpooled.copiedBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;


public class SendMessage {
	
	public static void sendByteData(Channel channel,byte[] msg){
		
		if(channel!=null && channel.isActive() && channel.isRegistered() && channel.isWritable()){
			String message = EncoderUtil.getHexStr(msg);
			
			channel.writeAndFlush(msg);
		
		}else{
			System.out.println("byte[]***消息没有发出去***");
		}
	}
	
	public static void sendStringData(Channel channel,String msg){
		if(channel!=null && channel.isActive() && channel.isRegistered() && channel.isWritable()){
			ByteBuf buffer = copiedBuffer(msg, CharsetUtil.US_ASCII);
			channel.writeAndFlush(buffer);
			System.out.println("String***消息发送成功***");
		}else{
			System.out.println("String***消息没有发出去***");
		}
	}
	
	// 发送消息
	public static void sendMsg(Channel channel, String OriginMsg) {
		if (channel!=null && channel.isActive() && channel.isRegistered() && channel.isWritable() && channel.isOpen()) {
			if (!"".equals(OriginMsg)) {
				byte[] send = EncoderUtil.HexString2Bytes(OriginMsg, OriginMsg.length() / 2);
				channel.writeAndFlush(send);
				System.out.println("数据发送成功:"+OriginMsg+"\r\n");
			}
		} else {
			System.out.println("数据没有发送出去!!!!-->" + OriginMsg);
		}
	}

}
