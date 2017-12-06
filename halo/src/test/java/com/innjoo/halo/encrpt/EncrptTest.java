package com.innjoo.halo.encrpt;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import com.innjoo.halo.utils.Encryption;

public class EncrptTest {

	public static void main(String[] args) {
		int a = 134;
		System.out.println(Encryption.encryptId(a));

		char[] hello = "hello".toCharArray();
		for (int i = 0; i < 10; ++i) {
			byte[] crcData = new byte[] { 0x68, 0x61, 0x6c, 0x6f, 0x68, 0x61, 0x6c, 0x6f, 0x0, 0x0, 0x0, 0x12,
					(byte) 0xf0, 0x0, 0x0, 0x01, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, (byte) 0xf0, 0x3, 0x12, 0x34, 0x56, 0x78,
					(byte) 0xc4, 0x72 };
			
			byte[] cData = new byte[] {(byte) 0xf0,0x0,0x0,0x01,
					0x0,0x0,0x0,0x1,
					0x0,0x0,(byte) 0xf0,0x3,
					0x12,0x34,0x56,0x78};
			String crc = Encryption.getCrc(cData);
			System.out.println(crc);
		}
	}

	private static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);

		return cb.array();
	}
}
