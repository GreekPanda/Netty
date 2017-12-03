package com.innjoo.halo.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innjoo.halo.dao.HaloFriendsMapper;
import com.innjoo.halo.model.HaloFriends;
import com.innjoo.halo.service.IHaloFriendsService;

@Service("haloFriendsService")
public class HaloFriendsServiceImpl implements IHaloFriendsService {

	@Autowired
	private HaloFriendsMapper haloFriendsMapper;

	@Override
	public HaloFriends selectByPrimaryKey(Integer accountId) {
		HaloFriends hfs = new HaloFriends();

		hfs = haloFriendsMapper.selectByPrimaryKey(accountId);

		return hfs;
	}

	@Override
	public void insertSelective(HaloFriends hfs) {
		haloFriendsMapper.insertSelective(hfs);
	}

	@Override
	public HaloFriends selectByKid(Map<String, Object> map) {
		HaloFriends hf = new HaloFriends();

		hf = haloFriendsMapper.selectByKid(map);

		return hf;
	}

	@Override
	public int insertByPrimaryId(HaloFriends hfs) {
		
		haloFriendsMapper.insertByPrimaryId(hfs);
		
		return 0;
	}

	@Override
	public int insert(HaloFriends hfs) {
		haloFriendsMapper.insert(hfs);
		return 0;
	}
	
	

}
