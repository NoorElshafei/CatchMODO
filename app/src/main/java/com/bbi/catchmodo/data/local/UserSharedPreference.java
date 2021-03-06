package com.bbi.catchmodo.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.bbi.catchmodo.data.model.RegisterModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSharedPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;


    Context context;
    public static String SHARED_NAME = "user";
    public static String USER_NAME = "name";
    public static String USER_EMAIL = "email";
    public static String USER_ID = "id";
    public static String USER_SCORE = "score";
    public static String USER_IMAGE_URL = "image_url";
    public static String USER_PHONE = "phone";
    public static String HIGH_SCORE = "highScore";
    public static String USER_LANG = "USER_LANGUAGE";
    public static String USER_COINS = "USER_COINS";


    public UserSharedPreference(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseUser != null) {
            firebaseUser = firebaseAuth.getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(firebaseUser.getUid());
        }

        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


    }

    public void add(RegisterModel userModel) {
        editor.putString(USER_EMAIL, userModel.getEmail());
        editor.putString(USER_NAME, userModel.getUser_name());
        editor.putString(USER_ID, userModel.getId());
        editor.putString(USER_SCORE, userModel.getScore());
        editor.putString(USER_IMAGE_URL, userModel.getImage_url());
        editor.putString(USER_PHONE, userModel.getPhone());
        editor.putLong(USER_COINS, userModel.getCoins());
        editor.apply();

    }

    public RegisterModel getUserDetails() {

        RegisterModel userModel = new RegisterModel(
                sharedPreferences.getString(USER_ID, ""),
                sharedPreferences.getString(USER_EMAIL, ""),
                sharedPreferences.getString(USER_NAME, ""),
                sharedPreferences.getString(USER_SCORE, ""),
                sharedPreferences.getString(USER_IMAGE_URL, ""),
                sharedPreferences.getString(USER_PHONE, ""),
                sharedPreferences.getLong(USER_COINS, 0)
        );

        return userModel;
    }

    public void setHighScore(int highScore) {
        editor.putInt(HIGH_SCORE, highScore);
        editor.apply();

    }

    public int getHighScore() {
        return sharedPreferences.getInt(HIGH_SCORE, getUserScore());
    }

    public void setCoins(long coins) {
        if (reference != null) {
            reference.child("coins").setValue(coins);
        }
        editor.putLong(USER_COINS, coins);
        editor.apply();

    }

    public long getCoins() {
        return sharedPreferences.getLong(USER_COINS, getUserCoins());
    }

    private long getUserCoins() {

        return sharedPreferences.getLong(USER_COINS, 0);
    }


    public void setRoomId(String roomId) {
        editor.putString("Room_ID", roomId);
        editor.apply();

    }

    public String getRoomId() {
        return sharedPreferences.getString("Room_ID", "0");
    }


    //ROOM NAME
    public void setRoomName(String roomName) {
        editor.putString("ROOM_NAME", roomName);
        editor.apply();

    }

    public String getRoomName() {
        return sharedPreferences.getString("ROOM_NAME", "");
    }

    //


    public void setUserScore(int score) {
        editor.putInt(USER_SCORE, score);
        editor.apply();

    }

    public int getUserScore() {

        return Integer.parseInt(sharedPreferences.getString(USER_SCORE, "0"));
    }


    public void removeData() {
        sharedPreferences.edit().clear().apply();
    }

    public void saveLanguage(String lang) {
        SharedPreferences.Editor shardEditor = sharedPreferences.edit();
        shardEditor.putString(USER_LANG, lang);
        shardEditor.apply();

    }

    public String getLanguage() {
        return sharedPreferences.getString(USER_LANG, "ar");

    }

}
