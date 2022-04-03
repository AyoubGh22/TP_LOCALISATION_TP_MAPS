package com.example.testret.Models;


public class Position {
    private int id;
    private double latitude;
    private double longitude;
    private String imei ;

    public Position(int id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Position(int id, double latitude, double longitude, String imei) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imei=imei;
    }

    public Position(double latitude, double longitude, String imei) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imei = imei;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
