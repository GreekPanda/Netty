package com.innjoo.halo.encrpt;

import java.util.Arrays;

public class StringConvertTest {

	public static void main(String[] args) {
		String s = " FFFFFFFFF0000001F0030020000001210001";
		//byte[] strAscii = new byte[] { -1, -1, -1, -1, -16, 0, 0, 1, -16, 3, 0, 32, 0, 0, 1, 33, 0, 1 };
		//byte[] strAscii = new byte[] {-1, -1, -1, -1, -16, 0, 0, 1, -16, 3, 0, 32, 0, 0, 1, 32, 0, 1};
		//byte[] strAscii = new byte[] {-1, -1, -1, -1, -16, 0, 0, 1, -16, 3, 0, 32, 0, 0, 1, 35, 0, 1};
		byte[] strAscii = new byte[] {-1, -1, -1, -1, -16, 0, 0, 1, -16, 3, 0, 32, 0, 0, 1, 42, 0, 1};
		
		String[] strHex = new String[strAscii.length];
		for(int i = 0; i < strAscii.length; ++i)
			strHex[i] = "0x" + Integer.toHexString(strAscii[i] & 0x000000ff);
		System.out.println(Arrays.toString(strHex));
		
		int[] intHex = new int[strHex.length];
		
		for(int i = 0; i < strHex.length; ++i)
			intHex[i] = strAscii[i] & 0xff;
		
		System.out.println(Arrays.toString(intHex));

	}

}
