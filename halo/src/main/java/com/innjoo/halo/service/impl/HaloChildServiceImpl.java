package com.innjoo.halo.service.impl;

import com.innjoo.halo.dao.HaloChildMapper;
import com.innjoo.halo.model.HaloChild;
import com.innjoo.halo.service.IHaloChildService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("haloChildService")
public class HaloChildServiceImpl implements IHaloChildService {

	private static final Logger LOG = Logger.getLogger(HaloChildServiceImpl.class);

	@Autowired
	private HaloChildMapper haloChildMapper;

	// 注意此处只是查询出一个结果而不是将结果作为一个List的结果集，是否可能查询出多个结果？
	@Override
	public HaloChild selectByPrimaryKey(Integer accoutId) {

		HaloChild hc = new HaloChild();

		hc = (HaloChild) haloChildMapper.selectByPrimaryKey(accoutId);

		return hc;
	}

	@Override
	public void updateByPrimaryKey(HaloChild hc) {
		haloChildMapper.updateByPrimaryKey(hc);
	}

	@Override
	public void updateHaloChild(HaloChild record) {
		haloChildMapper.updateHaloIdByPrimaryKey(record);
	}

	@Override
	public void updateHaloIdAndRoleIdByAccountId(HaloChild record) {
		haloChildMapper.updateHaloIdAndRoleIdByPrimaryKey(record);
	}

	@Override
	public List<HaloChild> selectByHaloId(String haloId) {
		List<HaloChild> listHaloChild = new ArrayList<HaloChild>();
		listHaloChild = haloChildMapper.selectByHaloId(haloId);

		return listHaloChild;
	}

	@Override
	public void updateInfoByPrimaryKey(HaloChild hc) {
		haloChildMapper.updateInfoByPrimaryKey(hc);

	}

}
