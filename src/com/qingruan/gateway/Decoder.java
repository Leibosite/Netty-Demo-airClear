package com.qingruan.gateway;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out)  {
		
		int msgLen = in.readableBytes();
		byte[] msg = (byte[]) null;
		StringBuffer sb = new StringBuffer();
		try{
			if ((in.isReadable()) && (msgLen > 0)) {
				msg = new byte[msgLen];
				in.readBytes(msg);	
				sb.append(EncoderUtil.getHexStr(msg));
				CommonData.msgCache.get(ctx.channel()).append(sb);
			}
		}catch(Exception e){
			//log.error(e.toString());
			System.out.println(e.toString());
		}
		//System.out.println("Gataway_Receive_Data:" + sb.toString()+"\r\n");
		
	}

}
