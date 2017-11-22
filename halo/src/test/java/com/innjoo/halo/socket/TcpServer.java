package com.innjoo.halo.socket;

import org.apache.log4j.Logger;

import com.innjoo.halo.netty.NettyServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TcpServer {

	private static final Logger LOG = Logger.getLogger(TcpServer.class);

	public static void start(int port) {

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new TcpServerInitializer());

            try {
            	LOG.info("Server is running, binding port is: " + port);
            	System.out.println("Serve is running, listen on port: " + port);
            	Channel ch = b.bind(port).sync().channel();
				
				ch.closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
	}
	
	public static void main(String[] args) {
		TcpServer.start(56778);
		
	}
}
