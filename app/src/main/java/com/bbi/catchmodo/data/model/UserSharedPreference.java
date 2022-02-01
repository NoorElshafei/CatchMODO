package com.bbi.catchmodo.data.model;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    Context context;
    public static String SHARED_NAME = "user";
    public static String USER_NAME = "name";
    public static String USER_EMAIL = "email";
    public static String USER_ID = "id";
    public static String USER_SCORE = "score";
    public static String USER_IMAGE_URL = "image_url";
    public static String USER_PHONE = "phone";
    public static String HIGH_SCORE = "highScore";







    public UserSharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void add(RegisterModel userModel) {
        editor.putString(USER_EMAIL,userModel.getEmail());
        editor.putString(USER_NAME, userModel.getUser_name());
        editor.putString(USER_ID, userModel.getId());
        editor.putString(USER_SCORE, userModel.getScore());
        editor.putString(USER_IMAGE_URL, userModel.getImage_url());
        editor.putString(USER_PHONE, userModel.getPhone());
        editor.apply();


    }
    public RegisterModel getUserDetails() {


        RegisterModel userModel = new RegisterModel(
                sharedPreferences.getString(USER_ID, ""),
                sharedPreferences.getString(USER_EMAIL, ""),
                sharedPreferences.getString(USER_NAME, ""),
                sharedPreferences.getString(USER_SCORE, ""),
                sharedPreferences.getString(USER_IMAGE_URL, ""),
                sharedPreferences.getString(USER_PHONE, ""));


        return userModel;
    }

    public void setHighScore(int highScore) {
        editor.putInt(HIGH_SCORE, highScore);
        editor.apply();

    }

    public int  getHighScore() {
        return sharedPreferences.getInt(HIGH_SCORE, getUserScore());
    }


    public void setUserScore(int score) {
        editor.putInt(USER_SCORE, score);
        editor.apply();

    }

    public int  getUserScore() {

        return Integer.parseInt(sharedPreferences.getString(USER_SCORE, "0"));
    }


















    public void removeData() {
        sharedPreferences.edit().clear().apply();
    }



}