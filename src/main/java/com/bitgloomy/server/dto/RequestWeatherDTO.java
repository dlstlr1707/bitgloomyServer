package com.bitgloomy.server.dto;

public class RequestWeatherDTO {
    private double latitude;
    private double longitude;
    private double xlatitude;
    private double ylongitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getXlatitude() {
        return xlatitude;
    }

    public void setXlatitude(double xlatitude) {
        this.xlatitude = xlatitude;
    }

    public double getYlongitude() {
        return ylongitude;
    }

    public void setYlongitude(double ylongitude) {
        this.ylongitude = ylongitude;
    }


}
