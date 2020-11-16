package com.example.weatherforecast_demo.entity;

public class App {
    private String time;
    private String date;
    private String message;
    private String status;
    private Data data;
    private Forecast forecast;
    private CityInfo cityInfo;

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }


    public Forecast getForecast() {
        return forecast;
    }


    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

}
