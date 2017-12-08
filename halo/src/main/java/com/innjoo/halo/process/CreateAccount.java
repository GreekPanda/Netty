package com.innjoo.halo.process;

import org.apache.log4j.Logger;

import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.service.IHaloChildService;

public class CreateAccount {

	public static final Logger LOG = Logger.getLogger(CreateAccount.class);

	private final static IHaloChildService haloChildService = (IHaloChildService) AppCtx.getInstance()
			.getBean("haloChildService");
	
	public static void proc(byte[] in, byte[] out) {
		
	}

}
