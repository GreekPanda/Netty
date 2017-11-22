package com.innjoo.halo.process;

import org.apache.log4j.Logger;

import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.model.HaloFriends;
import com.innjoo.halo.proto.ProtoOpType;
import com.innjoo.halo.service.impl.HaloFriendsServiceImpl;
import com.innjoo.halo.utils.Utils;

public class MakeFriends {

	private static final Logger LOG = Logger.getLogger(MakeFriends.class);

	private final static HaloFriendsServiceImpl haloFriendsService = (HaloFriendsServiceImpl) AppCtx.getInstance()
			.getBean("haloFriendsService");

	public static short proc(byte[] in, byte[] out) {

		if (in == null || in.length == 0)
			return -1;

		// 报文格式Naccount/Nfriend_account
		byte[] byteAccountId = new byte[4];
		System.arraycopy(in, 0, byteAccountId, 0, 4);
		int accountId = Utils.byte2Int(byteAccountId);

		byte[] byteFriendAccountId = new byte[4];
		System.arraycopy(in, 0, byteFriendAccountId, 0, 4);
		int friendAccountId = Utils.byte2Int(byteFriendAccountId);

		// 根据账号查询数据库
		HaloFriends hfs = new HaloFriends();
		hfs = haloFriendsService.selectByKid(accountId);
		if (hfs != null) {
			HaloFriends newHfs = new HaloFriends();
			newHfs.setKid(accountId);
			newHfs.setfKid(friendAccountId);
			haloFriendsService.insertSelective(hfs);

			// 回送结果，回送报文内容只有两个自己，如果表存在回送0xf1，如果不存在回送0xf2
			short hasId = 0;
			if (hfs.getId() >= 1)
				hasId = 0xf1;
			else
				hasId = 0xf2;

			byte[] byteHasId = new byte[2];
			Utils.int2Byte(hasId, byteHasId, 2);
			System.arraycopy(byteHasId, 0, out, 0, 2);

		}
		return ProtoOpType.SERVER_ACK_MAKEFRIEND_REQ;

	}

}
