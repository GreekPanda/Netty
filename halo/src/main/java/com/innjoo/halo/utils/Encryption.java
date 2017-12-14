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

		// 假如传入的是16进制的数据需要转换成ascii
		short crc = (short) (string[0] * 256 + string[1]);
		// short crc = (short) (string[0] + string[1]);

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
		// return (short) (crc % 65536);
		return (short) (crc % 0x10000);
	}

	public static short crc16(int[] pchMsg, short wDataLen) {

		short lwCrc, lwData;
		int i = 0;
		int initDataLen = 0;
		int bitPos = 0;
		initDataLen = wDataLen;

		// 读取第一个字节的内容*256加上第二个字节的内容
		lwCrc = (short) ((pchMsg[0] * 256 + pchMsg[1]) & 0xffff);
		for (i = 2; i <= (wDataLen - 1); i++) {
			lwData = (short) pchMsg[i];
			bitPos = 8;
			do {
				// lwCrc&0x8000)==0x8000
				if ((lwCrc & 0x8000) == 0x8000) {
					lwCrc = (short) (lwCrc << 1);
					if (((lwData >> (bitPos - 1)) & 0x1) == 0x1) {
						lwCrc = (short) ((lwCrc | 1) & 0xffff);
					}
					lwCrc = (short) ((lwCrc ^ 0x1021) & 0xffff);
				} else {
					lwCrc = (short) ((lwCrc << 1) & 0xffff);
					if (((lwData >> (bitPos - 1)) & 1) == 1) {
						lwCrc = (short) ((lwCrc | 1) & 0xffff);
					}
				}
				bitPos--;
			} while (bitPos != 0);
		}
		return (short) ((lwCrc % 0x10000) & 0xffff);
	}

	public static String genCrc(byte[] in) {
		String crc = null;
		
		int[] tmpInt = new int[in.length];
		for (int i = 0; i < in.length; ++i)
			tmpInt[i] = in[i] & 0xFF;
		
		short genCrc = Encryption.crc16(tmpInt, (short) tmpInt.length);
		String strGenCrc = Integer.toHexString(genCrc);

		if (strGenCrc.length() >= 4)
			crc = strGenCrc.substring(strGenCrc.length() - 4, strGenCrc.length());
		else
			crc = strGenCrc;
		return crc;
	}

	public static void main(String[] args) {
		byte[] test = { 0x1, 0x0, 0x0, (byte) 0xf0, 0x1, 0x0, 0x0, 0x0, 0x3, (byte) 0xf0, 0x0, 0x0, 0x78, 0x56, 0x34,
				0x12 };
		System.out.println(getcrc16(test, test.length) + "," + Integer.toHexString(getcrc16(test, test.length)));
	}
}
