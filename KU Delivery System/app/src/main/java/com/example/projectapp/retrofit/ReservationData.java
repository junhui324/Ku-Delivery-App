package com.example.projectapp.retrofit;

import com.google.gson.annotations.SerializedName;

public class ReservationData {
    @SerializedName("st_id")
    private String st_id;

    @SerializedName("loc")
    private String loc;

    @SerializedName("type")
    private String type;

    @SerializedName("time_info")
    private String time_info;

    @SerializedName("load_type")
    private String load_type;


    public ReservationData(String st_id, String loc, String type, String time_info, String load_type) {
        this.st_id = st_id;
        this.loc = loc;
        this.type = type;
        this.time_info = time_info;
        this.load_type = load_type;
    }

    public void setSt_id(String st_id) { this.st_id = st_id; }
    public String getSt_id() { return st_id; }

    public void setLoc(String loc) { this.loc = loc; }
    public String getLoc() { return loc; }

    public void setType(String type) { this.type = type; }
    public String getType() { return type; }

    public void setTime_info(String time_info) { this.time_info = time_info; }
    public String getTime_info() { return time_info; }

    public void setLoad_type(String load_type) { this.load_type = load_type; }
    public String getLoad_type() { return load_type; }

}
