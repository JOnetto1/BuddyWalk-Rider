package com.grimlin31.buddywalkowner.model;

public class Item {

    private String mName;
    private String userIndex;
    private String notification;

    public Item(String name, String userIndex, String notification) {

        this.mName = name;
        this.userIndex = userIndex;
        this.notification = notification;
    }

    public String getUserIndex() {

        return userIndex;
    }

    public void setUserIndex(String userIndex) {

        this.userIndex = userIndex;
    }

    public String getName() {

        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getNotification(){
        return notification;
    }

    public void setNotification(String notification){
        this.notification = notification;
    }

    @Override
    public String toString() {

        return this.mName;
    }
}
