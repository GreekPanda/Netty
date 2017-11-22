package com.innjoo.halo.model;

import java.util.Date;

public class HaloDayWater {
    private Integer id;

    private Integer water;

    private Integer kid;

    private Integer typeTime;

    private Date uploadTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWater() {
        return water;
    }

    public void setWater(Integer water) {
        this.water = water;
    }

    public Integer getKid() {
        return kid;
    }

    public void setKid(Integer kid) {
        this.kid = kid;
    }

    public Integer getTypeTime() {
        return typeTime;
    }

    public void setTypeTime(Integer typeTime) {
        this.typeTime = typeTime;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}