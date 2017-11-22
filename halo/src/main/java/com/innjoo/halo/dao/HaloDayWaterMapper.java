package com.innjoo.halo.dao;

import com.innjoo.halo.model.HaloDayWater;

public interface HaloDayWaterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HaloDayWater record);

    int insertSelective(HaloDayWater record);

    HaloDayWater selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HaloDayWater record);

    int updateByPrimaryKey(HaloDayWater record);
}