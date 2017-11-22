package com.innjoo.halo.model;

public class HaloPetStar {
    private Integer id;

    private String starName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName == null ? null : starName.trim();
    }
}