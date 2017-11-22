package com.innjoo.halo.netty;

import com.innjoo.halo.proto.HaloProto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyServerEncoder extends MessageToByteEncoder<HaloProto>{

	@Override
	protected void encode(ChannelHandlerContext ctx, HaloProto msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		
		
		
//		private byte[] package_head;
//		private int package_len;
//		private int sender_id;
//		private int receiver_id;
//		private short sender_type;
//		private short contorl_code;
//		private byte[] data;
//		private short crc;
		
		//构造发送给客户端的数据
		out.writeBytes(msg.getPackage_head());
		out.writeInt(msg.getPackage_len());
		out.writeInt(msg.getSender_id());
		out.writeInt(msg.getReceiver_id());
		out.writeShort(msg.getSender_type());
		out.writeShort(msg.getControl_code());
		out.writeBytes(msg.getData());
		out.writeShort(msg.getCrc());	
	}

}
