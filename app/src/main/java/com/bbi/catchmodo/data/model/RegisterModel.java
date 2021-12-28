package com.bbi.catchmodo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RegisterModel implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("score")
    private String score;

    public RegisterModel() {
    }

    public RegisterModel(String id, String user_name, String email, String phone, String score) {
        this.id = id;
        this.user_name = user_name;
        this.email = email;
        this.phone = phone;
        this.score=score;

    }

    protected RegisterModel(Parcel in) {
        id = in.readString();
        user_name = in.readString();
        email = in.readString();
        phone = in.readString();
        score=in.readString();

    }

    public static final Creator<RegisterModel> CREATOR = new Creator<RegisterModel>() {
        @Override
        public RegisterModel createFromParcel(Parcel in) {
            return new RegisterModel(in);
        }

        @Override
        public RegisterModel[] newArray(int size) {
            return new RegisterModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(user_name);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(score);

    }
}
