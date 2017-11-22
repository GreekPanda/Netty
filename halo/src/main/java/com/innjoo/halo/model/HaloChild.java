package com.innjoo.halo.model;

import java.util.Date;

public class HaloChild {
	private Integer id;

	private String haloId;

	private String name;

	private String avatar;

	private Date birth;

	private Short weight;

	private Byte gender;

	private Integer target;

	private String schoolTimeStart;

	private String schoolTimeEnd;

	private String sleepTimeStart;

	private String sleepTimeEnd;

	private Byte timeZone;

	private String exploredStarId;

	private Byte petid;

	private Integer level;

	private Integer score;

	private Integer pid;

	private Byte starId;

	private Byte starLevel;

	private Integer curDrinkWater;

	private Byte devicelanguage;
	
	public HaloChild() {
		
	}

	public HaloChild(String haloId, 
			String exploredStarId, 
			short roleId, 
			short curDrinkLevel, 
			short curDrinkVol, 
			byte starId,
			byte starLevel, 
			short deviceLanuage) {
		// TODO Auto-generated constructor stub
		this.haloId = haloId;
		this.exploredStarId = exploredStarId;
		this.petid = (byte) roleId;
		this.level = (int) curDrinkLevel;
		this.curDrinkWater = (int) curDrinkVol;
		this.starId = starId;
		this.starLevel = starLevel;
		this.devicelanguage = (byte) deviceLanuage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHaloId() {
		return haloId;
	}

	public void setHaloId(String haloId) {
		this.haloId = haloId == null ? null : haloId.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar == null ? null : avatar.trim();
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Short getWeight() {
		return weight;
	}

	public void setWeight(Short weight) {
		this.weight = weight;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public String getSchoolTimeStart() {
		return schoolTimeStart;
	}

	public void setSchoolTimeStart(String schoolTimeStart) {
		this.schoolTimeStart = schoolTimeStart == null ? null : schoolTimeStart.trim();
	}

	public String getSchoolTimeEnd() {
		return schoolTimeEnd;
	}

	public void setSchoolTimeEnd(String schoolTimeEnd) {
		this.schoolTimeEnd = schoolTimeEnd == null ? null : schoolTimeEnd.trim();
	}

	public String getSleepTimeStart() {
		return sleepTimeStart;
	}

	public void setSleepTimeStart(String sleepTimeStart) {
		this.sleepTimeStart = sleepTimeStart == null ? null : sleepTimeStart.trim();
	}

	public String getSleepTimeEnd() {
		return sleepTimeEnd;
	}

	public void setSleepTimeEnd(String sleepTimeEnd) {
		this.sleepTimeEnd = sleepTimeEnd == null ? null : sleepTimeEnd.trim();
	}

	public Byte getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Byte timeZone) {
		this.timeZone = timeZone;
	}

	public String getExploredStarId() {
		return exploredStarId;
	}

	public void setExploredStarId(String exploredStarId) {
		this.exploredStarId = exploredStarId == null ? null : exploredStarId.trim();
	}

	public Byte getPetid() {
		return petid;
	}

	public void setPetid(Byte petid) {
		this.petid = petid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Byte getStarId() {
		return starId;
	}

	public void setStarId(Byte starId) {
		this.starId = starId;
	}

	public Byte getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(Byte starLevel) {
		this.starLevel = starLevel;
	}

	public Integer getCurDrinkWater() {
		return curDrinkWater;
	}

	public void setCurDrinkWater(Integer curDrinkWater) {
		this.curDrinkWater = curDrinkWater;
	}

	public Byte getDevicelanguage() {
		return devicelanguage;
	}

	public void setDevicelanguage(Byte devicelanguage) {
		this.devicelanguage = devicelanguage;
	}
}