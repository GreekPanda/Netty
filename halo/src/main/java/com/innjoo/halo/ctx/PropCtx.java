package com.innjoo.halo.ctx;

import com.innjoo.halo.utils.PropertyUtils;

public class PropCtx {

	private static final PropertyUtils prop = new PropertyUtils("/sys.properties");

	private PropCtx() {}
	
	public static synchronized PropertyUtils getPropInstance() {
		if (prop == null)
			return new PropertyUtils("/sys.properties");
		return prop;
	}
}
