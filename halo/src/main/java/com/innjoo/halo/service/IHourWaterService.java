package com.innjoo.halo.service;

import java.util.Map;

import com.innjoo.halo.model.HaloHourWater;

public interface IHourWaterService {

	int insertSelective(HaloHourWater hhw);
	
	HaloHourWater selectByKidAndHour(Map<String, Object> map);
	
	int updateDrinkWaterByPrimaryKey(HaloHourWater record);
}
