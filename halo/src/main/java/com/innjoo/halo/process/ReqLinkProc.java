package com.innjoo.halo.process;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.ctx.PropCtx;
import com.innjoo.halo.model.HaloChild;
import com.innjoo.halo.model.HaloFriends;
import com.innjoo.halo.proto.ResultData;
import com.innjoo.halo.service.IHaloChildService;
import com.innjoo.halo.service.IHaloFriendsService;

/*
 * @Autho:liaohuanghe
 * @date:2017-11-9
 * 处理请求类型为0x02时的消息
 * */
public class ReqLinkProc {

	public static final Logger LOG = Logger.getLogger(ReqLinkProc.class);

	private final static IHaloChildService haloChildService = (IHaloChildService) AppCtx.getInstance()
			.getBean("haloChildService");

	private final static IHaloFriendsService haloFriendsService = (IHaloFriendsService) AppCtx.getInstance()
			.getBean("haloFriendsService");

	// 构造回送报文的数据内容
	public static byte[] sendResponseData(HaloChild hc, short needUpdateSize) {
		byte[] out = new byte[] {};

		if (needUpdateSize > 0) {
			out = new byte[42];
			// 报文格式，前面两个short的分别是1和3，貌似固定值
			// nnumber1(1)/nnumber2(3)/a7sleep_start/a7sleep_end/a7school_start/a7school_end/nneedDrinkVol/a8localtime
			byte[] b1 = new byte[] { 1, 0 };
			System.arraycopy(b1, 0, out, 0, 2);

			byte[] b2 = new byte[] { 3, 0 };
			System.arraycopy(b2, 0, out, 2, 2);

			byte[] sleepStart = ProcComm.makeSleepTimeStartResp(hc);
			System.arraycopy(sleepStart, 0, out, 4, 7);

			byte[] sleepEnd = ProcComm.makeSleepTimeEndResp(hc);
			System.arraycopy(sleepEnd, 0, out, 11, 7);

			byte[] schoolStart = ProcComm.makeSchoolTimeStartResp(hc);
			System.arraycopy(schoolStart, 0, out, 18, 7);

			byte[] schoolEnd = ProcComm.makeSchooTimeEndResp(hc);
			System.arraycopy(schoolEnd, 0, out, 25, 7);

			byte[] needDrinkVol = new byte[2];
			System.arraycopy(needDrinkVol, 0, out, 32, 2);

			byte[] localTime = new byte[8];
			localTime = ProcComm.getLocalTime();
			System.arraycopy(localTime, 0, out, 34, 8);

		} else {
			// 与上过一个版本中缺少了前面的两个short字段1和3
			// a7sleep_start/a7sleep_end/a7school_start/a7school_end/nneedDrinkVol/a8localtime
			out = new byte[38];

			byte[] sleepStart = ProcComm.makeSleepTimeStartResp(hc);
			System.arraycopy(sleepStart, 0, out, 0, 7);

			byte[] sleepEnd = ProcComm.makeSleepTimeEndResp(hc);
			System.arraycopy(sleepEnd, 0, out, 7, 7);

			byte[] schoolStart = ProcComm.makeSchoolTimeStartResp(hc);
			System.arraycopy(schoolStart, 0, out, 14, 7);

			byte[] schoolEnd = ProcComm.makeSchooTimeEndResp(hc);
			System.arraycopy(schoolEnd, 0, out, 21, 7);

			byte[] byteNeedDrinkVol = new byte[2];
			System.arraycopy(byteNeedDrinkVol, 0, out, 28, 2);

			byte[] localTime = new byte[8];
			localTime = ProcComm.getLocalTime();
			System.arraycopy(localTime, 0, out, 30, 8);
		}

		return out;

	}

	// 获取是否需要升级
	public static short needUpdateSize(short needUpdateSize) {
		return needUpdateSize;
	}

	// 先判断传入水杯账号是否存在
	public static boolean isAccountExisted(int id) {
		boolean isExisted = true;

		HaloChild hc = new HaloChild();
		try {
			hc = haloChildService.selectByPrimaryKey(id);
			if (hc != null)
				isExisted = true;
			else
				isExisted = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isExisted;
	}

	public static void update(HaloChild hc) {
		if (hc == null)
			return;
		haloChildService.updateInfoByPrimaryKey(hc);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void parseDataFromClientPackage(byte[] in, HaloChild hc, ResultData rd)
			throws UnsupportedEncodingException {
		// 报文格式，此格式从原来的php中摘取，是一个字符串类型
		/*
		 * a12halo_id/Naccount_id/nroleId/a16device_version
		 * /a16game_version/ncurDrinkLevel/ncurDrinkVol
		 * /C30universerId/CcurUniverserLevel/CcurUniverserId
		 * /nYear/CnMonth/CnDay/CnHour/CnMin/CnSec/CDayIndex
		 * /ndeviceLanguage/nneedUpdateDataSize/Nfriend1
		 * /Nfriend2/Nfriend3/Nfriend4/Nfriend5';
		 * 
		 */

		// !!!注意从in获取的数据全部都是string型，内部包含的都是ASCII，所以直接使用new string(byte[])才能正确的获取数值
		byte[] haloId;
		short roleId;
		short curDrinkLevel;
		short curDrinkVol;
		byte[] universerId;
		byte curUniverserLevel;
		byte curUniverserId;
		short deviceLanuage;
		short needUpdateDataSize;
		StringBuilder exploredStarId = new StringBuilder();
		try {
			haloId = new byte[12];
			// 0~11
			System.arraycopy(in, 0, haloId, 0, 12);
			// System.out.println(new String(haloId));

			int accountId = 0;
			// 2.accountId 从12~15
			byte[] byteAccountId = new byte[4];
			System.arraycopy(in, 12, byteAccountId, 0, 4);
			// 小端
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				accountId = ByteBuffer.wrap(byteAccountId).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
			else
				accountId = ByteBuffer.wrap(byteAccountId).order(java.nio.ByteOrder.BIG_ENDIAN).getInt();
			// 大端java.nio.ByteBuffer.wrap(bytes).getInt();

			// 3.ckpnum 16~17
			byte[] byteRoleId = new byte[2];
			System.arraycopy(in, 16, byteRoleId, 0, 2);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				roleId = ByteBuffer.wrap(byteAccountId).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();
			else
				roleId = ByteBuffer.wrap(byteAccountId).order(java.nio.ByteOrder.BIG_ENDIAN).getShort();

			// 4.device_version，从第20个字节获取，长度为16
			byte[] byteDeviceVersion = new byte[16];
			System.arraycopy(in, 18, byteDeviceVersion, 0, 15);
			// System.out.println(new String(byteDeviceVersion));

			// 5.GameVersion，从第36字节开始，长度为16
			byte[] byteGameVersion = new byte[16];
			System.arraycopy(in, 34, byteGameVersion, 0, 15);
			System.out.println(new String(byteGameVersion));

			// 6.DrinkLevel,从50字节开始，长度为2
			byte[] byteCurDrinkLevel = new byte[2];
			System.arraycopy(in, 50, byteCurDrinkLevel, 0, 2);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				curDrinkLevel = ByteBuffer.wrap(byteCurDrinkLevel).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();
			else
				curDrinkLevel = ByteBuffer.wrap(byteCurDrinkLevel).order(java.nio.ByteOrder.BIG_ENDIAN).getShort();

			// 7.dayNeedDrinkVol，从52字节开始，长度为2
			byte[] byteCurDrinkVol = new byte[2];
			System.arraycopy(in, 52, byteCurDrinkVol, 0, 2);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				curDrinkVol = ByteBuffer.wrap(byteCurDrinkVol).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();
			else
				curDrinkVol = ByteBuffer.wrap(byteCurDrinkVol).order(java.nio.ByteOrder.BIG_ENDIAN).getShort();

			/*
			 * a12halo_id/Naccount_id/nroleId/a16device_version
			 * /a16game_version/ncurDrinkLevel/ncurDrinkVol
			 * /C30universerId/CcurUniverserLevel/CcurUniverserId
			 * /nYear/CnMonth/CnDay/CnHour/CnMin/CnSec/CDayIndex
			 * /ndeviceLanguage/nneedUpdateDataSize/Nfriend1
			 * /Nfriend2/Nfriend3/Nfriend4/Nfriend5';
			 * 
			 */

			universerId = new byte[30];
			System.arraycopy(in, 54, universerId, 0, 30);
			for (int i = 0; i < 29; ++i) {
				exploredStarId.append(Integer.toHexString(universerId[i]) + ",");
			}
			exploredStarId.append(Integer.toHexString(universerId[29]));
			// System.out.println("exploredStarId : " + exploredStarId.toString());

			// 9.curUniverserLeveL，从84自己开始，长度为一个字节
			byte[] byteCurUniverserLevel = new byte[1];
			System.arraycopy(in, 84, byteCurUniverserLevel, 0, 1);
			curUniverserLevel = byteCurUniverserLevel[0];

			// 10.curUniverserId，从85字节开始，长度为一个字节
			byte[] byteCurUniverserId = new byte[1];
			System.arraycopy(in, 85, byteCurUniverserId, 0, 1);
			curUniverserId = byteCurUniverserId[0];

			// 11.byteYear,从86字节开始，长度为一个字节
			short year = 0;
			byte[] byteYear = new byte[2];
			System.arraycopy(in, 86, byteYear, 0, 2);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				year = ByteBuffer.wrap(byteYear).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();
			else
				year = ByteBuffer.wrap(byteYear).order(java.nio.ByteOrder.BIG_ENDIAN).getShort();
			// 12.byteMonth,从88字节开始，长度为一个字节
			byte[] byteMonth = new byte[1];
			System.arraycopy(in, 88, byteMonth, 0, 1);
			byte month = byteMonth[0];

			// 13.byteDay,从89字节开始，长度为一个字节
			byte[] byteDay = new byte[1];
			System.arraycopy(in, 89, byteDay, 0, 1);
			byte day = byteDay[0];

			/*
			 * a12halo_id/Naccount_id/nroleId/a16device_version
			 * /a16game_version/ncurDrinkLevel/ncurDrinkVol
			 * /C30universerId/CcurUniverserLevel/CcurUniverserId
			 * /nYear/CnMonth/CnDay/CnHour/CnMin/CnSec/CDayIndex
			 * /ndeviceLanguage/nneedUpdateDataSize/Nfriend1
			 * /Nfriend2/Nfriend3/Nfriend4/Nfriend5';
			 * 
			 */

			// 14.byteHour,从90字节开始，长度为一个字节
			byte[] byteHour = new byte[1];
			System.arraycopy(in, 90, byteHour, 0, 1);
			byte hour = byteHour[0];

			// 15.byteMinute,从91字节开始，长度为一个字节
			byte[] byteMinute = new byte[1];
			System.arraycopy(in, 91, byteMinute, 0, 1);
			byte min = byteMinute[0];

			// 16.byteSec,从92字节开始，长度为一个字节
			byte[] byteSec = new byte[1];
			System.arraycopy(in, 92, byteSec, 0, 1);
			byte sec = byteSec[0];

			// 17.DayIndex，从93字节开始，长度为一个字节
			byte[] byteWeekIndex = new byte[1];
			System.arraycopy(in, 93, byteWeekIndex, 0, 1);
			byte weekIndex = byteWeekIndex[0];

			// 18.deviceLanguage，从94字节开始，长度为一个字节
			byte[] byteDeviceLanuage = new byte[2];
			System.arraycopy(in, 94, byteDeviceLanuage, 0, 2);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				deviceLanuage = ByteBuffer.wrap(byteDeviceLanuage).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();
			else
				deviceLanuage = ByteBuffer.wrap(byteDeviceLanuage).order(java.nio.ByteOrder.BIG_ENDIAN).getShort();

			// 19. needUpdateDataSize，96字节开始，长度为4个字节
			byte[] byteNeedUpdateDataSize = new byte[2];
			System.arraycopy(in, 96, byteNeedUpdateDataSize, 0, 2);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				needUpdateDataSize = ByteBuffer.wrap(byteNeedUpdateDataSize).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();
			else
				needUpdateDataSize = ByteBuffer.wrap(byteNeedUpdateDataSize).order(java.nio.ByteOrder.BIG_ENDIAN).getShort();

			// 20. friend1，98字节开始，长度为4个字节
			int friend1 = 0;
			int friend2 = 0;
			int friend3 = 0;
			int friend4 = 0;
			int friend5 = 0;
			byte[] byteFriend1 = new byte[4];
			System.arraycopy(in, 98, byteFriend1, 0, 4);		
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				friend1 = ByteBuffer.wrap(byteFriend1).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
			else
				friend1 = ByteBuffer.wrap(byteFriend1).order(java.nio.ByteOrder.BIG_ENDIAN).getInt();

			// 21. friend2，102字节开始，长度为4个字节
			byte[] byteFriend2 = new byte[4];
			System.arraycopy(in, 102, byteFriend2, 0, 4);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				friend2 = ByteBuffer.wrap(byteFriend2).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
			else
				friend2 = ByteBuffer.wrap(byteFriend2).order(java.nio.ByteOrder.BIG_ENDIAN).getInt();

			// 22. friend3，106字节开始，长度为4个字节
			byte[] byteFriend3 = new byte[4];
			System.arraycopy(in, 106, byteFriend3, 0, 4);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				friend3 = ByteBuffer.wrap(byteFriend3).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();
			else
				friend3 = ByteBuffer.wrap(byteFriend3).order(java.nio.ByteOrder.BIG_ENDIAN).getShort();
			// 23. friend4，116字节开始，长度为4个字节
			byte[] byteFriend4 = new byte[4];
			System.arraycopy(in, 110, byteFriend4, 0, 4);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				friend4 = ByteBuffer.wrap(byteFriend4).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
			else
				friend4 = ByteBuffer.wrap(byteFriend4).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

			// 24. friend5，120字节开始，长度为4个字节
			byte[] byteFriend5 = new byte[4];
			System.arraycopy(in, 114, byteFriend5, 0, 4);
			if (PropCtx.getPropInstance().getProperty("host.endian").equals("little"))
				friend5 = ByteBuffer.wrap(byteFriend5).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
			else
				friend5 = ByteBuffer.wrap(byteFriend5).order(java.nio.ByteOrder.BIG_ENDIAN).getInt();

			hc.setId(accountId);
			hc.setHaloId(new String(haloId));
			hc.setExploredStarId(exploredStarId.toString());
			hc.setPetid((int) roleId);
			hc.setLevel((int) curDrinkLevel);
			hc.setCurDrinkWater((int) curDrinkVol);
			hc.setStarId((int) curUniverserId);
			hc.setStarLevel((int) curUniverserLevel);
			hc.setDevicelanguage((int) deviceLanuage);

			LOG.info("控制码为2时客户端数据内容： " + hc.toString());

			rd.setNeedUpdateSize(needUpdateDataSize);

			// List<Integer, Integer> listFriends = Lists.newArrayList();
			HashMap[] mapFriends = new HashMap[5];
			// Map<String, Integer> mapFriends[] = new HashMap<String, Integer>;
			mapFriends[0] = new HashMap<String, Integer>();
			mapFriends[0].put("kid", accountId);
			mapFriends[0].put("fKid", friend1);

			mapFriends[1] = new HashMap<String, Integer>();
			mapFriends[1].put("kid", accountId);
			mapFriends[1].put("fKid", friend2);

			mapFriends[2] = new HashMap<String, Integer>();
			mapFriends[2].put("kid", accountId);
			mapFriends[2].put("fKid", friend3);

			mapFriends[3] = new HashMap<String, Integer>();
			mapFriends[3].put("kid", accountId);
			mapFriends[3].put("fKid", friend4);

			mapFriends[4] = new HashMap<String, Integer>();
			mapFriends[4].put("kid", accountId);
			mapFriends[4].put("fKid", friend5);

			for (int i = 0; i < 5; ++i) {
				HaloFriends hfs = new HaloFriends();
				hfs = haloFriendsService.selectByKid(mapFriends[i]);
				if (hfs == null)
					haloFriendsService.insertWithKid(mapFriends[i]);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

}
