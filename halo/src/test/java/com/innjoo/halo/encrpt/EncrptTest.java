package com.innjoo.halo.encrpt;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.google.common.io.BaseEncoding;
import com.innjoo.halo.utils.Encryption;
import com.innjoo.halo.utils.Utils;

public class EncrptTest {

	public static void main(String[] args) {

		// c472，正确

		int[] crcData = new int[] { (byte) 0xf0, 0x0, 0x0, 0x01, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, (byte) 0xf0, 0x3, 0x12,
				0x34, 0x56, 0x78 };
		String ret1 = Integer.toHexString(Encryption.crc16(crcData, (short) crcData.length));
		System.out.println(
				"hex: " + ret1 + ",dec: " + Integer.parseInt(ret1.substring(ret1.length() - 4, ret1.length()), 16));

		// 此生成的crc是587b，正确
		byte[] cData = new byte[] { 0x1, 0x0, 0x0, (byte) 0xf0, 0x1, 0x0, 0x0, 0x0, 0x3, (byte) 0xf0, 0x0, 0x0, 0x78,
				0x56, 0x34, 0x12 };
		String ret2 = Integer.toHexString(Encryption.getcrc16(cData, (short) cData.length));
		System.out.println(
				"hex: " + ret2 + ",dec: " + Integer.parseInt(ret2.substring(ret2.length() - 4, ret2.length()), 16));

		int[] tmpInt = new int[cData.length];
		for (int i = 0; i < cData.length; ++i)
			tmpInt[i] = cData[i];
		// System.arraycopy(cData, 0, tmpInt, 0, cData.length);
		String ret21 = Integer.toHexString(Encryption.crc16(tmpInt, (short) tmpInt.length));
		System.out.println("hex: " + ret21);

		int[] cSetting = new int[] { 0xFF, 0xFF, 0xFF, 0xFF, 0xF0, 0x00, 0x00, 0x01, 0xF0, 0x03, 0x00, 0x20, 0x00, 0x00,
				0x01, 0x1E, 0x00, 0x01 };
		String ret3 = Integer.toHexString(Encryption.crc16(cSetting, (short) cSetting.length));
		System.out.println("Hex: " + ret3 + ",Dec: " + Integer.parseInt(ret3, 16));//

		// 结果与拷贝之后的结果又不一致？
		int[] test3 = new int[] { 0xFF, 0xFF, 0xFF, 0xFF, 0xF0, 0x00, 0x00, 0x01, 0xF0, 0x03, 0x00, 0x20, 0x00, 0x00,
				0x01, 0x20, 0x00, 0x01 };
		String ret5 = Integer.toHexString(Encryption.crc16(test3, (short) test3.length));
		System.out.println("Hex: " + ret5);//

		int[] test31 = new int[] { 255, 255, 255, 255, 240, 0, 0, 1, 240, 3, 0, 32, 0, 0, 1, 33, 0, 1 };
		String ret51 = Integer.toHexString(Encryption.crc16(test31, (short) test31.length));
		System.out.println("AscII: Hex: " + ret51);//

		int[] test32 = new int[] { 255, 255, 255, 255, 240, 0, 0, 1, 240, 3, 0, 32, 0, 0, 1, 35, 0, 1 };
		String ret52 = Integer.toHexString(Encryption.crc16(test32, (short) test32.length));
		System.out.println("AscII: Hex(32): " + ret52);//

		// bcd8
		int[] test33 = new int[] { 255, 255, 255, 255, 240, 0, 0, 1, 240, 3, 0, 32, 0, 0, 1, 42, 0, 1 };
		String ret53 = Integer.toHexString(Encryption.crc16(test33, (short) test33.length));
		System.out.println("AscII: Hex(33): " + ret53);//

		int[] test34 = new int[] { 0x00, 0x00, 0x01, 0x29, 0xf0, 0x00, 0x00, 0x01, 0xf0, 0x03, 0x00, 0x20, 0x00, 0x00,
				0x01, 0x2e, 0x00, 0x01, 0x6a, 0x68, 0x68, 0x66, 0x66, 0x66, 0x6d, 0x6b, 0x20, 0x20, 0x20, 0x20};
		String ret54 = Integer.toHexString(Encryption.crc16(test34, (short) test34.length));
		System.out.println("AscII: Hex(test34): " + ret54);

		// 2df1
		byte[] test4 = new byte[] { (byte) 0xff, 0x00, 0x00, 0x10, (byte) 0xf0, 0x00, 0x00, 0x1, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0xff };
		String ret4 = Integer.toHexString(Encryption.getcrc16(test4, (short) test4.length));
		// System.out.println(
		// "Hex: " + ret4 + ",Dec: " + Integer.parseInt(ret4.substring(ret4.length() -
		// 4, ret4.length()), 16));//
		// 此生成的crc是c472
		// byte[] cData = new byte[] {(byte)
		// 0xf0,0x0,0x0,0x01,0x0,0x0,0x0,0x1,0x0,0x0,(byte)
		// 0xf0,0x3,0x12,0x34,0x56,0x78};

		// Setting data
		String strSettingData = "fffffffff0000001f00300200000011d00016a626e6a6868202020202020";
		byte[] byteSetting = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0, 0x00, 0x00,
				0x01, (byte) 0xf0, 0x03, 0x00, 0x20, 0x00, 0x00, 0x01, 0x1d, 0x00, 0x01, 0x6a, 0x62, 0x6e, 0x6a, 68, 68,
				20, 20, 20, 20, 20, 20 };
		// System.out.println(byteSetting.length);
		// System.out.println(Integer.toHexString(Encryption.getcrc16(byteSetting,
		// byteSetting.length)));
		// 此crc是6a62
		byte[] testByte = new byte[] { -1, -1, -1, -1, -16, 0, 0, 1, -16, 3, 0, 32, 0, 0, 1, 29, 0, 1, 106, 98, 110,
				106, 104, 104, 32, 32, 32, 32, 32, 32 };

		// System.out.println(Encryption.getcrc16(testByte, testByte.length));
		// System.out.println(Arrays.toString(strSettingData.getBytes()));
		// System.out.println(Arrays.toString(Utils.unpack0(strSettingData.getBytes())));
		short crc = Encryption.getcrc16(javax.xml.bind.DatatypeConverter.parseHexBinary(strSettingData),
				javax.xml.bind.DatatypeConverter.parseHexBinary(strSettingData).length);
		// System.out.println(crc);
		String crcStr = Integer.toHexString(crc);
		// if(crcStr.length() >= 4)
		// System.out.println(crcStr.substring(crcStr.length() - 4, crcStr.length()));
		// else
		// System.out.println(crcStr);

		byte[] aaaa = new byte[] { 0, 0, 0, 1 };
		byte[] b = new byte[4];
		// System.out.println(aaaa[0] + "," + aaaa[1] + "," + aaaa[2] + "," + aaaa[3]);
		// System.arraycopy(aaaa, 0, b, 0, 4);
		// System.out.println(b[0] + "," + b[1] + "," + b[2] + "," + b[3]);
		int ret = ByteBuffer.wrap(b).order(java.nio.ByteOrder.BIG_ENDIAN).getInt();
		// System.out.println(ret);
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
