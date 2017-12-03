package com.innjoo.halo.process;

import java.util.Calendar;

import com.innjoo.halo.model.HaloChild;
import com.innjoo.halo.utils.DateUtils;
import com.innjoo.halo.utils.Utils;

/**
 * @author Cash Liao
 * @date 2017-11-13
 * 
 * 处理常用的接收数据的类，主要用于时间格式的处理
 * */


public class ProcComm {

	public static final byte SLEEP_START = 0x01;
	public static final byte SLEEP_END = 0x02;

	public static final byte SCHOOL_START = 0x03;
	public static final byte SCHOOL_END = 0x04;

	private static int weekIndex() {
		
		Calendar rightNow=Calendar.getInstance();  
        int day=rightNow.get(Calendar.DAY_OF_WEEK);
        return day;
	}
	
	// 获取本地时间，需要将年月日时分秒都填入到字节流中
	public static byte[] getLocalTime() {

		byte[] localTime = new byte[8];
		String dateTime = DateUtils.getDateTime();
		short year = Short.parseShort(dateTime.split(" ")[0].split("-")[0]);
		byte[] byteYear = new byte[2];
		byteYear = Utils.short2Byte(year);
		
		byte mon = Byte.parseByte(dateTime.split(" ")[0].split("-")[1]);
		byte day = Byte.parseByte(dateTime.split(" ")[0].split("-")[2]);
		byte hour = Byte.parseByte(dateTime.split(" ")[1].split(":")[0]);
		byte min = Byte.parseByte(dateTime.split(" ")[1].split(":")[1]);
		byte sec = Byte.parseByte(dateTime.split(" ")[1].split(":")[2]);

		System.arraycopy(byteYear, 0, localTime, 0, 2);
		localTime[2] = mon;
		localTime[3] = day;
		localTime[4] = hour;
		localTime[5] = min;
		localTime[6] = sec;
		localTime[7] = (byte)weekIndex();

		return localTime;
	}

	// 获取喝水的目标值
	public static int getNeedDrinkVol(HaloChild hc) {
		if (hc == null)
			return 0;

		return hc.getTarget();
	}

	public static byte[] makeSleepTimeStartResp(HaloChild hc) {
		if (hc == null)
			return null;

		byte[] sleepStartTime = new byte[8];
		
		if (hc.getSleepTimeStart() != null) {			
			formatTime(sleepStartTime, hc, SLEEP_START, false);
		} else {			
			formatTime(sleepStartTime, hc, SLEEP_START, true);
		}

		return sleepStartTime;

	}

	public static byte[] makeSleepTimeEndResp(HaloChild hc) {
		if (hc == null)
			return null;
		
		byte[] sleepEndTime = new byte[8];
		
		if (hc.getSleepTimeEnd() != null) {
			// TODO:字符串中存放时间的格式，数据库中式0:0格式，所以可以使用“:”分开，获取sleep_time的小时和分钟
			formatTime(sleepEndTime, hc, SLEEP_END, false);
		} else {
			// 年、月、日、时、分、秒、星期均为0
			formatTime(sleepEndTime, hc, SLEEP_END, true);
		}

		return sleepEndTime;

	}

	public static byte[] makeSchoolTimeStartResp(HaloChild hc) {
		if (hc == null)
			return null;
		
		byte[] schoolStartTime = new byte[8];

		if (hc.getSchoolTimeStart() != null) {
			// TODO:字符串中存放时间的格式，数据库中式0:0格式，所以可以使用“:”分开，获取sleep_time的小时和分钟
			formatTime(schoolStartTime, hc, SCHOOL_START, false);
		} else {
			// 年、月、日、时、分、秒、星期均为0
			formatTime(schoolStartTime, hc, SLEEP_START, true);
		}

		return schoolStartTime;

	}

	public static byte[] makeSchooTimeEndResp(HaloChild hc) {
		if (hc == null)
			return null;
		
		byte[] schoolEndTime = new byte[8];

		if (hc.getSchoolTimeEnd() != null) {
			// TODO:字符串中存放时间的格式，数据库中式0:0格式，所以可以使用“:”分开，获取sleep_time的小时和分钟
			formatTime(schoolEndTime, hc, SCHOOL_END, false);
		} else {
			// 年、月、日、时、分、秒、星期均为0
			formatTime(schoolEndTime, hc, SCHOOL_END, true);
		}

		return schoolEndTime;

	}

	private static void formatTime(byte[] timeResp, HaloChild hc, byte flag, boolean isDefault) {

		// 年
		byte[] year = new byte[] { 00, 00 };
		System.arraycopy(year, 0, timeResp, 0, year.length);

		// 月
		byte[] month = new byte[] { 0 };
		System.arraycopy(month, 0, timeResp, 2, month.length);

		// 日
		byte[] day = new byte[] { 0 };
		System.arraycopy(day, 0, timeResp, 3, day.length);

		if (flag == SLEEP_START) {
			if (!isDefault) {
				// 时
				byte[] hour = new byte[1];
				hour = hc.getSleepTimeStart().split(":")[0].getBytes();
				System.arraycopy(hour, 0, timeResp, 4, hour.length);

				// 分
				byte[] min = hc.getSleepTimeStart().split(":")[1].getBytes();
				System.arraycopy(min, 0, timeResp, 5, min.length);
			} else {
				// 时
				byte[] hour = new byte[] { 0 };
				System.arraycopy(hour, 0, timeResp, 4, hour.length);

				// 分
				byte[] min = new byte[] { 0 };
				System.arraycopy(min, 0, timeResp, 5, min.length);
			}
		} else if (flag == SLEEP_END) {
			if (!isDefault) {
				// 时
				byte[] hour = hc.getSleepTimeEnd().split(":")[0].getBytes();
				System.arraycopy(hour, 0, timeResp, 4, hour.length);

				// 分
				byte[] min = hc.getSleepTimeEnd().split(":")[1].getBytes();
				System.arraycopy(min, 0, timeResp, 5, min.length);
			} else {
				// 时
				byte[] hour = new byte[] { 0 };
				System.arraycopy(hour, 0, timeResp, 4, hour.length);

				// 分
				byte[] min = new byte[] { 0 };
				System.arraycopy(min, 0, timeResp, 5, min.length);
			}
		} else if (flag == SCHOOL_START) {
			if (!isDefault) {
				// 时
				byte[] hour = hc.getSchoolTimeStart().split(":")[0].getBytes();
				System.arraycopy(hour, 0, timeResp, 4, hour.length);

				// 分
				byte[] min = hc.getSchoolTimeStart().split(":")[1].getBytes();
				System.arraycopy(min, 0, timeResp, 5, min.length);
			} else {
				// 时
				byte[] hour = new byte[] { 0 };
				System.arraycopy(hour, 0, timeResp, 4, hour.length);

				// 分
				byte[] min = new byte[] { 0 };
				System.arraycopy(min, 0, timeResp, 5, min.length);
			}
		} else if (flag == SCHOOL_END) {
			if (!isDefault) {
				// 时
				byte[] hour = hc.getSchoolTimeEnd().split(":")[0].getBytes();
				System.arraycopy(hour, 0, timeResp, 4, hour.length);

				// 分
				byte[] min = hc.getSchoolTimeEnd().split(":")[1].getBytes();
				System.arraycopy(min, 0, timeResp, 5, min.length);
			} else {
				// 时
				byte[] hour = new byte[] { 0 };
				System.arraycopy(hour, 0, timeResp, 4, hour.length);

				// 分
				byte[] min = new byte[] { 0 };
				System.arraycopy(min, 0, timeResp, 5, min.length);
			}
		}

		// 秒
		byte[] sec = new byte[] { 0 };
		System.arraycopy(sec, 0, timeResp, 6, sec.length);

		// 星期
		byte[] week = new byte[] { 0 };
		System.arraycopy(week, 0, timeResp, 7, week.length);

	}

}
