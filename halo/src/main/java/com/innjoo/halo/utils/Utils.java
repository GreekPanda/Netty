package com.innjoo.halo.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class Utils {

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(" " + hv);
		}
		return stringBuilder.toString();
	}

	public static void printHexString(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase());
		}

	}

	// Byte to int, short and short to byte ..
	/**
	 * 将short转成byte[2]
	 * 
	 * @param a
	 * @return
	 */
	public static byte[] short2Byte(short a) {
		byte[] b = new byte[2];

		b[0] = (byte) (a >> 8);
		b[1] = (byte) (a);

		return b;
	}

	/**
	 * 将short转成byte[2]
	 * 
	 * @param a
	 * @param b
	 * @param offset
	 *            b中的偏移量
	 */
	public static void short2Byte(short a, byte[] b, int offset) {
		b[offset] = (byte) (a >> 8);
		b[offset + 1] = (byte) (a);
	}

	/**
	 * 将byte[2]转换成short
	 * 
	 * @param b
	 * @return
	 */
	public static short byte2Short(byte[] b) {
		return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
	}

	/**
	 * 将byte[2]转换成short
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static short byte2Short(byte[] b, int offset) {
		return (short) (((b[offset] & 0xff) << 8) | (b[offset + 1] & 0xff));
	}

	/**
	 * long转byte[8]
	 * 
	 * @param a
	 * @param b
	 * @param offset
	 *            b的偏移量
	 */
	public static void long2Byte(long a, byte[] b, int offset) {
		b[offset + 0] = (byte) (a >> 56);
		b[offset + 1] = (byte) (a >> 48);
		b[offset + 2] = (byte) (a >> 40);
		b[offset + 3] = (byte) (a >> 32);

		b[offset + 4] = (byte) (a >> 24);
		b[offset + 5] = (byte) (a >> 16);
		b[offset + 6] = (byte) (a >> 8);
		b[offset + 7] = (byte) (a);
	}

	/**
	 * byte[8]转long
	 * 
	 * @param b
	 * @param offset
	 *            b的偏移量
	 * @return
	 */
	public static long byte2Long(byte[] b, int offset) {
		return ((((long) b[offset + 0] & 0xff) << 56) | (((long) b[offset + 1] & 0xff) << 48)
				| (((long) b[offset + 2] & 0xff) << 40) | (((long) b[offset + 3] & 0xff) << 32)

				| (((long) b[offset + 4] & 0xff) << 24) | (((long) b[offset + 5] & 0xff) << 16)
				| (((long) b[offset + 6] & 0xff) << 8) | (((long) b[offset + 7] & 0xff) << 0));
	}

	/**
	 * byte[8]转long
	 * 
	 * @param b
	 * @return
	 */
	public static long byte2Long(byte[] b) {
		return ((b[0] & 0xff) << 56) | ((b[1] & 0xff) << 48) | ((b[2] & 0xff) << 40) | ((b[3] & 0xff) << 32) |

				((b[4] & 0xff) << 24) | ((b[5] & 0xff) << 16) | ((b[6] & 0xff) << 8) | (b[7] & 0xff);
	}

	/**
	 * long转byte[8]
	 * 
	 * @param a
	 * @return
	 */
	public static byte[] long2Byte(long a) {
		byte[] b = new byte[4 * 2];

		b[0] = (byte) (a >> 56);
		b[1] = (byte) (a >> 48);
		b[2] = (byte) (a >> 40);
		b[3] = (byte) (a >> 32);

		b[4] = (byte) (a >> 24);
		b[5] = (byte) (a >> 16);
		b[6] = (byte) (a >> 8);
		b[7] = (byte) (a >> 0);

		return b;
	}

	/**
	 * byte数组转int
	 * 
	 * @param b
	 * @return
	 */
	public static int byte2Int(byte[] b) {
		return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
	}

	/**
	 * byte数组转int
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static int byte2Int(byte[] b, int offset) {
		return ((b[offset++] & 0xff) << 24) | ((b[offset++] & 0xff) << 16) | ((b[offset++] & 0xff) << 8)
				| (b[offset++] & 0xff);
	}

	/**
	 * int转byte数组
	 * 
	 * @param a
	 * @return
	 */
	public static byte[] int2Byte(int a) {
		byte[] b = new byte[4];
		b[0] = (byte) (a >> 24);
		b[1] = (byte) (a >> 16);
		b[2] = (byte) (a >> 8);
		b[3] = (byte) (a);

		return b;
	}

	public static int bytesToIntBig(byte[] src, int offset) {
	    int value;
	    value = (int) (((src[offset] & 0xFF) << 24)
	            | ((src[offset + 1] & 0xFF) << 16)
	            | ((src[offset + 2] & 0xFF) << 8)
	            | (src[offset + 3] & 0xFF));
	    return value;
	}
	
	public static int bytesToIntLittle(byte[] src, int offset) {
	    int value;
	    value = (int) ((src[offset] & 0xFF)
	            | ((src[offset + 1] & 0xFF) << 8)
	            | ((src[offset + 2] & 0xFF) << 16)
	            | ((src[offset + 3] & 0xFF) << 24));
	    return value;
	}
	
	public static short bytesToShortBig(byte[] src, int offset) {
	    short value;
	    value = (short) (((src[offset] & 0xFF) << 8)
	            | (src[offset + 1] & 0xFF));
	    return value;
	}
	
	public static short bytesToShortLittle(byte[] src, int offset) {
	    short value;
	    value = (short) ((src[offset] & 0xFF)
	            | ((src[offset + 1] & 0xFF) << 8));
	    return value;
	}

	
	/**
	 * int转byte数组
	 * 
	 * @param a
	 * @param b
	 * @param offset
	 * @return
	 */
	public static void int2Byte(int a, byte[] b, int offset) {
		b[offset++] = (byte) (a >> 24);
		b[offset++] = (byte) (a >> 16);
		b[offset++] = (byte) (a >> 8);
		b[offset++] = (byte) (a);
	}

	// char[]转byte[]

	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();

	}

	public static byte getByte(char c) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(1);
		cb.put(c);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		byte[] tmp = bb.array();
		return tmp[0];
	}

	// byte转char

	public static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);

		return cb.array();
	}

	// byte转char

	public static char getChar(byte bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(1);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);

		char[] tmp = cb.array();

		return tmp[0];
	}
}
