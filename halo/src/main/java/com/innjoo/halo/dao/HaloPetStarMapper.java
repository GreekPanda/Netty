package com.innjoo.halo.dao;

import com.innjoo.halo.model.HaloPetStar;

public interface HaloPetStarMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HaloPetStar record);

    int insertSelective(HaloPetStar record);

    HaloPetStar selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HaloPetStar record);

    int updateByPrimaryKey(HaloPetStar record);
}