package com.innjoo.halo.service;


import java.util.List;

import com.innjoo.halo.model.HaloChild;

public interface IHaloChildService {

	HaloChild selectByPrimaryKey(Integer accountId);
	void updateByPrimaryKey(HaloChild hc);
	void updateHaloChild(HaloChild record);
	void updateHaloIdAndRoleIdByAccountId(HaloChild record);
	List<HaloChild> selectByHaloId(String haloId);
	void updateInfoByPrimaryKey(HaloChild hc);
} 
