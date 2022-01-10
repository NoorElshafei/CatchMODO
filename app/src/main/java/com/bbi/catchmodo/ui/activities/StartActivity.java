package com.bbi.catchmodo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bbi.catchmodo.R;

public class StartActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


    }

    public void startGameFirst(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    }
