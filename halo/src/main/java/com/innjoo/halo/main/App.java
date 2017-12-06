package com.innjoo.halo.main;

import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.ctx.PropCtx;
import com.innjoo.halo.netty.NettyServer;

/**
 * 主入口程序
 *
 */
public class App {
	public static void main(String[] args) {
		AppCtx.getInstance();
		NettyServer.start(Integer.parseInt(PropCtx.getPropInstance().getProperty("host.port")));
	}
}
