package com.qingruan.gateway;

import java.util.Collection;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class MessageHandler extends ChannelInboundHandlerAdapter {

	public MessageHandler() {

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		/**
		 * �?��消息缓存里面是否有此缓存，没有就加上
		 */
		 //System.out.println("channel is active"+ctx.channel());
		System.out.println("channel is active and channel is" + ctx.channel());
		
		if (!CommonData.msgCache.containsKey(ctx.channel())) {
			CommonData.msgCache.put(ctx.channel(), new StringBuilder());
		}
	}


	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		//System.out.println("channel is channelUnregistered"+ctx.channel());
		System.out.println("channel is Unregistered and channel is" + ctx.channel());
		Collection<Channel> channelCollection = CommonData.deviceChannels.values();
		if(channelCollection.contains(ctx.channel())){
			channelCollection.remove(ctx.channel());
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		 //System.out.println("Read Complete");
		Channel channel = ctx.channel();

		List<String> msgList = ResolveData.msgDeal(channel);
		
		if (msgList == null || msgList.size() == 0)
			return;

		for (int i = 0; i < msgList.size(); i++) {
			String msgData = msgList.get(i);
			 
			if(msgData.length()>8){

				
				if(msgData.length()>14 && "AA".equals(msgData.substring(12, 14))){
					if(CommonData.getAirCleanerPort()==CommonData.getLocalIpAddressPort(channel)){
						CommonData.deviceChannels.put(msgData.substring(0,12), channel);
						ResolveAirCleanerData.resolveAirCleanerData(channel, msgData);
					}
				}
			
				
				//String cmd = msgData.substring(0, 2);
				
			/*	if("7E".equals(cmd)){
					ResolveWeatherData.resolveWeatherStationData(channel, msgData);
				}

				if("B0".equals(cmd) || "B1".equals(cmd) || "B2".equals(cmd) || "B3".equals(cmd)){
					CommonData.connectThClientChannel = channel;
					ResolveServerData.judgeCommand(channel, msgData);
				}
				
				if (UnPack.check(msgData)) {
					UpDataPackage info = UnPack.verify(msgData);// 消息拆包
					CommonData.deviceChannels.put(info.getMonitorMacAddress(), channel);
					ResolveData.judgeCommand(info);
				}*/
				
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

		System.out.println("channel is exception and channel is" + ctx.channel());
		ctx.channel().close();
		if (CommonData.deviceChannels.containsValue(ctx.channel())) {
			Collection<Channel> channelCollection = CommonData.deviceChannels.values();
			if(channelCollection.contains(ctx.channel())){
				channelCollection.remove(ctx.channel());
			}
		}
		System.out.println("Message Handler Exception:"+cause.getMessage());
	}
}
