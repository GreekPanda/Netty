package com.innjoo.halo.proto;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author liaohuanghe date:2017-11-3 服务器与水杯客户端之间接口协议，按照规定的头文件来进行定义
 */
public class HaloProto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] package_head;
	private int package_len;
	private int sender_id;
	private int receiver_id;
	private short sender_type;
	private short control_code;
	private byte[] data;
	private short crc;
	
	public HaloProto() {
		
	}	
		
	public HaloProto(byte[] package_head, int package_len, int sender_id, int receiver_id, short sender_type,
			short control_code, byte[] data, short crc) {
		//super();
		this.package_head = package_head;
		this.package_len = package_len;
		this.sender_id = sender_id;
		this.receiver_id = receiver_id;
		this.sender_type = sender_type;
		this.control_code = control_code;
		this.data = data;
		this.crc = crc;
	}
	public byte[] getPackage_head() {
		return package_head;
	}
	public void setPackage_head(byte[] package_head) {
		this.package_head = package_head;
	}
	public int getPackage_len() {
		return package_len;
	}
	public void setPackage_len(int package_len) {
		this.package_len = package_len;
	}
	public int getSender_id() {
		return sender_id;
	}
	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}
	public int getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(int receiver_id) {
		this.receiver_id = receiver_id;
	}
	public short getSender_type() {
		return sender_type;
	}
	public void setSender_type(short sender_type) {
		this.sender_type = sender_type;
	}
	public short getControl_code() {
		return control_code;
	}
	public void setControl_code(short contorl_code) {
		this.control_code = contorl_code;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public short getCrc() {
		return crc;
	}
	public void setCrc(short crc) {
		this.crc = crc;
	}
	
	@Override
	public String toString() {
		return "HaloProto [package_head=" + Arrays.toString(package_head) + ", package_len=" + package_len
				+ ", sender_id=" + sender_id + ", receiver_id=" + receiver_id + ", sender_type=" + sender_type
				+ ", contorl_code=" + control_code + ", data=" + Arrays.toString(data) + ", crc=" + crc + "]";
	}
	
}
