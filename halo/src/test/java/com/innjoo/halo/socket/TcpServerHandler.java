package com.innjoo.halo.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpServerHandler extends SimpleChannelInboundHandler<Proto> {
	private ByteBuf buf;

	public void channelRead0(ChannelHandlerContext ctx, Proto msg) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("Echo to client's msg: "
		// + msg.getHeaderId() + " , "
		// + msg.getPackage_len()
		// + " " + msg.getRes() + " "
		// + msg.getData());
		ctx.writeAndFlush(msg);

		// ByteBuf m = (ByteBuf)msg;
		// ByteBuf m = (ByteBuf)msg;
		// buf.writeBytes(m); // (2)
		//
		// m.release();
		//
		// if (buf.readableBytes() < 18 ) { // (3)
		// System.out.println("buf length is not right");
		// ctx.close();
		// }
		// ctx.writeAndFlush(m);

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("The remote client's ip: " + ctx.channel().remoteAddress());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
	}

	// 此msg就是从decode中构造的数据，因为decode中使用构造期构造数据

	private byte[] ObjectToByteArray(Object obj) throws Exception {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		Channel channel = ctx.channel();
		// ……
		if (channel.isActive())
			ctx.close();
	}

}
