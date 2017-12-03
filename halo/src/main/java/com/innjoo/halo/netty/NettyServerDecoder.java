package com.innjoo.halo.netty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.innjoo.halo.model.HaloChild;
import com.innjoo.halo.process.MakeFriends;
import com.innjoo.halo.process.OtherSetting;
import com.innjoo.halo.process.RecoverProc;
import com.innjoo.halo.process.ReqLinkProc;
import com.innjoo.halo.proto.HaloProto;
import com.innjoo.halo.proto.ProtoOpType;
import com.innjoo.halo.proto.ResultData;
import com.innjoo.halo.utils.Encryption;
import com.innjoo.halo.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/*
 * @Auth:廖黄河
 * @date:2017-11-8
 * 
 * 从客户端接收到二进制数据流之后逐字节解析，netty的标准做法。 * 
 * 
 * */

public class NettyServerDecoder extends ByteToMessageDecoder {

	private static final Logger LOG = Logger.getLogger(NettyServerDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// 从客户端接收到二进制流进行解析

		// 传输报文格式：a8package_head/Npackage_len/Nsender_id/Nreceiver_id/nsender_type/ncontorl_code/a{$length}data/ncrc

		int len = in.readableBytes();

		if (in == null || len <= 0) {
			LOG.info("Recv from client data length: " + len);
			return;
		}

		in.markReaderIndex();

		// 如果报文小于14字节，也就是小于头部长度，直接丢弃
		if (in.readableBytes() >= ProtoOpType.HALO_PACKAGE_HEADRE_LEN + 12) {

			// 1.获取头部的headerId，一般都是halohalo
			byte[] headerId = new byte[8];
			in.readBytes(headerId);

			// 如果headerId不是halolhalo，直接返回不处理
			String strHeaderId = new String(headerId);
			LOG.info(strHeaderId);

			if (!strHeaderId.equals("halohalo"))
				return;

			// 2.获取整个报文的长度，由于有高低字节序的问题，所以需要每一个字节转换，客户端默认是大端
			byte[] bytePackage_len = new byte[4];
			in.getBytes(8, bytePackage_len, 0, 4);
			// int package_len = Utils.byte2Int(bytePackage_len);

			int package_len = Utils.bytesToIntLittle(bytePackage_len, 0);
			// System.out.println("********************* package len : " + package_len);

			byte[] byteSenderId = new byte[4];
			in.getBytes(12, byteSenderId, 0, 4);
			int senderId = Utils.bytesToIntLittle(byteSenderId, 0);

			byte[] byteRecvId = new byte[4];
			in.getBytes(16, byteRecvId, 0, 4);
			int recvId = Utils.bytesToIntLittle(byteRecvId, 0);

			byte[] byteSenderType = new byte[2];
			in.getBytes(20, byteSenderType, 0, 2);
			int senderType = Utils.bytesToShortLittle(byteSenderType, 0);

			// 和客户端数据中的操作码
			byte[] byteCtrlCode = new byte[2];
			in.getBytes(22, byteCtrlCode, 0, 2);
			short ctrlCode = Utils.bytesToShortLittle(byteCtrlCode, 0);

			// 3.获取数据报文的长度，即整个整个包长减去包头26字节（headerId(8) + package_len(4) + sender_id(4)
			// recv_id(4)+sender_type(2) + control_code (2) + crc(2) 字节）
			short rawDataLen = (short) (package_len - 24);
			// System.out.println("********************* Raw data len : " + rawDataLen);
			if (rawDataLen < 0) {
				LOG.fatal("数据长度不正确: + " + rawDataLen);
				return;
			}

			byte[] clientData = new byte[rawDataLen];

			// 从数据流的第24字节开始读取整个数据长度
			in.getBytes(24, clientData, 0, rawDataLen);

			// 4.获取原来的crc
			short crc = in.getShort(package_len - 2);

			// 如果重新生成的crc与原来不一致，则直接返回不处理
			byte[] packageNoCrc = new byte[package_len - 2];
			in.getBytes(package_len - 2, packageNoCrc, 0, package_len - 2);
			// crc不包括crc字段，所以长度需要减去2字节
			short genCrc = Encryption.crc16(Utils.getChars(packageNoCrc), (byte) (package_len - 2));
			// if (crc != genCrc) {
			// //并将错误的结果直接返回给客户端
			// out.add(makeHaloProto(null, 0, ProtoOpType.SERVER_ACK_INVALID));
			// return;
			// }

			// 处理所有的分支流程
			processCtrlCode(ctrlCode, clientData, out);

		} else {
			in.resetReaderIndex();
			return;
		}
	}

	private void processReqLink(byte[] in, HaloChild hc, ResultData rd, List<Object> out) {
		try {
			ReqLinkProc.parseDataFromClientPackage(in, hc, rd);

			boolean isExisted = true;
			byte[] data = new byte[] {};

			// 判断当前账号是否存在，注意传入的参数是accountId对应数据库中主键id
			isExisted = ReqLinkProc.isAccountExisted(hc.getId());
			if (!isExisted) {
				LOG.debug("Data is: " + Arrays.toString(data) + ", length: " + data.length);
				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_ACK_ACCOUNT_INVALID, 0xf0000001));
				return;
			} else {
				// 更新账号的数据库内容
				if (rd.getNeedUpdateSize() > 0)
					rd.setCtrlCode(ProtoOpType.SERVER_CMD_REQ_HISTORYDETAIL_SYNC);
				else
					rd.setCtrlCode(ProtoOpType.SERVER_ACK_NORMAL);

				data = ReqLinkProc.sendResponseData(hc, (short) rd.getNeedUpdateSize());
				ReqLinkProc.update(hc);
				out.add(makeHaloProto(data, data.length, rd.getCtrlCode(), 0xf0000001));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processHistoryDetail(byte[] in, List<Object> out) {

	}

	private void processVersionUpdate(byte[] in, List<Object> out) {

		// 获取文件名
		String fileName = "e:/pack/";
		int fileNumber = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(fileName);
		sb.append("." + fileNumber);
		sb.append(".pack");

		short crc = 0;
		int fileLen = 0;

		// 读取文件
		Path path = Paths.get(fileName);
		try {
			byte[] content = Files.readAllBytes(path);
			fileLen = content.length;
			crc = Encryption.crc16(Utils.getChars(content), (short) fileLen);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] data = new byte[14];

		System.arraycopy(Utils.int2Byte(0), 0, data, 0, 4);
		System.arraycopy(Utils.int2Byte(fileLen), 0, data, 4, 4);
		System.arraycopy(Utils.int2Byte(fileNumber), 0, data, 8, 4);
		System.arraycopy(Utils.int2Byte(crc), 0, data, 12, 2);

		out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_VERSIONUPDATE, 0xf0000001));
	}

	private void processVersionUpdateFeedback(byte[] in, List<Object> out) {
		if (in == null || in.length <= 0)
			return;

		String fileName = "/www/thinkSwooleFirst/log/";

		// 报文内容格式：nfeedback/NpackageName，6个字节
		byte[] byteFeedback = new byte[2];
		System.arraycopy(in, 0, byteFeedback, 0, 2);
		short feedback = ByteBuffer.wrap(byteFeedback).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();

		byte[] bytePackageName = new byte[4];
		System.arraycopy(in, 2, bytePackageName, 0, 4);
		int packageName = ByteBuffer.wrap(bytePackageName).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

		// 不知道这个242是什么意思，原有的PHP代码就是这个
		if (feedback == 242) {
			byte[] emptyData = new byte[] {};
			out.add(makeHaloProto(emptyData, emptyData.length, ProtoOpType.SERVER_ACK_INVALID, 0xf0000001));
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(fileName + packageName + ".pack");
			Path path = Paths.get(fileName);
			try {
				byte[] data = Files.readAllBytes(path);
				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_VERSIONUPDATE_DATA, 0));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processVerUpdateAccept(byte[] in, List<Object> out) {

		if (in == null || in.length <= 0)
			return;

		// 报文格式Ncount/NpackageName，8个字节
		byte[] byteCount = new byte[4];
		System.arraycopy(in, 0, byteCount, 0, 4);
		int count = ByteBuffer.wrap(byteCount).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

		byte[] bytePackageName = new byte[4];
		System.arraycopy(in, 4, bytePackageName, 0, 4);
		int packageName = ByteBuffer.wrap(byteCount).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

		// String fileName = "/www/thinkSwooleFirst/log/";
		String fileName = "e:/package";
		StringBuilder sb = new StringBuilder();
		sb.append(fileName + packageName + ".pack");

		File f = new File(sb.toString());
		InputStream ins = null;
		try {
			ins = new FileInputStream(f);
			byte b[] = new byte[(int) f.length() - count]; // 创建合适文件大小的数组
			ins.skip(count);
			ins.read(b); // 读取文件中的内容到b[]数组

			if (b.length <= 0) {
				short res = 241;
				byte[] byteRes = new byte[2];
				System.arraycopy(Utils.short2Byte(res), 0, byteRes, 0, 0);
				out.add(makeHaloProto(byteRes, byteRes.length, ProtoOpType.SERVER_CMD_ACK_VERSIONUPDATE_END,
						0xf0000001));
			} else {
				out.add(makeHaloProto(b, b.length, ProtoOpType.SERVER_CMD_REQ_VERSIONUPDATE_DATA, count));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ins != null) {
					ins.close();
					ins = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void processRoleUpdate(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		short magic1 = 13;
		int magic2 = 100;

		byte[] data = new byte[6];

		System.arraycopy(Utils.short2Byte(magic1), 0, data, 0, 2);
		System.arraycopy(Utils.int2Byte(magic2), 0, data, 2, 4);

		out.add(makeHaloProto(data, data.length, ProtoOpType.HALO_CMD_REQ_ROLEUPDATE_RESP, 0xf0000001));
	}

	private void processAckRoleUpdate(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		int dataLen = 0;

		String fileName = "e:/pack/jiqiren.png";

		Path path = Paths.get(fileName);
		try {
			byte[] data = Files.readAllBytes(path);
			dataLen = (short) data.length;
			if (dataLen <= 0) {
				byte[] emptyData = new byte[] {};

				out.add(makeHaloProto(emptyData, emptyData.length, ProtoOpType.SERVER_CMD_REQ_ROLEUPDATE_DATA,
						0xf0000001));

			} else {

				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_ROLEUPDATE_DATA, 0xf0000001));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void processAckRoleUpdateDataAccept(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] bytePos = new byte[4];
		System.arraycopy(in, 0, bytePos, 0, 4);

		int pos = ByteBuffer.wrap(bytePos).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort();

		int dataLen = 0;
		// String fileName = "/www/thinkSwooleFirst/log/jiqiren.png";
		String fileName = "e:/packge/jiqiren.png";
		InputStream ins = null;
		try {
			File f = new File(fileName);
			ins = new FileInputStream(f);
			byte[] b = new byte[(int) f.length() - pos];
			ins.skip(pos);
			ins.read(b);

			if (dataLen <= 0) {
				byte[] emptyData = new byte[] {};
				out.add(makeHaloProto(emptyData, emptyData.length, ProtoOpType.SERVER_CMD_REQ_ROLEUPDATE_DATA,
						0xf0000001));

			} else {
				byte[] data = new byte[b.length];
				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_ROLEUPDATE_DATA, 0xf0000001));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ins != null) {
					ins.close();
					ins = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	private void processDataRecover(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] data = new byte[44];

		RecoverProc.proc(in, data);

		out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_DATARECOVER, 0xf0000001));

	}

	private void processInvalidCtrlCode(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] data = new byte[] {};

		out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_ACK_INVALID, 0xf0000001));

	}

	private void processOtherSetting(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] data = new byte[48];

		OtherSetting.proc(in, data);
		out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_OTHERSETTING_WRITE, 0xf0000001));

	}

	// 交友
	private void processMakeFriends(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] data = new byte[2];

		MakeFriends.proc(in, data);
		out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_ACK_MAKEFRIEND_REQ, 0xf0000001));
	}

	// in从传输报文中解析出来的数据
	private void processCtrlCode(short ctrlCode, byte[] in, List<Object> out) {

		switch (ctrlCode) {

		case 0:
			break;

		// 2
		case ProtoOpType.HALO_CMD_REQ_LINK_WITHDATA:
			HaloChild hc = new HaloChild();
			ResultData rd = new ResultData();
			processReqLink(in, hc, rd, out);
			break;

		// 4
		// 原有的PHP流程很晦涩，不能理解其逻辑是什么
		// TODO:需要跟水杯端沟通这个功能到底是什么作用？
		case ProtoOpType.HALO_CMD_ACK_HISTORYDETAIL_SYNC:
			processHistoryDetail(in, out);
			break;

		// 7
		case ProtoOpType.HALO_CMD_ACK_VERSIONUPDATE:
			processVersionUpdate(in, out);
			break;

		// 9
		case ProtoOpType.HALO_CMD_ACK_VERSIONUPDATE_ACK:
			processVersionUpdateFeedback(in, out);
			break;

		// 11=>0x0b
		case ProtoOpType.HALO_CMD_ACK_VERSIONUPDATE_DATA_ACCEPT:
			processVerUpdateAccept(in, out);
			break;

		// 13=>0x0d
		case ProtoOpType.HALO_CMD_REQ_ROLEUPDATE:
			processRoleUpdate(in, out);
			break;

		// 16=>0x10
		case ProtoOpType.HALO_CMD_ACK_ROLEUPDATE:
			processAckRoleUpdate(in, out);
			break;

		// 18=>0x12
		case ProtoOpType.HALO_CMD_ACK_ROLEUPDATE_DATA_ACCEPT:
			processAckRoleUpdateDataAccept(in, out);
			break;

		// 20=>0x14
		case ProtoOpType.HALO_CMD_ACK_DATA_RECOVER:
			processDataRecover(in, out);
			break;

		// 24=>0x18
		// 此接口不在需要了。
		// case ProtoOpType.HALO_CMD_ACK_DEVICEID_WRITE:
		// break;

		// 32=>0x20
		case ProtoOpType.HALO_CMD_ACK_OTHERSETTING_WRITE:
			processOtherSetting(in, out);
			break;

		// 37=>0x25
		case ProtoOpType.HALO_MAKEFRIEND_REQ:
			processMakeFriends(in, out);
			break;

		default:
			LOG.info("Unknown code: " + ctrlCode);
			processInvalidCtrlCode(in, out);
			return;
		}
	}

	// 构造回送给客户端的传输报文
	private HaloProto makeHaloProto(byte[] sendData, int dataLen, short ctrlCode, int recvId) {
		HaloProto hp = null;

		// a8package_head/Npackage_len/Nsender_id/Nreceiver_id/nsender_type/ncontorl_code/a{$length}data/ncrc
		// 将所有的要发送的数据全部拷贝到一个数组中然后计算crc
		byte[] transPackage = new byte[sendData.length + ProtoOpType.HALO_PACKAGE_HEADRE_LEN + 12];

		byte[] servId = "servhalo".getBytes();
		System.arraycopy(servId, 0, transPackage, 0, servId.length);

		int svrSendPackageLen = sendData.length + 14 + 12;
		byte[] byteSvrSendPackageLen = new byte[4];
		byteSvrSendPackageLen = Utils.int2Byte(svrSendPackageLen);
		System.arraycopy(byteSvrSendPackageLen, 0, transPackage, servId.length, 4);

		int svrSenderId = 0x0001;
		byte[] byteServId = new byte[4];
		byteServId = Utils.int2Byte(svrSenderId);
		System.arraycopy(byteServId, 0, transPackage, servId.length + 4, 4);

		int clntRecvId = recvId;
		byte[] byteclntRecvId = new byte[4];
		byteclntRecvId = Utils.int2Byte(clntRecvId);
		//// 需要向后偏移出前面已经占的4(svrSenderId) + 4(svrSendPackageLen)字节位置，下面类同
		System.arraycopy(byteclntRecvId, 0, transPackage, servId.length + 4 + 4, 4);

		short svrSenderType = (short) 0xf003;
		byte[] byteSvrSenderType = new byte[2];
		byteSvrSenderType = Utils.int2Byte(svrSenderType);
		System.arraycopy(byteSvrSenderType, 0, transPackage, servId.length + 4 + 4 + 4, 2);

		byte[] byteCtrlCode = new byte[2];
		byteCtrlCode = Utils.int2Byte(ctrlCode);
		System.arraycopy(byteCtrlCode, 0, transPackage, servId.length + 4 + 4 + 4 + 2, 2);

		if (sendData != null && sendData.length > 0) {
			hp = new HaloProto();

			hp.setPackage_head(servId);
			hp.setSender_id(svrSenderId);
			hp.setPackage_len(sendData.length + 26);
			hp.setReceiver_id(clntRecvId);
			hp.setSender_type(svrSenderType);
			hp.setControl_code(ctrlCode);
			hp.setData(sendData);
			System.arraycopy(sendData, 0, transPackage, servId.length + 4 + 4 + 4 + 2 + 2, sendData.length);
			// 将整个报文存放在数组中，然后全部计算crc
			short sendCrc = Encryption.crc16(Utils.getChars(transPackage), (short) transPackage.length);
			hp.setCrc(sendCrc);

			LOG.debug("#############HaloProto : " + hp.toString());

		} else {
			hp = new HaloProto();

			hp.setPackage_head(servId);
			hp.setSender_id(svrSenderId);
			hp.setPackage_len(26);
			hp.setPackage_head(servId);
			hp.setReceiver_id(clntRecvId);
			hp.setSender_type(svrSenderType);
			hp.setControl_code((short) 0x0023);
			hp.setData(new byte[] {});
			short sendCrc = Encryption.crc16(Utils.getChars(transPackage), (short) transPackage.length);
			hp.setCrc(sendCrc);

			LOG.debug("@@@@@@@@@@@@@@@@HaloProto : " + hp.toString());
		}

		// LOG.info(hp.toString());
		return hp;
	}

}
