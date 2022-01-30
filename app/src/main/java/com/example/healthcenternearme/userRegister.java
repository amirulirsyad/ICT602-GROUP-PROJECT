package com.example.healthcenternearme;

public class userRegister {

    private String fullName;
    private String username;
    private String email;
    private String permission;

    private String date;
    private String userAgent;
    private String userCoordinate;

    public userRegister() //Default constructor
    {

    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String name)
    {
        this.fullName = name;
    }

    public String getUserName()
    {
        return username;
    }

    public void setUserName(String user)
    {
        this.username = user;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String mail)
    {
        this.email = mail;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String da)
    {
        this.date = da;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String ua)
    {
        this.userAgent = ua;
    }

    public String getUserCoordinate()
    {
        return userCoordinate;
    }

    public void setUserCoordinate(String uc)
    {
        this.userCoordinate = uc;
    }

    public String getPermission()
    {
        return permission;
    }

    public void setPermission(String per)
    {
        this.permission = per;
    }
}
