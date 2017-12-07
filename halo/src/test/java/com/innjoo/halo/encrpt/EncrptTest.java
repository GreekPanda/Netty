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

			String str = "68 61 6c 6f 68 61 6c 6f 31 32 33 340 20 00 00 0 220 0 32 30 31 37 30 36 32 38 2d 31 32 33 34 35 36 37 32 30 31 37 30 36 32 38 2d 31 32 33 34 35 36 37 320 0 140 00 10 30 40 50 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 60 00 10 1 e10 70 b0 40 8 37 230 00 00 0 140 00 00 00 10 00 00 00 20 00 00 00 30 00 00 00 40 00 00 00 50 0".replace(" ", ",");
			//System.out.println(str);
			byte[] cData = new byte[] { (byte) 0xf0, 0x0, 0x0, 0x01, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, (byte) 0xf0, 0x3,
					0x12, 0x34, 0x56, 0x78 };
			int crc = Encryption.crc16(cData, (short) cData.length);
			//System.out.println(crc);
			
			
		}
		
		byte[] aaaa = new byte[] {0,0,0,1};
		byte[] b = new byte[4];
		System.out.println(aaaa[0] + "," + aaaa[1] + "," + aaaa[2] + "," + aaaa[3]);
		System.arraycopy(aaaa, 0, b, 0, 4);
		System.out.println(b[0] + "," + b[1] + "," + b[2] + "," + b[3]);
		int ret = ByteBuffer.wrap(b).order(java.nio.ByteOrder.BIG_ENDIAN).getInt();
		System.out.println(ret);
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
