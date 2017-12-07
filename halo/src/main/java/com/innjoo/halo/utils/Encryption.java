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

	public static int crc16(byte[] pchMsg, short wDataLen) {
		short lwCrc, lwData;
		int i = 0;
		int bitPos = 0;
		//lwCrc = (short) (pchMsg[0] * 256 + pchMsg[1]);
		lwCrc = (short) (pchMsg[0] + pchMsg[1]);
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

		return (lwCrc % 0x10000);
	}
	
	public static short crc161(byte[] pchMsg, short wDataLen) {
		short lwCrc, lwData;
		int i = 0;
		int bitPos = 0;
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

	public static String getCrc(byte[] data) {
		int high;
		int flag;

		// 16位寄存器，所有数位均为1
		int wcrc = 0xffff;
		for (int i = 0; i < data.length; i++) {
			// 16 位寄存器的高位字节
			high = wcrc >> 8;
			// 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
			wcrc = high ^ data[i];

			for (int j = 0; j < 8; j++) {
				flag = wcrc & 0x0001;
				// 把这个 16 寄存器向右移一位
				wcrc = wcrc >> 1;
				// 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
				if (flag == 1)
					wcrc ^= 0xa001;
			}
		}

		return Integer.toHexString(wcrc);
	}

}
