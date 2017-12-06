package com.innjoo.halo.netty;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS));
		pipeline.addLast(new NettyServerDecoder());
		pipeline.addLast(new NettyServerEncoder());
		pipeline.addLast(new NettyServerHandler());

	}

}
