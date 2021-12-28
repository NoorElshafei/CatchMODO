package com.bbi.catchmodo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bbi.catchmodo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
    FirebaseUser firebaseUser;
    Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        continue_btn = findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(view -> {
            if (firebaseUser == null) {
                Intent intent = new Intent(Splash.this, WelcomeActivity.class);
                startActivity(intent);
            }else {
                Intent intent= new Intent(Splash.this, StartActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}