package com.bbi.catchmodo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bbi.catchmodo.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WelcomeActivity extends AppCompatActivity {
Button register;
TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        register=findViewById(R.id.button2);
        login=findViewById(R.id.text_login);
        register.setOnClickListener(view -> {


            Intent intent=new Intent(WelcomeActivity.this,RegisterActivity.class);
            startActivity(intent);
        });
        login.setOnClickListener(view -> {
            Intent intent2=new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent2);
        });

    }



}