package com.example.weatherforecast_demo.entity;

public class Yesterday {
    private String date;
    private String ymd;
    private String week;
    private String sunrise;
    private String high;
    private String low;
    private String sunset;
    private String aqi;
    private String fx;
    private String fl;
    private String type;
    private String notice;

    public void setDate(String date) {
        this.date = date;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getDate() {
        return date;
    }

    public String getAqi() {
        return aqi;
    }

    public String getFl() {
        return fl;
    }

    public String getFx() {
        return fx;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getNotice() {
        return notice;
    }

    public String getSunset() {
        return sunset;
    }

    public String getType() {
        return type;
    }

    public String getWeek() {
        return week;
    }

    public String getYmd() {
        return ymd;
    }
}
