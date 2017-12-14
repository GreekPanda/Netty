package com.innjoo.halo;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.innjoo.halo.process.ProcComm;
import com.innjoo.halo.utils.Utils;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private static byte[] testBf() {
		short value = 2017;
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(value);
		return buffer.array();
	}

	public static void main(String[] args) {
		byte[] b = new byte[2];

		b = testBf();

		System.out.println(Arrays.toString(b));
		System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(b));

		byte[] c = new byte[2];
		c = Utils.short2Byte((short) 2017);
		System.out.println(Arrays.toString(c));

		System.out.println(Arrays.toString(ProcComm.getLocalTime()));
		System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(ProcComm.getLocalTime()));

	}
}
