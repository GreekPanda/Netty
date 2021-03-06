package com.innjoo.halo.netty;

import com.innjoo.halo.ctx.PropCtx;
import com.innjoo.halo.proto.HaloProto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyServerEncoder extends MessageToByteEncoder<HaloProto> {

	

	@Override
	protected void encode(ChannelHandlerContext ctx, HaloProto msg, ByteBuf out) throws Exception {

		// private byte[] package_head;
		// private int package_len;
		// private int sender_id;
		// private int receiver_id;
		// private short sender_type;
		// private short contorl_code;
		// private byte[] data;
		// private short crc;

		// 构造发送给客户端的数据
		String hostEdian = PropCtx.getPropInstance().getProperty("host.endian");
		if (hostEdian.equals("little")) {

			out.writeBytes(msg.getPackage_head());
			out.writeIntLE(msg.getPackage_len());
			out.writeIntLE(msg.getSender_id());
			out.writeIntLE(msg.getReceiver_id());
			out.writeShortLE(msg.getSender_type());
			out.writeShortLE(msg.getControl_code());
			out.writeBytes(msg.getData());
			out.writeShortLE(msg.getCrc());
		} else {
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

}
