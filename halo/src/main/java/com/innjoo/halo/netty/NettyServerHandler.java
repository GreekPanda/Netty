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
		onLineConn++;
		LOG.info("Remote host ip/port: " + ctx.channel().remoteAddress());
		LOG.info("Current online connect hosts is : " + onLineConn);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HaloProto msg) throws Exception {
		// 将数据发送给客户端
		LOG.debug("Send to client: " + msg.toString());
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		onLineConn--;
		LOG.debug("Channel is channelInactive");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOG.debug("Channel is exceptionCaught, cause of: " + ctx.channel().remoteAddress() + " "
				+ cause.getLocalizedMessage());
		ctx.close();
	}

}
