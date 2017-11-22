package com.innjoo.halo.process;

import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.model.HaloChild;
import com.innjoo.halo.service.IHaloChildService;
import com.innjoo.halo.utils.Utils;

public class RecoverProc {
	
	
	private final static IHaloChildService haloChildService = (IHaloChildService) AppCtx.getInstance()
			.getBean("haloChildService");

	public static void proc(byte[] in, byte[] out) {
		if(in == null || in.length == 0)
			return;
	
		//格式报文：Nhalo_id/Naccount_id
		byte[] byteHaloId = new byte[4];
		System.arraycopy(in, 0, byteHaloId, 0, 4);
		int haloId = Utils.byte2Int(byteHaloId);
		
		byte[] byteAccountId = new byte[4];
		System.arraycopy(in, 4, byteAccountId, 0, 4);
		int accountId = Utils.byte2Int(byteAccountId);
		
		//根据accountId更新数据库haloId
		haloChildService.updateHaloChild(accountId);
		
		HaloChild hc = haloChildService.queryHaloChildByAccountId(accountId);
		if(hc != null) {
			short curDrinkLevel = hc.getLevel().shortValue();
			short curDrinkVol = hc.getCurDrinkWater().shortValue();
			byte curUniverserLevel = hc.getStarLevel();
			byte curUniverserId = hc.getStarId();
			
			short roleId = hc.getPetid().shortValue();
			short deviceLanguage = hc.getDevicelanguage().shortValue();
			
			byte[] visitedStars = new byte[30];
			if(hc.getExploredStarId() != null) {
				//将查询结果拷贝到数组中
				System.arraycopy(hc.getExploredStarId(), 0, visitedStars, 0, 30);
			} else {
				visitedStars = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			}
			
			/*
			unsigned short curDrinkLevel; //当前水杯等级  //2字节
			unsigned short curDrinkVol;  //当前水杯等级中的喝水量 //2字节
			unsigned char universerId[MAX_UNIVERSER_SIZE]; //保存探索过的星球ID，假若没有的话是0值。 //30字节
			unsigned char curUniverserLeveL;//当前星球等级，分3级 //1字节
			unsigned char curUniverserId;//当前星球ID //1字节
			unsigned int accountId;//水杯账户ID //4字节 //添加
			unsigned short roleId;//角色ID//2字节 //添加
			unsigned short deviceLanguage;//设备语言//2字节 //添加
			*/
			
			byte[] byteCurUniverserLevel = new byte[1];
			byteCurUniverserLevel[0] = curUniverserLevel;
			
			byte[] byteCurUniverserId = new byte[1];
			byteCurUniverserId[0] = curUniverserId;
			
			out = new byte[44];
			
			System.arraycopy(Utils.short2Byte(curDrinkLevel), 0, out, 0, 0);
			System.arraycopy(Utils.short2Byte(curDrinkVol), 0, out, 2, 2);
			System.arraycopy(byteCurUniverserLevel, 0, out, 4, 1);
			System.arraycopy(byteCurUniverserId, 0, out, 5, 1);
			System.arraycopy(visitedStars, 0, out, 6, 30);
			System.arraycopy(byteAccountId, 0, out, 36, 4);
			System.arraycopy(Utils.short2Byte(roleId), 0, out, 40, 2);
			System.arraycopy(Utils.short2Byte(deviceLanguage), 0, out, 42, 2);
			
		}		
	}
}
