package com.innjoo.halo.dao;

import com.innjoo.halo.model.HaloMinWater;

public interface HaloMinWaterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HaloMinWater record);

    int insertSelective(HaloMinWater record);

    HaloMinWater selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HaloMinWater record);

    int updateByPrimaryKey(HaloMinWater record);
}