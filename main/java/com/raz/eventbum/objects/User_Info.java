package com.raz.eventbum.objects;


public class User_Info {

    private String userName;
    private String userID;

    public User_Info() {
    }

    public User_Info(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
