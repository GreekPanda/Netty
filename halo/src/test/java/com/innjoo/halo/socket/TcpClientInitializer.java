package com.innjoo.halo.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class TcpClientInitializer extends ChannelInitializer<SocketChannel> {

	
	    @Override
	    public void initChannel(SocketChannel ch) {
	        ChannelPipeline pipeline = ch.pipeline();

	        // Add the number codec first,
	        pipeline.addLast(new ProtoDecoder());
	        pipeline.addLast(new ProtoEncoder());

	        // and then business logic.
	        pipeline.addLast(new TcpClientHandler());
	    }

}
