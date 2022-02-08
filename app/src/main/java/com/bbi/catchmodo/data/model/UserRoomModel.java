package com.bbi.catchmodo.data.model;

import com.google.gson.annotations.SerializedName;

public class UserRoomModel {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("score")
    private String score;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
