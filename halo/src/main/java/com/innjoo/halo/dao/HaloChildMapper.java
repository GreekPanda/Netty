package com.innjoo.halo.dao;

import java.util.List;

import com.innjoo.halo.model.HaloChild;

public interface HaloChildMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HaloChild record);

    int insertSelective(HaloChild record);

    HaloChild selectByPrimaryKey(Integer id);
    
    List<HaloChild> selectByHaloId(String haloId);

    int updateByPrimaryKeySelective(HaloChild record);

    int updateByPrimaryKey(HaloChild record);
    
    int updateHaloIdByPrimaryKey(HaloChild record);

	void updateHaloIdAndRoleIdByPrimaryKey(HaloChild record);
	
	void updateInfoByPrimaryKey(HaloChild record);
    
}