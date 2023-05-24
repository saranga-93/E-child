package com.example.phase5;

public class LocationHelper {
    Double latitude,longitude;
    String Date, Time;

    public LocationHelper(Double latitude, Double longitude, String date, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        Date = date;
        Time = time;
    }

    public LocationHelper() {

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
