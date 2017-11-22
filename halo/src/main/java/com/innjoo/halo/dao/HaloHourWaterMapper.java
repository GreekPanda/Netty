package com.innjoo.halo.dao;

import com.innjoo.halo.model.HaloHourWater;

public interface HaloHourWaterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HaloHourWater record);

    int insertSelective(HaloHourWater record);

    HaloHourWater selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HaloHourWater record);

    int updateByPrimaryKey(HaloHourWater record);
}