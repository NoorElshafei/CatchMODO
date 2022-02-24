package com.bbi.catchmodo.data.model;

import com.google.gson.annotations.SerializedName;

public class UserRoomModel {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("score")
    private long score;
    @SerializedName("id")
    private String id;

    public UserRoomModel(String user_id, int score, String id) {
        this.user_id = user_id;
        this.score = score;
        this.id = id;
    }

    public UserRoomModel() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
