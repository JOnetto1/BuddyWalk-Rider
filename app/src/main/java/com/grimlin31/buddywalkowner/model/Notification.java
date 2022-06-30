package com.grimlin31.buddywalkowner.model;

public class Notification {
    private int pending;
    private String userIndex;
    private String message;
    private double lat;
    private double lon;

    public Notification(){

    }

    public Notification(int pending, String userIndex, String message, double lat, double lon){
        this.pending = pending; // 0: now, 1: done, 2: pending
        this.userIndex = userIndex;
        this.message = message;
        this.lat = lat;
        this.lon = lon;
    }

    public int getPending(){return pending;}
    public String getUserIndex(){return userIndex;}
    public String getMessage(){return message;}
    public double getLatitude(){return lat;}
    public double getLongitude(){return lon;}
}
