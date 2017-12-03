package com.innjoo.halo.model;

import java.util.Date;

public class HaloChild {
	private Integer id;

	private String haloId;

	private String name;

	private String avatar;

	private Date birth;

	private Short weight;

	private Integer gender;

	private Integer target;

	private String schoolTimeStart;

	private String schoolTimeEnd;

	private String sleepTimeStart;

	private String sleepTimeEnd;

	private Integer timeZone;

	private String exploredStarId;

	private Integer petid;

	private Integer level;

	private Integer score;

	private Integer pid;

	private Integer starId;

	private Integer starLevel;

	private Integer curDrinkWater;

	private Integer devicelanguage;

	private String insertTime;

	private Integer status;

	private Integer systemVersion;

	private String ip;

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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
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

	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	public String getExploredStarId() {
		return exploredStarId;
	}

	public void setExploredStarId(String exploredStarId) {
		this.exploredStarId = exploredStarId == null ? null : exploredStarId.trim();
	}

	public Integer getPetid() {
		return petid;
	}

	public void setPetid(Integer petid) {
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

	public Integer getStarId() {
		return starId;
	}

	public void setStarId(Integer starId) {
		this.starId = starId;
	}

	public Integer getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(Integer starLevel) {
		this.starLevel = starLevel;
	}

	public Integer getCurDrinkWater() {
		return curDrinkWater;
	}

	public void setCurDrinkWater(Integer curDrinkWater) {
		this.curDrinkWater = curDrinkWater;
	}

	public Integer getDevicelanguage() {
		return devicelanguage;
	}

	public void setDevicelanguage(Integer devicelanguage) {
		this.devicelanguage = devicelanguage;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime == null ? null : insertTime.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(Integer systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	@Override
	public String toString() {
		return "HaloChild [id=" + id + ", haloId=" + haloId + ", name=" + name + ", avatar=" + avatar + ", birth="
				+ birth + ", weight=" + weight + ", gender=" + gender + ", target=" + target + ", schoolTimeStart="
				+ schoolTimeStart + ", schoolTimeEnd=" + schoolTimeEnd + ", sleepTimeStart=" + sleepTimeStart
				+ ", sleepTimeEnd=" + sleepTimeEnd + ", timeZone=" + timeZone + ", exploredStarId=" + exploredStarId
				+ ", petid=" + petid + ", level=" + level + ", score=" + score + ", pid=" + pid + ", starId=" + starId
				+ ", starLevel=" + starLevel + ", curDrinkWater=" + curDrinkWater + ", devicelanguage=" + devicelanguage
				+ ", insertTime=" + insertTime + ", status=" + status + ", systemVersion=" + systemVersion + ", ip="
				+ ip + "]";
	}

}