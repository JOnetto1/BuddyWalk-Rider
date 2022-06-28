package com.grimlin31.buddywalkowner.model;

public class Notification {
    private int id;
    private String userIndex;
    private String message;
    private double lat;
    private double lon;

    public Notification(){

    }

    public Notification(int id, String userIndex, String message, double lat, double lon){
        this.id = id;
        this.userIndex = userIndex;
        this.message = message;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId(){return id;}
    public String getUserIndex(){return userIndex;}
    public String getMessage(){return message;}
    public double getLatitude(){return lat;}
    public double getLongitude(){return lon;}
}
