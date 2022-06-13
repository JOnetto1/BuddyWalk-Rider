package com.grimlin31.buddywalkowner.model;

public class Walker {
    private int id;
    private String email;
    private String password;
    private String username;
    private double lat;
    private double lon;
    private int busy;
    private int online;

    public Walker(){

    }

    public Walker(String email, String password){

    }

    public Walker(int id, String email, String password, String username, double lat, double lon, int busy, int online){
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.lat = lat;
        this.lon = lon;
        this.busy = busy;
        this.online = online;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public double getLatitude() {
        return lat;
    }
    public void setLatitude(double lat) {
        this.lat = lat;
    }
    public double getLongitude() {
        return lon;
    }
    public void setLongitude(double lon) {
        this.lon = lon;
    }
    public int getBusy() {return busy;}
    public void setBusy(int busy){this.busy = busy;}
    public int getOnline() {return online;}
    public void setOnline(int online) {this.online = online;}
}
