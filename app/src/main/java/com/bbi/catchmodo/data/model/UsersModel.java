package com.bbi.catchmodo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.ViewModel;
import com.google.gson.annotations.SerializedName;
public class UsersModel implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("score")
    private String score;

    public UsersModel(){
    }

    public UsersModel(String id, String name, String phone,String score) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.score = score;
    }

    protected UsersModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        phone = in.readString();
        score = in.readString();
    }
    public static final Creator<UsersModel> CREATOR = new Creator<UsersModel>() {
        @Override
        public UsersModel createFromParcel(Parcel in) {
            return new UsersModel(in);
        }
        @Override
        public UsersModel[] newArray(int size) {
            return new UsersModel[size];
        }
    };

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(score);
    }
}
