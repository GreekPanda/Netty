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

	public static short crc16(char[] pchMsg, short wDataLen) {
		short lwCrc, lwData;
		int i = 0;
		int initDataLen = 0;
		int bitPos = 0;
		initDataLen = wDataLen;
		lwCrc = (short) (pchMsg[0] * 256 + pchMsg[1]);
		for (i = 2; i <= (wDataLen - 1); i++) {
			lwData = (short) pchMsg[i];
			bitPos = 8;
			do {
				if ((lwCrc & 0x8000) == 0x8000) {
					lwCrc = (short) (lwCrc << 1);
					if (((lwData >> (bitPos - 1)) & 0x1) == 0x1) {
						lwCrc = (short) (lwCrc | 1);
					}
					lwCrc = (short) (lwCrc ^ 0x1021);
				} else {
					lwCrc = (short) (lwCrc << 1);
					if (((lwData >> (bitPos - 1)) & 1) == 1) {
						lwCrc = (short) (lwCrc | 1);
					}
				}
				bitPos--;
			} while (bitPos != 0);
		}
		return (short) (lwCrc % 0x10000);
	}

}
