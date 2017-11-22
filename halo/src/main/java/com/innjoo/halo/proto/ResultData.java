package com.innjoo.halo.proto;

import java.util.Arrays;

public class ResultData {

	private short ctrlCode;
	private short needUpdateSize;
	private byte[] data;

	public ResultData() {
	}

	public ResultData(short ctrlCode, short needUpdateSize, byte[] data) {
		this.ctrlCode = ctrlCode;
		this.needUpdateSize = needUpdateSize;
		this.data = data;
	}

	public short getCtrlCode() {
		return ctrlCode;
	}

	public void setCtrlCode(short ctrlCode) {
		this.ctrlCode = ctrlCode;
	}

	public short getNeedUpdateSize() {
		return needUpdateSize;
	}

	public void setNeedUpdateSize(short needUpdateSize) {
		this.needUpdateSize = needUpdateSize;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResultData [ctrlCode=" + ctrlCode + ", needUpdateSize=" + needUpdateSize + ", data="
				+ Arrays.toString(data) + "]";
	}

}
