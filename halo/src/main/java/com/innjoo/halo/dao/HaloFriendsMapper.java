package com.innjoo.halo.dao;

import com.innjoo.halo.model.HaloFriends;

public interface HaloFriendsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HaloFriends record);

    int insertSelective(HaloFriends record);

    HaloFriends selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HaloFriends record);

    int updateByPrimaryKey(HaloFriends record);
    
    HaloFriends selectByKid(Integer kid);
    
    int insertByPrimaryId(HaloFriends record);
}