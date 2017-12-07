package com.innjoo.halo.service;

import java.util.Map;

import com.innjoo.halo.model.HaloFriends;

public interface IHaloFriendsService {

	HaloFriends selectByPrimaryKey(Integer accountId);
	void insertSelective(HaloFriends hfs);
	HaloFriends selectByKid(Map<String, Object> map);
	//HaloFriends selectByKid(HaloFriends hfs);
	int insertByPrimaryId(HaloFriends hfs);
	int insert(HaloFriends hfs);
	int insertWithKid(Map<String, Object> map);
}
