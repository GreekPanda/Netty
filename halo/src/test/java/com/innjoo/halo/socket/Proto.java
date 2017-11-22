package com.innjoo.halo.socket;

import java.io.Serializable;

import com.innjoo.halo.utils.Utils;

public class Proto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] headerId; //8 byte
	private short package_len;//2 byte
	private short opType; // 2 byte
	private short res;
	private byte[] data; // variable
	
	public Proto() {
		
	}
	
	public Proto(byte[] headerId, short package_len, short opType, byte[] data) {
		this.headerId = headerId;
		this.package_len = package_len;
		this.opType = opType;
		this.res = 0;
		this.data = data;
	}

	public short getPackage_len() {
		return package_len;
	}

	public void setPackage_len(short package_len) {
		this.package_len = package_len;
	}

	public short getOpType() {
		return opType;
	}

	public void setOpType(short opType) {
		this.opType = opType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getHeaderId() {
		return headerId;
	}

	public void setHeaderId(byte[] headerId) {
		this.headerId = headerId;
	}
	@Override
	public String toString() {
		String strHeaderId = new String(headerId);
		//String strData = new String(data);
		
		 return String.format("[headerId=%s,package_len=%d, opType=%x,content=%s]",
				 strHeaderId, package_len, opType, Utils.bytesToHexString(data));
	}

	public short getRes() {
		return 0;
	}

	public void setRes(short res) {
		this.res = res;
	}

}
