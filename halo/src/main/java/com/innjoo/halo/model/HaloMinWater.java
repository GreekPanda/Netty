package com.innjoo.halo.model;

public class HaloMinWater {
    private Integer id;

    private Integer kid;

    private Long insertTime;

    private Integer hourTime;

    private Integer dayTime;

    private Integer water;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKid() {
        return kid;
    }

    public void setKid(Integer kid) {
        this.kid = kid;
    }

    public Long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Long insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getHourTime() {
        return hourTime;
    }

    public void setHourTime(Integer hourTime) {
        this.hourTime = hourTime;
    }

    public Integer getDayTime() {
        return dayTime;
    }

    public void setDayTime(Integer dayTime) {
        this.dayTime = dayTime;
    }

    public Integer getWater() {
        return water;
    }

    public void setWater(Integer water) {
        this.water = water;
    }
}