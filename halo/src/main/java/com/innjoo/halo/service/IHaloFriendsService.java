package com.innjoo.halo.service;

import com.innjoo.halo.model.HaloFriends;

public interface IHaloFriendsService {

	HaloFriends selectByPrimaryKey(Integer accountId);
	void insertSelective(HaloFriends hfs);
	HaloFriends selectByKid(Integer kid);
	int insertByPrimaryId(HaloFriends hfs);
}
