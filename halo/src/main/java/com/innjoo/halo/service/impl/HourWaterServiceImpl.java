package com.innjoo.halo.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innjoo.halo.dao.HaloHourWaterMapper;
import com.innjoo.halo.model.HaloHourWater;
import com.innjoo.halo.service.IHourWaterService;

@Service("hourWaterService")
public class HourWaterServiceImpl implements IHourWaterService {

	@Autowired
	HaloHourWaterMapper hourWaterMapper;

	@Override
	public int insertSelective(HaloHourWater hhw) {
		return hourWaterMapper.insertSelective(hhw);

	}

	@Override
	public HaloHourWater selectByKidAndHour(Map<String, Object> map) {
		HaloHourWater hhw = new HaloHourWater();
		hhw = hourWaterMapper.selectByKidAndHour(map);
		return hhw;
	}

	@Override
	public int updateDrinkWaterByPrimaryKey(HaloHourWater record) {
		return hourWaterMapper.updateDrinkWaterByPrimaryKey(record);
	}

}
