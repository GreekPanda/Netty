package com.innjoo.halo.netty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
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

		// 如果报文小于14字节，也就是小于头部长度，直接丢弃
		if (in.readableBytes() >= ProtoOpType.HALO_PACKAGE_HEADRE_LEN) {

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
			short rawDataLen = (short) (package_len - 26);
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

		}
	}

	private void parseDataFromClientPackage(byte[] in, HaloChild hc, ResultData rd, byte[] out) {
		// 报文格式，此格式从原来的php中摘取，是一个字符串类型
		/*
		 * a12halo_id/Naccount_id/nroleId/a16device_version
		 * /a16game_version/ncurDrinkLevel/ncurDrinkVol
		 * /C30universerId/CcurUniverserLevel/CcurUniverserId
		 * /nYear/CnMonth/CnDay/CnHour/CnMin/CnSec/CDayIndex
		 * /ndeviceLanguage/nneedUpdateDataSize/Nfriend1
		 * /Nfriend2/Nfriend3/Nfriend4/Nfriend5';
		 * 
		 */

		// !!!注意从in获取的数据全部都是string型，内部包含的都是ASCII，所以直接使用new string(byte[])才能正确的获取数值
		byte[] haloId;
		short roleId;
		short curDrinkLevel;
		short curDrinkVol;
		byte[] universerId;
		byte curUniverserLevel;
		byte curUniverserId;
		short deviceLanuage;
		short needUpdateDataSize;
		try {
			haloId = new byte[12];
			System.arraycopy(in, 0, haloId, 0, 12);
			System.out.println(new String(haloId));

			// 2.accountId 从12字节开始获取，长度为4
			byte[] byteAccountId = new byte[4];
			System.arraycopy(in, 12, byteAccountId, 0, 4);
			int accountId = Integer.valueOf(new String(byteAccountId));

			// 3.ckpnum 从第16个字节获取，长度为2
			byte[] byteRoleId = new byte[2];
			System.arraycopy(in, 16, byteRoleId, 0, 2);
			roleId = Short.valueOf(new String(byteRoleId));

			// 4.device_version，从第20个字节获取，长度为16
			byte[] byteDeviceVersion = new byte[16];
			System.arraycopy(in, 18, byteDeviceVersion, 0, 16);
			// System.out.println(new String(byteDeviceVersion));

			// 5.GameVersion，从第36字节开始，长度为16
			byte[] byteGameVersion = new byte[16];
			System.arraycopy(in, 34, byteGameVersion, 0, 16);
			// System.out.println(new String(byteGameVersion));

			// 6.DrinkLevel,从50字节开始，长度为2
			byte[] byteCurDrinkLevel = new byte[2];
			System.arraycopy(in, 50, byteCurDrinkLevel, 0, 2);
			curDrinkLevel = 0;
			if (byteCurDrinkLevel != null && byteCurDrinkLevel.length > 0)
				curDrinkLevel = Short.valueOf(new String(byteCurDrinkLevel));

			// 7.dayNeedDrinkVol，从52字节开始，长度为2
			byte[] byteCurDrinkVol = new byte[2];
			System.arraycopy(in, 52, byteCurDrinkVol, 0, 2);
			curDrinkVol = 0;
			if (byteCurDrinkVol != null && byteCurDrinkVol.length > 0)
				curDrinkVol = Short.valueOf(new String(byteCurDrinkVol));

			/*
			 * a12halo_id/Naccount_id/nroleId/a16device_version
			 * /a16game_version/ncurDrinkLevel/ncurDrinkVol
			 * /C30universerId/CcurUniverserLevel/CcurUniverserId
			 * /nYear/CnMonth/CnDay/CnHour/CnMin/CnSec/CDayIndex
			 * /ndeviceLanguage/nneedUpdateDataSize/Nfriend1
			 * /Nfriend2/Nfriend3/Nfriend4/Nfriend5';
			 * 
			 */

			universerId = new byte[30];
			System.arraycopy(in, 54, universerId, 0, 30);

			// 9.curUniverserLeveL，从84自己开始，长度为一个字节
			byte[] byteCurUniverserLevel = new byte[1];
			System.arraycopy(in, 84, byteCurUniverserLevel, 0, 1);
			curUniverserLevel = Byte.valueOf(new String(byteCurUniverserLevel));

			// 10.curUniverserId，从85字节开始，长度为一个字节
			byte[] byteCurUniverserId = new byte[1];
			System.arraycopy(in, 85, byteCurUniverserId, 0, 1);
			curUniverserId = Byte.valueOf(new String(byteCurUniverserId));

			// 11.byteYear,从86字节开始，长度为一个字节
			byte[] byteYear = new byte[2];
			System.arraycopy(in, 86, byteYear, 0, 2);
			// TODO:为啥这个年就是有问题？
			// short year = Short.valueOf(new String(byteYear));

			// 12.byteMonth,从88字节开始，长度为一个字节
			byte[] byteMonth = new byte[1];
			System.arraycopy(in, 88, byteMonth, 0, 1);
			byte month = Byte.valueOf(new String(byteMonth));

			// 13.byteDay,从89字节开始，长度为一个字节
			byte[] byteDay = new byte[1];
			System.arraycopy(in, 89, byteDay, 0, 1);
			byte day = Byte.valueOf(new String(byteDay));

			/*
			 * a12halo_id/Naccount_id/nroleId/a16device_version
			 * /a16game_version/ncurDrinkLevel/ncurDrinkVol
			 * /C30universerId/CcurUniverserLevel/CcurUniverserId
			 * /nYear/CnMonth/CnDay/CnHour/CnMin/CnSec/CDayIndex
			 * /ndeviceLanguage/nneedUpdateDataSize/Nfriend1
			 * /Nfriend2/Nfriend3/Nfriend4/Nfriend5';
			 * 
			 */

			// 14.byteHour,从90字节开始，长度为一个字节
			byte[] byteHour = new byte[1];
			System.arraycopy(in, 90, byteHour, 0, 1);
			byte hour = Byte.valueOf(new String(byteHour));

			// 15.byteMinute,从91字节开始，长度为一个字节
			byte[] byteMinute = new byte[1];
			System.arraycopy(in, 91, byteMinute, 0, 1);
			byte min = Byte.valueOf(new String(byteMinute));

			// 16.byteSec,从92字节开始，长度为一个字节
			byte[] byteSec = new byte[1];
			System.arraycopy(in, 92, byteSec, 0, 1);
			byte sec = Byte.valueOf(new String(byteSec));

			// 17.DayIndex，从93字节开始，长度为一个字节
			byte[] byteWeekIndex = new byte[1];
			System.arraycopy(in, 93, byteWeekIndex, 0, 1);
			byte weekIndex = Byte.valueOf(new String(byteWeekIndex));

			// 18.deviceLanguage，从94字节开始，长度为一个字节
			byte[] byteDeviceLanuage = new byte[2];
			System.arraycopy(in, 94, byteDeviceLanuage, 0, 2);
			deviceLanuage = Short.valueOf(new String(byteDeviceLanuage));

			// 19. needUpdateDataSize，96字节开始，长度为4个字节
			byte[] byteNeedUpdateDataSize = new byte[2];
			System.arraycopy(in, 96, byteNeedUpdateDataSize, 0, 2);
			needUpdateDataSize = Short.valueOf(new String(byteNeedUpdateDataSize));

			// 20. friend1，98字节开始，长度为4个字节
			byte[] byteFriend1 = new byte[4];
			System.arraycopy(in, 98, byteFriend1, 0, 4);
			int friend1 = Integer.valueOf(new String(byteFriend1));

			// 21. friend2，102字节开始，长度为4个字节
			byte[] byteFriend2 = new byte[4];
			System.arraycopy(in, 102, byteFriend2, 0, 4);
			int friend2 = Integer.valueOf(new String(byteFriend2));

			// 22. friend3，106字节开始，长度为4个字节
			byte[] byteFriend3 = new byte[4];
			System.arraycopy(in, 106, byteFriend3, 0, 4);
			int friend3 = Integer.valueOf(new String(byteFriend3));

			// 23. friend4，116字节开始，长度为4个字节
			byte[] byteFriend4 = new byte[4];
			System.arraycopy(in, 110, byteFriend4, 0, 4);
			int friend4 = Integer.valueOf(new String(byteFriend4));

			// 24. friend5，120字节开始，长度为4个字节
			byte[] byteFriend5 = new byte[4];
			System.arraycopy(in, 114, byteFriend5, 0, 4);
			int friend5 = Integer.valueOf(new String(byteFriend5));

			LOG.info("haloId: " + Arrays.toString(haloId) + ", roleId: " + roleId + ", curDrinkLevel: " + curDrinkLevel
					+ ", curDrinkVol: " + curDrinkVol + ", universerId: " + Arrays.toString(universerId)
					+ ", curUniverserLevel: " + curUniverserLevel + ", curUniverserId: " + curUniverserId
					+ ", deviceLanuage: " + deviceLanuage + ", needUpdateDataSize: " + needUpdateDataSize);

			hc.setHaloId(new String(haloId));
			hc.setExploredStarId(new String(universerId));
			hc.setPetid((byte) roleId);
			hc.setLevel((int) curDrinkLevel);
			hc.setCurDrinkWater((int) curDrinkVol);
			hc.setStarId(curUniverserId);
			hc.setStarLevel(curUniverserLevel);
			hc.setDevicelanguage((byte) deviceLanuage);

			rd.setNeedUpdateSize(needUpdateDataSize);

		} catch (NumberFormatException e) {

			e.printStackTrace();
		}

	}

	private void processReqLink(byte[] in, HaloChild hc, ResultData rd, List<Object> out) {
		// 解析传输报文中的数据内容

		byte[] sendData = new byte[] {};

		try {
			parseDataFromClientPackage(in, hc, rd, sendData);

			boolean isExisted = true;
			HaloProto hp = new HaloProto();
			byte[] data = new byte[] {};

			// 判断当前账号是否存在，注意传入的参数是accountId，但是数据库中却是haloId，容易造成误解
			isExisted = ReqLinkProc.isAccountExisted(hc.getHaloId());
			if (!isExisted) {
				LOG.debug("Data is: " + Arrays.toString(data) + ", length" + data.length);
				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_ACK_ACCOUNT_INVALID));
				return;
			} else {
				// 更新账号的数据库内容
				if (rd.getNeedUpdateSize() > 0)
					rd.setCtrlCode(ProtoOpType.HALO_CMD_REQ_LINK_FIRST);
				else
					rd.setCtrlCode(ProtoOpType.SERVER_ACK_NORMAL);

				ReqLinkProc.sendResponseData(hc, (short) rd.getNeedUpdateSize(), data);
				rd.setData(data);
				ReqLinkProc.update(hc.getHaloId().getBytes(), hc);
				out.add(makeHaloProto(sendData, sendData.length, rd.getCtrlCode()));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseDataFromHistoryDetail(byte[] in, HaloChild hc, ResultData rd, byte[] sendData, List<Object> out) {
		int iLoop = in.length / 10;
		if (iLoop == 1) {
			// 直接获取历史数据
		} else {

		}
	}

	private void processHistoryDetail(byte[] in, HaloChild hc, ResultData rd, List<Object> out) {

	}

	private void processVersionUpdate(byte[] in, List<Object> out) {

		// 获取文件名
		String fileName = "/www/thinkSwooleFirst/log/";
		int fileNumber = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(fileName);
		sb.append("." + fileNumber);
		sb.append(".pack");

		short newCrc = 0;
		short dataLen = 0;

		// 读取文件
		Path path = Paths.get(fileName);
		try {
			byte[] data = Files.readAllBytes(path);
			dataLen = (short) data.length;
			newCrc = Encryption.crc16(Utils.getChars(data), (short) dataLen);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] resData = new byte[14];
		int tmp = 0;
		resData[0] = (byte) ((tmp >> 8) & 0xFF);
		resData[1] = (byte) ((tmp >> 16) & 0xFF);
		resData[2] = (byte) ((tmp >> 24) & 0xFF);
		resData[3] = (byte) ((tmp) & 0xFF);

		resData[4] = (byte) ((dataLen >> 8) & 0xFF);
		resData[5] = (byte) ((dataLen >> 16) & 0xFF);
		resData[6] = (byte) ((dataLen >> 24) & 0xFF);
		resData[7] = (byte) ((dataLen) & 0xFF);

		resData[8] = (byte) ((fileNumber >> 8) & 0xFF);
		resData[9] = (byte) ((fileNumber >> 16) & 0xFF);
		resData[10] = (byte) ((fileNumber >> 24) & 0xFF);
		resData[11] = (byte) ((fileNumber) & 0xFF);

		resData[12] = (byte) ((newCrc >> 8) & 0xFF);
		resData[13] = (byte) ((newCrc) & 0xFF);

		out.add(makeHaloProto(resData, resData.length, ProtoOpType.SERVER_CMD_REQ_VERSIONUPDATE));
	}

	private void processVersionUpdateFeedback(byte[] in, List<Object> out) {
		if (in == null || in.length <= 0)
			return;

		String fileName = "/www/thinkSwooleFirst/log/";
		short dataLen = 0;
		short newCrc = 0;

		// 报文内容格式：nfeedback/NpackageName，6个字节
		byte[] byteFeedback = new byte[2];
		System.arraycopy(in, 0, byteFeedback, 0, 2);
		short feedback = Utils.byte2Short(byteFeedback);

		byte[] bytePackageName = new byte[4];
		System.arraycopy(in, 2, bytePackageName, 0, 4);
		int packageName = Utils.byte2Int(bytePackageName);

		// 不知道这个242是什么意思，原有的PHP代码就是这个
		if (feedback == 242) {
			byte[] emptyData = new byte[] {};
			out.add(makeHaloProto(emptyData, emptyData.length, ProtoOpType.SERVER_ACK_INVALID));
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(fileName + packageName + ".pack");
			Path path = Paths.get(fileName);
			try {
				byte[] data = Files.readAllBytes(path);
				dataLen = (short) data.length;
				newCrc = Encryption.crc16(Utils.getChars(data), (short) dataLen);
				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_VERSIONUPDATE_DATA));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processVerUpdateAccept(byte[] in, List<Object> out) {

		if (in == null || in.length <= 0)
			return;

		int dataLen = 0;
		short newCrc = 0;

		// 报文格式Ncount/NpackageName，8个字节
		byte[] byteCount = new byte[4];
		System.arraycopy(in, 0, byteCount, 0, 4);
		int count = Utils.byte2Int(byteCount);

		byte[] bytePackageName = new byte[4];
		System.arraycopy(in, 4, bytePackageName, 0, 4);
		int packageName = Utils.byte2Int(bytePackageName);

		String fileName = "/www/thinkSwooleFirst/log/";
		StringBuilder sb = new StringBuilder();
		sb.append(fileName + packageName + ".pack");
		Path path = Paths.get(fileName);
		try {
			byte[] data = Files.readAllBytes(path);
			dataLen = (short) data.length;
			newCrc = Encryption.crc16(Utils.getChars(data), (short) dataLen);
			if (dataLen <= 0) {
				short res = 241;
				byte[] byteRes = new byte[2];

				byteRes[0] = (byte) ((res & 0xff) >> 8);
				byteRes[1] = (byte) (res & 0xff);
				out.add(makeHaloProto(byteRes, byteRes.length, ProtoOpType.SERVER_CMD_ACK_VERSIONUPDATE_END));

			} else {
				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_VERSIONUPDATE_DATA));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void processRoleUpdate(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		short magic1 = 15;
		byte[] byteMagic1 = new byte[2];
		byteMagic1[0] = (byte) ((magic1 & 0xff) >> 8);
		byteMagic1[1] = (byte) (magic1 & 0xff);

		int magic2 = 100;
		byte[] byteMagic2 = new byte[4];

		byteMagic2[0] = (byte) ((magic2 & 0xff) >> 24);
		byteMagic2[1] = (byte) ((magic2 & 0xff) >> 16);
		byteMagic2[2] = (byte) ((magic2 & 0xff) >> 8);
		byteMagic2[3] = (byte) ((magic2 & 0xff));

		byte[] data = new byte[6];
		System.arraycopy(byteMagic1, 0, data, 0, 2);
		System.arraycopy(byteMagic2, 0, data, 2, 4);

		out.add(makeHaloProto(data, data.length, ProtoOpType.HALO_CMD_REQ_ROLEUPDATE_RESP));
	}

	private void processAckRoleUpdate(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		int dataLen = 0;
		short newCrc = 0;

		String fileName = "/www/thinkSwooleFirst/log/jiqiren.png";
		Path path = Paths.get(fileName);
		try {
			byte[] data = Files.readAllBytes(path);
			dataLen = (short) data.length;
			newCrc = Encryption.crc16(Utils.getChars(data), (short) dataLen);
			if (dataLen <= 0) {
				byte[] emptyData = new byte[] {};

				out.add(makeHaloProto(emptyData, emptyData.length, ProtoOpType.SERVER_CMD_REQ_ROLEUPDATE_DATA));

			} else {

				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_ROLEUPDATE_DATA));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// TODO:此处需要从某一个位置开始读取之后开始，也就是类似于断点续传
	void processAckRoleUpdateDataAccept(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		int dataLen = 0;
		short newCrc = 0;
		String fileName = "/www/thinkSwooleFirst/log/jiqiren.png";
		Path path = Paths.get(fileName);
		try {
			byte[] data = Files.readAllBytes(path);
			dataLen = (short) data.length;
			newCrc = Encryption.crc16(Utils.getChars(data), (short) dataLen);
			if (dataLen <= 0) {
				byte[] emptyData = new byte[] {};
				out.add(makeHaloProto(emptyData, emptyData.length, ProtoOpType.SERVER_CMD_REQ_ROLEUPDATE_DATA));

			} else {
				out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_ROLEUPDATE_DATA));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processDataRecover(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] data = new byte[] {};

		RecoverProc.proc(in, data);

		out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_DATARECOVER));

	}

	private void processInvalidCtrlCode(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] data = new byte[] {};

		out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_ACK_INVALID));

	}

	private void processOtherSetting(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] data = new byte[] { 0 };

		OtherSetting.proc(in, data);
		out.add(makeHaloProto(data, data.length, ProtoOpType.SERVER_CMD_REQ_OTHERSETTING_WRITE));

	}

	// 交友
	private void processMakeFriends(byte[] in, List<Object> out) {
		if (in == null || in.length == 0)
			return;

		byte[] data = new byte[2];

		short ctrlCode = MakeFriends.proc(in, data);
		out.add(makeHaloProto(data, data.length, ctrlCode));
	}

	// in从传输报文中解析出来的数据，sendData是需要发出去的数据内容
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
		case ProtoOpType.HALO_CMD_ACK_HISTORYDETAIL_SYNC:
			HaloChild hc1 = new HaloChild();
			ResultData rd1 = new ResultData();
			processHistoryDetail(in, hc1, rd1, out);
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
	@SuppressWarnings("unused")
	private HaloProto makeHaloProto(byte[] sendData, int dataLen, short ctrlCode) {
		HaloProto hp = null;

		// 将所有的要发送的数据全部拷贝到一个数组中然后计算crc
		byte[] transPackage = new byte[sendData.length + ProtoOpType.HALO_PACKAGE_HEADRE_LEN];

		byte[] servId = "servhalo".getBytes();
		System.arraycopy(servId, 0, transPackage, 0, servId.length);

		int svrSenderId = 0x0001;
		byte[] byteServId = new byte[4];
		byteServId = Utils.int2Byte(svrSenderId);
		System.arraycopy(byteServId, 0, transPackage, servId.length, 4);

		int svrSendPackageLen = sendData.length + 14;
		byte[] byteSvrSendPackageLen = new byte[4];
		byteSvrSendPackageLen = Utils.int2Byte(svrSendPackageLen);
		// 需要向后便宜出前面(svrSenderId)已经占的4字节位置
		System.arraycopy(byteSvrSendPackageLen, 0, transPackage, servId.length + 4, 4);

		int clntRecvId = 0xf0000001;
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

		if (sendData != null || sendData.length > 0) {
			// 将数据内容全部拷贝到数组中
			// 这种写法显得很丑，但是比较直观，以免遗漏错误的字节
			System.arraycopy(sendData, 0, transPackage, servId.length + 4 + 4 + 4 + 2 + 2, sendData.length);

			// 将整个报文存放在数组中，然后全部计算crc
			short sendCrc = Encryption.crc16(Utils.getChars(transPackage), (short) transPackage.length);
			hp = new HaloProto(servId, svrSendPackageLen, svrSenderId, clntRecvId, svrSenderType, ctrlCode, sendData,
					sendCrc);
		} else {
			hp = new HaloProto(servId, 24, svrSenderId, clntRecvId, svrSenderType, (short) 0x0023, null, (short) 0);
		}

		LOG.info(hp.toString());
		return hp;
	}

}
