package com.example.projectapp.retrofit;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;

public class Data {
    @SerializedName("st_id")
    private String st_id;

    @SerializedName("send_name")
    private String send_name;

    @SerializedName("rec_name")
    private String rec_name;

    @SerializedName("type")
    private String type;

    @SerializedName("send_loc")
    private String send_loc;

    @SerializedName("rec_loc")
    private String rec_loc;

    @SerializedName("time_info")
    private String time_info;

    public Data(String st_id, String send_name, String rec_name, String type, String send_loc, String rec_loc, String time_info) {
        this.st_id = st_id;
        this.send_name = send_name;
        this.rec_name = rec_name;
        this.type = type;
        this.send_loc = send_loc;
        this.rec_loc = rec_loc;
        this.time_info = time_info;
    }

    public void setSt_id(String st_id) { this.st_id = st_id; }
    public String getSt_id() { return st_id; }

    public void setSend_name(String send_name) { this.send_name = send_name; }
    public String getSend_name() { return send_name; }

    public void setRec_name(String rec_name) { this.rec_name = rec_name; }
    public String getRec_name() { return rec_name; }

    public void setType(String type) { this.type = type; }
    public String getType() { return type; }

    public void setSend_loc(String send_loc) { this.send_loc = send_loc; }
    public String getSend_loc() { return send_loc; }

    public void setRec_loc(String rec_loc) { this.rec_loc = rec_loc; }
    public String getRec_loc() { return rec_loc; }

    public void setTime_info(String time_info) { this.time_info = time_info; }
    public String getTime_info() { return time_info; }

}

