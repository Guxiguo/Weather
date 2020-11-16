package com.example.weatherforecast_demo.entity;

public class CityInfo{
    private String city;
    private String cityId;
    private String parent;
    private String updateTime;

    public void setCity(String city) {
        this.city = city;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCity() {
        return city;
    }

    public String getCityId() {
        return cityId;
    }

    public String getParent() {
        return parent;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
