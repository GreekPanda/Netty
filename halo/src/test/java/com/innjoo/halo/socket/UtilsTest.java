package com.innjoo.halo.socket;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.innjoo.halo.utils.Utils;

public class UtilsTest {

	public static void main(String[] args) {
		byte[] data = new byte[100];

		for (int i = 0; i < 100; i++) {
			data[i] = (byte) i;

		}
		// System.out.println(Utils.bytesToHexString(data));

		UtilsTest ut = new UtilsTest();
		byte[] t = new byte[10];
		ut.testByteIsChanged(t);
		// System.out.println(Utils.bytesToHexString(t));

		Proto p = new Proto();
		p = ut.makeProto((short) 123);
		// System.out.println(p.toString());

		String s1 = "2017-11-10 11:33:11";
		byte[] b = new byte[8];
		short year = Short.parseShort(s1.split(" ")[0].split("-")[0]);
		byte mon = Byte.parseByte(s1.split(" ")[0].split("-")[1]);
		byte day = Byte.parseByte(s1.split(" ")[0].split("-")[2]);
		byte hour = Byte.parseByte(s1.split(" ")[1].split(":")[0]);
		byte min = Byte.parseByte(s1.split(" ")[1].split(":")[1]);
		byte sec = Byte.parseByte(s1.split(" ")[1].split(":")[2]);
		// b[1] = (byte) ((Short.parseShort(s1.split(" ")[0].split("-")[0])) | 0xFF);
		//System.out.println(year);
		// b = shortToByte(year);
		// short year16 = Integer.toHexString(year);
		year = 417;
		b[0] = (byte) (year & 0xff);

		b[1] = (byte) ((year >> 8) & 0xff);
		b[2] = mon;
		b[3] = day;
		b[4] = hour;
		b[5] = min;
		b[6] = sec;
		
//		byte[] brr = new byte[]{(byte)(year & 0x00FF),(byte)((year & 0xFF00)>>8)};
//		
//		byte[] arr = new byte[]{(byte)((year >> 8) & 0xFF),( byte )( year & 0xFF)};

		//System.out.println("year: " + arr[0] + ""+ arr[1]);
		
		//System.out.println(ut.getWeek() - 1);
		
		
		//System.out.println(Arrays.toString(ut.varByte()) + ", " + "length: " + ut.varByte().length);
		
		ut.testTwoBytes();
	}

	private boolean testByteIsChanged(byte[] in) {

		byte[] hello = "12345".getBytes();
		System.arraycopy(hello, 0, in, 0, hello.length);

		return true;

	}

	private int getWeek() {
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		return weekday;
	}

	private Proto makeProto(short ctrlCode) {
		// private byte[] headerId; //8 byte
		// private short package_len;//2 byte
		// private short opType; // 2 byte
		// private short res;
		// private byte[] data; // variable

		byte[] headerId = "halohalo".getBytes();
		short package_len = 20;
		short opType = ctrlCode;
		// short res = 0;
		byte[] data = "123456".getBytes();

		Proto p = new Proto(headerId, package_len, opType, data);

		return p;

	}

	public static byte[] shortToByte(short number) {
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}
	
	public byte[] varByte() {
		byte[] data = new byte[] {};
		
		System.out.println("==> " + data.length);
		
		byte[] tmp = new byte[10];
		for(int i = 0; i < 10; ++i)
			tmp[i] = (byte) i;
		
		data = new byte[tmp.length];
		
		System.arraycopy(tmp, 0, data, 0, data.length);
		
		return data;
	}
	
	private void testTwoBytes() {
		byte[] out = new byte[4];
		
		byte[] b1 = new byte[] {1, 0};
		byte[] b2 = new byte[] {3, 0};		
		
		System.arraycopy(b1, 0, out, 0, 2);
		System.arraycopy(b2, 0, out, 2, 2);
		
		System.out.println(Arrays.toString(out));
	}

}
