package com.innjoo.halo.utils;

/*
 * Autho:liaohuanghe
 * date:2017-11-3
 * 
 * 用于加解密操作，当前的加解密操作比较简单，就是一个移位的操作。注意水杯客户端发送过来的数据并没有解密，而且是一个固定的头字段
 * */

public class Encryption {

	public static int encryptId(int id) {
		return (id >> 1) | (0x98 << 24);
	}

	public static Short getcrc16(byte[] string, int length) {

		short crc = (short) (string[0] * 256 + string[1]);

		short lwData = 0;
		for (int i = 2; i <= length - 1; i++) {
			lwData = string[i];
			for (int j = 8; j != 0; j--) {
				if ((crc & 0x8000) == 0x8000) {
					crc <<= 1;
					if (((lwData >> (j - 1)) & 0x1) == 0x1) {
						crc = (short) (crc | 1);
					}
					crc ^= 0x1021;
				} else {
					crc <<= 1;
					if (((lwData >> (j - 1)) & 1) == 1) {
						crc = (short) (crc | 1);
					}
				}
			}
		}
		return (short) (crc % 65536);
	}

	public static void main(String[] args) {
		byte[] test = { 0x1, 0x0, 0x0, (byte) 0xf0, 0x1, 0x0, 0x0, 0x0, 0x3, (byte) 0xf0, 0x0, 0x0, 0x78, 0x56, 0x34,
				0x12 };
		System.out.println(getcrc16(test, test.length) + "," + Integer.toHexString(getcrc16(test, test.length)));
	}
}
