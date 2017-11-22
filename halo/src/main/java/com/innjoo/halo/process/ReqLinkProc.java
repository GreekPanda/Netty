package com.innjoo.halo.process;

import java.util.List;

import org.apache.log4j.Logger;

import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.model.HaloChild;
import com.innjoo.halo.service.IHaloChildService;
import com.innjoo.halo.utils.DateUtils;

/*
 * @Autho:liaohuanghe
 * @date:2017-11-9
 * 处理请求类型为0x02时的消息
 * */
public class ReqLinkProc {

	public static final Logger LOG = Logger.getLogger(ReqLinkProc.class);

	private final static IHaloChildService haloChildService = (IHaloChildService) AppCtx.getInstance()
			.getBean("haloChildService");

	// 构造回送报文的数据内容
	public static void sendResponseData(HaloChild hc, short needUpdateSize, byte[] out) {

		if (needUpdateSize > 0) {
			out = new byte[40];
			// 报文格式，前面两个short的分别是1和3，貌似固定值
			// nnumber1(1)/nnumber2(3)/a7sleep_start/a7sleep_end/a7school_start/a7school_end/nneedDrinkVol/a8localtime
			// TODO:现在核心的问题就是short进入byte[]有问题
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
			localTime = ProcComm.getLocalTime(hc);
			System.arraycopy(localTime, 0, out, 34, 8);

		} else {
			// 与上过一个版本中缺少了前面的两个short字段1和3
			// a7sleep_start/a7sleep_end/a7school_start/a7school_end/nneedDrinkVol/a8localtime
			// TODO:现在核心的问题就是short进入byte[]有问题
			out = new byte[38];

			byte[] sleepStart = ProcComm.makeSleepTimeStartResp(hc);
			System.arraycopy(sleepStart, 0, out, 0, 7);

			byte[] sleepEnd = ProcComm.makeSleepTimeEndResp(hc);
			System.arraycopy(sleepEnd, 0, out, 7, 7);

			byte[] schoolStart = ProcComm.makeSchoolTimeStartResp(hc);
			System.arraycopy(schoolStart, 0, out, 14, 7);

			byte[] schoolEnd = ProcComm.makeSchooTimeEndResp(hc);
			System.arraycopy(schoolEnd, 0, out, 21, 7);

			// TODO:short转byte，两个字节
			byte[] byteNeedDrinkVol = new byte[2];
			System.arraycopy(byteNeedDrinkVol, 0, out, 28, 2);

			byte[] localTime = new byte[8];
			localTime = ProcComm.getLocalTime(hc);
			System.arraycopy(localTime, 0, out, 30, 8);
		}

	}

	// // 获取所有的时间格式比如睡觉开始结束、上学开始结束的时间
	// private static void getAllTimeFormat(HaloChild hc, byte[] sleepStartTime,
	// byte[] sleepEndTime,
	// byte[] schoolStartTime, byte[] schoolEndTime) {
	// // HaloChild hc = haloChildService.queryHaloChildByAccountId(haloId);
	// if (hc != null) {
	// makeSleepTimeStartResp(hc, sleepStartTime);
	// makeSleepTimeEndResp(hc, sleepEndTime);
	// makeSchoolTimeStartResp(hc, schoolStartTime);
	// makeSchooTimeEndResp(hc, schoolEndTime);
	// }
	//
	// }

	// 获取是否需要升级
	public static short needUpdateSize(short needUpdateSize) {
		return needUpdateSize;
	}

	// 先判断传入水杯账号是否存在
	public static boolean isAccountExisted(String haloId) {
		boolean isExisted = true;

		List<HaloChild> listhc;
		try {
			listhc = (List<HaloChild>) haloChildService.selectByHaloId(haloId);
			if (listhc != null && !listhc.isEmpty())
				isExisted = true;
			else
				isExisted = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isExisted;
	}

	public static void update(byte[] haloId, HaloChild hc) {
		if (haloId == null || haloId.length <= 0)
			return;
		haloChildService.updateByPrimaryKey(hc);

	}

}
