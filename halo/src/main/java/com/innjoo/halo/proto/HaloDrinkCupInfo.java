package com.innjoo.halo.proto;

import java.io.Serializable;
import java.util.Arrays;

public class HaloDrinkCupInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] haloIdSN; // 水杯ID，12个字符
	private int accountId; // 水杯账号
	private int ckpnum; //
	private int ckver; //
	private int cksize; //
	private int isUpdate; //
	private int loadSize; //
	private int version; // 游戏版本
	private int dayNeedDrinkVol;
	private byte[] universerId; // 水杯探索过的星球ID数组
	private byte curUniverserLeveL; // 当前星球等级 //每个星球都为3级
	private byte curUniverserId; // 当前星球ID
	private byte tempAlet; // 水温警告设置
	private byte deviceLanuage; // 设备语言
	TimeProto sleepTimeStart;
	TimeProto sleepTimeStop;
	TimeProto schoolTimeStart;
	TimeProto schoolTimeStop;

	public HaloDrinkCupInfo(byte[] haloIdSN, int accountId, int ckpnum, int ckver, int cksize, int isUpdate,
			int loadSize, int version, int dayNeedDrinkVol, byte[] universerId, byte curUniverserLeveL,
			byte curUniverserId, byte tempAlet, byte deviceLanuage, TimeProto sleepTimeStart, TimeProto sleepTimeStop,
			TimeProto schoolTimeStart, TimeProto schoolTimeStop) {
		//super();
		this.haloIdSN = haloIdSN;
		this.accountId = accountId;
		this.ckpnum = ckpnum;
		this.ckver = ckver;
		this.cksize = cksize;
		this.isUpdate = isUpdate;
		this.loadSize = loadSize;
		this.version = version;
		this.dayNeedDrinkVol = dayNeedDrinkVol;
		this.universerId = universerId;
		this.curUniverserLeveL = curUniverserLeveL;
		this.curUniverserId = curUniverserId;
		this.tempAlet = tempAlet;
		this.deviceLanuage = deviceLanuage;
		this.sleepTimeStart = sleepTimeStart;
		this.sleepTimeStop = sleepTimeStop;
		this.schoolTimeStart = schoolTimeStart;
		this.schoolTimeStop = schoolTimeStop;
	}

	public byte[] getHaloIdSN() {
		return haloIdSN;
	}

	public void setHaloIdSN(byte[] haloIdSN) {
		this.haloIdSN = haloIdSN;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getCkpnum() {
		return ckpnum;
	}

	public void setCkpnum(int ckpnum) {
		this.ckpnum = ckpnum;
	}

	public int getCkver() {
		return ckver;
	}

	public void setCkver(int ckver) {
		this.ckver = ckver;
	}

	public int getCksize() {
		return cksize;
	}

	public void setCksize(int cksize) {
		this.cksize = cksize;
	}

	public int getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}

	public int getLoadSize() {
		return loadSize;
	}

	public void setLoadSize(int loadSize) {
		this.loadSize = loadSize;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getDayNeedDrinkVol() {
		return dayNeedDrinkVol;
	}

	public void setDayNeedDrinkVol(int dayNeedDrinkVol) {
		this.dayNeedDrinkVol = dayNeedDrinkVol;
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

	public byte getTempAlet() {
		return tempAlet;
	}

	public void setTempAlet(byte tempAlet) {
		this.tempAlet = tempAlet;
	}

	public byte getDeviceLanuage() {
		return deviceLanuage;
	}

	public void setDeviceLanuage(byte deviceLanuage) {
		this.deviceLanuage = deviceLanuage;
	}

	public TimeProto getSleepTimeStart() {
		return sleepTimeStart;
	}

	public void setSleepTimeStart(TimeProto sleepTimeStart) {
		this.sleepTimeStart = sleepTimeStart;
	}

	public TimeProto getSleepTimeStop() {
		return sleepTimeStop;
	}

	public void setSleepTimeStop(TimeProto sleepTimeStop) {
		this.sleepTimeStop = sleepTimeStop;
	}

	public TimeProto getSchoolTimeStart() {
		return schoolTimeStart;
	}

	public void setSchoolTimeStart(TimeProto schoolTimeStart) {
		this.schoolTimeStart = schoolTimeStart;
	}

	public TimeProto getSchoolTimeStop() {
		return schoolTimeStop;
	}

	public void setSchoolTimeStop(TimeProto schoolTimeStop) {
		this.schoolTimeStop = schoolTimeStop;
	}

	@Override
	public String toString() {
		return "HaloDrinkCupInfo [haloIdSN=" + Arrays.toString(haloIdSN) + ", accountId=" + accountId + ", ckpnum="
				+ ckpnum + ", ckver=" + ckver + ", cksize=" + cksize + ", isUpdate=" + isUpdate + ", loadSize="
				+ loadSize + ", version=" + version + ", dayNeedDrinkVol=" + dayNeedDrinkVol + ", universerId="
				+ Arrays.toString(universerId) + ", curUniverserLeveL=" + curUniverserLeveL + ", curUniverserId="
				+ curUniverserId + ", tempAlet=" + tempAlet + ", deviceLanuage=" + deviceLanuage + ", sleepTimeStart="
				+ sleepTimeStart + ", sleepTimeStop=" + sleepTimeStop + ", schoolTimeStart=" + schoolTimeStart
				+ ", schoolTimeStop=" + schoolTimeStop + "]";
	}

}
