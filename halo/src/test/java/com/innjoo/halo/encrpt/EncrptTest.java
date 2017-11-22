package com.innjoo.halo.encrpt;

import com.innjoo.halo.utils.Encryption;

public class EncrptTest {

	public static void main(String[] args) {
		int a = 134;
		System.out.println(Encryption.encryptId(a));
		
		char[] hello = "hello".toCharArray();
		System.out.println(Encryption.crc16(hello, (short) hello.length));
	}
}
