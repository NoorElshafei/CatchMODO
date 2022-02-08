package com.bbi.catchmodo.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RoomModel {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("password")
    private String password;
    @SerializedName("status")
    private String status;
    @SerializedName("duration")
    private Date duration;

}
