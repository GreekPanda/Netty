package com.innjoo.halo.proto;

import java.io.Serializable;
import java.util.Arrays;

public class HaloInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private byte[] haloId;// 水杯ID //4字节
	private int accountId;// 水杯账户ID //4字节 //添加
	private short roleId;// 角色id //2字节 //添加
	private byte[] deviceVersion;// 硬件版本 16个字节，格式如："D2017-03－19－BA01"
	private byte[] gameVersion;// 游戏软件版本 16个字节，格式如："G2017-03－19－BA01"
	private short curDrinkLevel; // 当前水杯等级 //2字节
	private short curDrinkVol; // 当前水杯等级中的喝水量 //2字节
	private byte[] universerId; // 保存探索过的星球ID，假若没有的话是0值。 //30字节
	private byte curUniverserLeveL;// 当前星球等级，分3级 //1字节
	private byte curUniverserId;// 当前星球ID //1字节
	TimeProto deviceTime;// 当前水杯时间 //8字节
	private short deviceLanuage;// 当前设备语言 //2字节 1代表简体中文，2代表西班牙语，3代表英语
	private short needUpdateDataSize;// 当前需要同步的喝水数据 2字节
	private int friend1;
	private int friend2;
	private int friend3;
	private int friend4;
	private int friend5;

	public HaloInfo(byte[] haloId, int accountId, short roleId, byte[] deviceVersion, byte[] gameVersion,
			short curDrinkLevel, short curDrinkVol, byte[] universerId, byte curUniverserLeveL, byte curUniverserId,
			TimeProto deviceTime, short deviceLanuage, short needUpdateDataSize, int friend1, int friend2, int friend3,
			int friend4, int friend5) {
		// super();
		this.haloId = haloId;
		this.accountId = accountId;
		this.roleId = roleId;
		this.deviceVersion = deviceVersion;
		this.gameVersion = gameVersion;
		this.curDrinkLevel = curDrinkLevel;
		this.curDrinkVol = curDrinkVol;
		this.universerId = universerId;
		this.curUniverserLeveL = curUniverserLeveL;
		this.curUniverserId = curUniverserId;
		this.deviceTime = deviceTime;
		this.deviceLanuage = deviceLanuage;
		this.needUpdateDataSize = needUpdateDataSize;
		this.friend1 = friend1;
		this.friend2 = friend2;
		this.friend3 = friend3;
		this.friend4 = friend4;
		this.friend5 = friend5;
	}

	public byte[] getHaloId() {
		return haloId;
	}

	public void setHaloId(byte[] haloId) {
		this.haloId = haloId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public short getRoleId() {
		return roleId;
	}

	public void setRoleId(short roleId) {
		this.roleId = roleId;
	}

	public byte[] getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(byte[] deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public byte[] getGameVersion() {
		return gameVersion;
	}

	public void setGameVersion(byte[] gameVersion) {
		this.gameVersion = gameVersion;
	}

	public short getCurDrinkLevel() {
		return curDrinkLevel;
	}

	public void setCurDrinkLevel(short curDrinkLevel) {
		this.curDrinkLevel = curDrinkLevel;
	}

	public short getCurDrinkVol() {
		return curDrinkVol;
	}

	public void setCurDrinkVol(short curDrinkVol) {
		this.curDrinkVol = curDrinkVol;
	}

	public byte[] getUniverserId() {
		return universerId;
	}

	public void setUniverserId(byte[] universerId) {
		this.universerId = universerId;
	}

	public byte getCurUniverserLeveL() {
		return curUniverserLeveL;
	}

	public void setCurUniverserLeveL(byte curUniverserLeveL) {
		this.curUniverserLeveL = curUniverserLeveL;
	}

	public byte getCurUniverserId() {
		return curUniverserId;
	}

	public void setCurUniverserId(byte curUniverserId) {
		this.curUniverserId = curUniverserId;
	}

	public TimeProto getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(TimeProto deviceTime) {
		this.deviceTime = deviceTime;
	}

	public short getDeviceLanuage() {
		return deviceLanuage;
	}

	public void setDeviceLanuage(short deviceLanuage) {
		this.deviceLanuage = deviceLanuage;
	}

	public short getNeedUpdateDataSize() {
		return needUpdateDataSize;
	}

	public void setNeedUpdateDataSize(short needUpdateDataSize) {
		this.needUpdateDataSize = needUpdateDataSize;
	}

	public int getFriend1() {
		return friend1;
	}

	public void setFriend1(int friend1) {
		this.friend1 = friend1;
	}

	public int getFriend2() {
		return friend2;
	}

	public void setFriend2(int friend2) {
		this.friend2 = friend2;
	}

	public int getFriend3() {
		return friend3;
	}

	public void setFriend3(int friend3) {
		this.friend3 = friend3;
	}

	public int getFriend4() {
		return friend4;
	}

	public void setFriend4(int friend4) {
		this.friend4 = friend4;
	}

	public int getFriend5() {
		return friend5;
	}

	public void setFriend5(int friend5) {
		this.friend5 = friend5;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "HaloInfo [haloId=" + Arrays.toString(haloId) + ", accountId=" + accountId + ", roleId=" + roleId
				+ ", deviceVersion=" + Arrays.toString(deviceVersion) + ", gameVersion=" + Arrays.toString(gameVersion)
				+ ", curDrinkLevel=" + curDrinkLevel + ", curDrinkVol=" + curDrinkVol + ", universerId="
				+ Arrays.toString(universerId) + ", curUniverserLeveL=" + curUniverserLeveL + ", curUniverserId="
				+ curUniverserId + ", deviceTime=" + deviceTime.toString() + ", deviceLanuage=" + deviceLanuage
				+ ", needUpdateDataSize=" + needUpdateDataSize + ", friend1=" + friend1 + ", friend2=" + friend2
				+ ", friend3=" + friend3 + ", friend4=" + friend4 + ", friend5=" + friend5 + "]";
	}

}
