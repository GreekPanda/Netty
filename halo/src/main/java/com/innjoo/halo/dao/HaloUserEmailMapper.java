package com.innjoo.halo.dao;

import com.innjoo.halo.model.HaloUserEmail;

public interface HaloUserEmailMapper {
    int deleteByPrimaryKey(String email);

    int insert(HaloUserEmail record);

    int insertSelective(HaloUserEmail record);

    HaloUserEmail selectByPrimaryKey(String email);

    int updateByPrimaryKeySelective(HaloUserEmail record);

    int updateByPrimaryKey(HaloUserEmail record);
}