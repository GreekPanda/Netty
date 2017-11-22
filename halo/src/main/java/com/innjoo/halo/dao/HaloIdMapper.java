package com.innjoo.halo.dao;

import com.innjoo.halo.model.HaloId;

public interface HaloIdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HaloId record);

    int insertSelective(HaloId record);
}