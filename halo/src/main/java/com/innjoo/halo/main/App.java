package com.innjoo.halo.main;


import org.apache.log4j.Logger;

import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.netty.NettyServer;

/**
 * 主入口程序
 *
 */
public class App {
	private static final Logger LOG = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		AppCtx.getInstance();
		NettyServer.start(9501);
	}
}
