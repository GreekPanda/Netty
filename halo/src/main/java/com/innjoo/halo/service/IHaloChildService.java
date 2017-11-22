package com.innjoo.halo.service;


import java.util.List;

import com.innjoo.halo.model.HaloChild;

public interface IHaloChildService {

	HaloChild queryHaloChildByAccountId(Integer accountId);
	void updateByPrimaryKey(HaloChild hc);
	void updateHaloChild(Integer id);
	void updateHaloIdAndRoleIdByAccountId(Integer accountId, String haloId, byte petId);
	List<HaloChild> selectByHaloId(String haloId);
} 
