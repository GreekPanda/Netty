package com.innjoo.halo.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TcpClient {

	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer.parseInt(System.getProperty("port", "56778"));

	public static void main(String[] args) {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new TcpClientInitializer());

			// Make a new connection.
			ChannelFuture f = null;
			try {
				f = b.connect(HOST, PORT).sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Get the handler instance to retrieve the answer.
			TcpClientHandler handler = (TcpClientHandler) f.channel().pipeline().last();

		} finally {
			group.shutdownGracefully();
		}
	}

}
