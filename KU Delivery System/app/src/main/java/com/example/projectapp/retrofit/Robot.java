package com.example.projectapp.retrofit;

import com.google.gson.annotations.SerializedName;

public class Robot {
    @SerializedName("robot_idx")
    private String robot_idx;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public Robot(String robot_idx, String latitude, String longitude) {
        this.robot_idx = robot_idx;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setRobot_idx(String robot_idx) { this.robot_idx = robot_idx; }
    public String getRobot_idx() { return robot_idx; }

    public void setLatitude(String latitude) { this.latitude = latitude; }
    public String getLatitude() { return latitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }
    public String getLongitude() { return longitude; }
}
