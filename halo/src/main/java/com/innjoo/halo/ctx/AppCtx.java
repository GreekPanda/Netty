package com.innjoo.halo.ctx;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * @Auth:廖黄河
 * @date:2017-11-9
 * 
 * 构造spring的上下文件用来使之可以访问数据库等，线程安全的饿汉单例模式
 * 
 * */

public class AppCtx {

	public static String[] xmlList = new String[] { "spring.xml", "spring-mybatis.xml" };

	private static ApplicationContext instance = new ClassPathXmlApplicationContext(xmlList);

	public static synchronized ApplicationContext getInstance() {
		if (instance == null)
			instance = (new ClassPathXmlApplicationContext(xmlList));
		return instance;
	}
}
