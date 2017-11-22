package com.innjoo.halo.netty;

import org.apache.log4j.Logger;

import com.innjoo.halo.proto.HaloProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler extends SimpleChannelInboundHandler<HaloProto> {
	

	private static final Logger LOG = Logger.getLogger(NettyServerHandler.class);
	private static long onLineConn = 0;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		onLineConn++;
		LOG.info("Remote host ip/port: " + ctx.channel().remoteAddress());
		LOG.info("Current online connect hosts is : " + onLineConn);
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HaloProto msg) throws Exception {
		// TODO Auto-generated method stub
		
		//将数据发送给客户端
		LOG.info("Send to client: " + msg.toString());
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		onLineConn--;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOG.fatal("Channel is exceptionCaught, cause of: " + cause.getCause());
		//cause.printStackTrace();
		ctx.close();
	}

}
