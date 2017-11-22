package com.innjoo.halo.process;

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
		int accountId = Utils.byte2Int(byteAccountId);

		byte[] byteRoleId = new byte[2];
		short roleId = Utils.byte2Short(byteRoleId);

		byte[] byteHaloId = new byte[12];
		System.arraycopy(byteHaloId, 0, in, 6, 12);

		// 根据accountId查询数据库
		HaloChild hc = new HaloChild();
		hc = haloChildService.queryHaloChildByAccountId(accountId);
		if(hc != null) {
			//根据accountId更新haloId和roleId
			haloChildService.updateHaloIdAndRoleIdByAccountId(accountId, Arrays.toString(byteHaloId), (byte)roleId);
			
			//构造回送给客户端的报文
//				applib_time_struct  halo_time; 
//				unsigned short language;
//				unsigned short temp;
//				unsigned short drinkVol; 
//				unsigned short roleId;
//				applib_time_struct sleepTime_start;
//				applib_time_struct sleepTime_stop;
//				applib_time_struct learnTime_start;
//				applib_time_struct learnTime_stop;
			
			byte[] localTime = ProcComm.getLocalTime(hc);
			
			//Sleep time start
			short language = hc.getDevicelanguage();
			byte[] byteLanguage = Utils.short2Byte(language);
			
			//这个40度是原有代码中，不知道为什么是40度？
			short temp = 40;
			byte[] byteTemp = Utils.short2Byte(temp);
			
			//喝水的量
			short drinkVol = hc.getTarget().shortValue();
			byte[] byteDrinkVol = Utils.short2Byte(drinkVol);
			
			short petId;
			
			if(hc.getPetid() == 0)
				petId = roleId;
			else
				petId = hc.getPetid();			
			byte[] bytePetId = Utils.short2Byte(petId);
			
			byte[] sleepTimeStart = ProcComm.makeSleepTimeStartResp(hc);
			byte[] sleepTimeEnd = ProcComm.makeSleepTimeEndResp(hc);
			byte[] schoolTimeStart = ProcComm.makeSchoolTimeStartResp(hc);
			byte[] schoolTimeEnd = ProcComm.makeSchooTimeEndResp(hc);
			
			out = new byte[48];
			
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
			
		}
	}
}
