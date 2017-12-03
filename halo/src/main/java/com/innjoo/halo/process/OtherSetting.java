package com.innjoo.halo.process;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.model.HaloChild;
import com.innjoo.halo.service.IHaloChildService;
import com.innjoo.halo.utils.Utils;

public class OtherSetting {

	private static final Logger LOG = Logger.getLogger(OtherSetting.class);

	private final static IHaloChildService haloChildService = (IHaloChildService) AppCtx.getInstance()
			.getBean("haloChildService");

	public static void proc(byte[] in, byte[] out) {

		// 报文格式：Naccount_id/nroleId/a12halo_id，一共18个字节
		byte[] byteAccountId = new byte[4];
		System.arraycopy(in, 0, byteAccountId, 0, 4);
		int accountId = ByteBuffer.wrap(byteAccountId).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

		byte[] byteRoleId = new byte[2];
		System.arraycopy(in, 4, byteRoleId, 0, 2);
		int roleId = ByteBuffer.wrap(byteRoleId).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();

		byte[] byteHaloId = new byte[12];
		System.arraycopy(in, 6, byteHaloId, 0, 12);
		String haloId = new String(byteHaloId);

		// 根据accountId查询数据库
		HaloChild hc = new HaloChild();
		hc = haloChildService.selectByPrimaryKey(accountId);
		if (hc != null) {
			// 根据accountId更新haloId和roleId			
			haloChildService.updateHaloIdAndRoleIdByAccountId(hc);

			byte[] localTime = ProcComm.getLocalTime();

			short language = hc.getDevicelanguage().shortValue();
			byte[] byteLanguage = Utils.short2Byte(language);

			// 这个40度是原有代码中，不知道为什么是40度？
			short temp = 40;
			byte[] byteTemp = Utils.short2Byte(temp);

			// 喝水的量
			short drinkVol = hc.getTarget().shortValue();
			byte[] byteDrinkVol = Utils.short2Byte(drinkVol);

			short petId;

			if (hc.getPetid() == 0)
				petId = (short) roleId;
			else
				petId = (short) hc.getPetid().shortValue();

			byte[] bytePetId = Utils.short2Byte(petId);
			byte[] sleepTimeStart = ProcComm.makeSleepTimeStartResp(hc);
			byte[] sleepTimeEnd = ProcComm.makeSleepTimeEndResp(hc);
			byte[] schoolTimeStart = ProcComm.makeSchoolTimeStartResp(hc);
			byte[] schoolTimeEnd = ProcComm.makeSchooTimeEndResp(hc);

			System.arraycopy(localTime, 0, out, 0, 8);
			System.arraycopy(byteLanguage, 0, out, 8, 2);
			System.arraycopy(byteTemp, 0, out, 10, 2);
			System.arraycopy(byteDrinkVol, 0, out, 12, 2);
			System.arraycopy(bytePetId, 0, out, 14, 2);
			System.arraycopy(sleepTimeStart, 0, out, 16, 8);
			System.arraycopy(sleepTimeEnd, 0, out, 24, 8);
			System.arraycopy(schoolTimeStart, 0, out, 32, 8);
			System.arraycopy(schoolTimeEnd, 0, out, 40, 8);

		} else {
			return;
		}
	}
}
