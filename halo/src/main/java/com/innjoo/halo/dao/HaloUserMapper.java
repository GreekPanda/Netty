package com.innjoo.halo.dao;

import com.innjoo.halo.model.HaloUser;

public interface HaloUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HaloUser record);

    int insertSelective(HaloUser record);

    HaloUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HaloUser record);

    int updateByPrimaryKey(HaloUser record);
}