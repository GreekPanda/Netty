package com.innjoo.halo.process;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.innjoo.halo.ctx.AppCtx;
import com.innjoo.halo.model.HaloFriends;
import com.innjoo.halo.service.IHaloFriendsService;
import com.innjoo.halo.utils.Utils;

public class MakeFriends {

	private static final Logger LOG = Logger.getLogger(MakeFriends.class);

	private final static IHaloFriendsService haloFriendsService = (IHaloFriendsService) AppCtx.getInstance()
			.getBean("haloFriendsService");

	public static void proc(byte[] in, byte[] out) {

		if (in == null || in.length == 0)
			return;

		// 报文格式Naccount/Nfriend_account，8个字节
		byte[] byteAccountId = new byte[4];
		System.arraycopy(in, 0, byteAccountId, 0, 4);
		int accountId = ByteBuffer.wrap(byteAccountId).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

		byte[] byteFriendAccountId = new byte[4];
		System.arraycopy(in, 4, byteFriendAccountId, 0, 4);
		int friendAccountId = ByteBuffer.wrap(byteFriendAccountId).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kid", accountId);
		map.put("fkid", friendAccountId);

		HaloFriends hfs = haloFriendsService.selectByKid(map);

		if (hfs == null) {
			HaloFriends newHfs = new HaloFriends();
			newHfs.setfKid(friendAccountId);
			newHfs.setKid(accountId);
			haloFriendsService.insert(newHfs);

			// 回送结果，回送报文内容只有两个自己，如果表存在回送0xf1，如果不存在回送0xf2，这里原来的逻辑是否有点问题
			short hasId = 0;
			// if (hfs.getId() >= 1)
			hasId = 0xf1;
			// else
			// hasId = 0xf2;

			System.arraycopy(Utils.short2Byte(hasId), 0, out, 0, 2);

		} else {
			short hasId = 0xf1;
			System.arraycopy(Utils.short2Byte(hasId), 0, out, 0, 2);
		}
	}

}
