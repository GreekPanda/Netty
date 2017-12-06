package com.innjoo.halo.process;

import org.apache.log4j.Logger;

import com.innjoo.halo.ctx.PropCtx;
import com.innjoo.halo.model.HaloHourWater;
import com.innjoo.halo.utils.DateUtils;
import com.innjoo.halo.utils.Utils;

public class DrinkHistoryProc {

	private static final Logger log = Logger.getLogger(DrinkHistoryProc.class);

	public static void proc(int senderId, int package_len, byte[] in, byte[] out) {

		// 客户端就是这么生成的
		int count = (package_len - 14) / 10;
		if (count == 1) {
			// 将时间信息插入到数据库中
			byte[] byteYear = new byte[2];
			System.arraycopy(in, 0, byteYear, 0, 2);

			byte[] byteMonth = new byte[1];
			System.arraycopy(in, 2, byteMonth, 0, 1);

			byte[] byteDay = new byte[1];
			System.arraycopy(in, 3, byteDay, 0, 1);

			byte[] byteHour = new byte[1];
			System.arraycopy(in, 4, byteHour, 0, 1);
			byte hour = byteHour[0];

			byte[] byteMin = new byte[1];
			System.arraycopy(in, 5, byteMin, 0, 1);

			byte[] byteSec = new byte[1];
			System.arraycopy(in, 6, byteSec, 0, 1);

			byte[] byteWeek = new byte[1];
			System.arraycopy(in, 7, byteWeek, 0, 1);

			byte[] byteDrinkVol = new byte[2];
			System.arraycopy(in, 8, byteDrinkVol, 0, 2);
			short drinkVol = 0;
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				drinkVol = Utils.bytesToShortLittle(byteDrinkVol, 0);
			else
				drinkVol = Utils.bytesToShortBig(byteDrinkVol, 0);

			String strMinTime = String.format("%4d%02d%02d%02d%02d%", byteYear, byteMonth, byteDay, byteHour, byteMin);
			String strHourTime = String.format("%4d%02d%02d%02d%", byteYear, byteMonth, byteDay, byteHour);
			String strDayTime = String.format("%4d%02d%02d", byteYear, byteMonth, byteDay);

			HaloHourWater hw = new HaloHourWater();
			hw.setKid(senderId);
			hw.setDayTime(Integer.parseInt(strDayTime));
			hw.setTypeTime(Integer.parseInt(strMinTime));
			hw.setGroupId(escapeHourToGroupId(hour));
			hw.setUploadTime(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss"));

		} else {
			for (int i = 0; i < count; ++i) {
				// 数据中时间信息成为了一个数组，然后将所有的信息插入到数据库中
			}
		}
	}

	private static short escapeHourToGroupId(byte hour) {
		if (hour >= 0 && hour <= 8)
			return 10;
		else if (hour >= 9 && hour <= 10)
			return 11;
		else if (hour >= 11 && hour <= 12)
			return 12;
		else if (hour >= 13 && hour <= 14)
			return 13;
		else if (hour >= 15 && hour <= 16)
			return 14;
		else if (hour >= 17 && hour <= 18)
			return 15;
		else if (hour >= 19 && hour <= 23)
			return 16;
		else
			return 0xff;

	}
}
