package com.bbi.catchmodo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {
    TextView moodo;
    EditText phoneNumber, userName;
    LinearLayout login;
    FirebaseDatabase database;
    DatabaseReference reference;
    String score = "0";
    UsersModel usersModel;
    ArrayList<UsersModel> usersModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userName = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.phone);
        moodo = findViewById(R.id.login_text);
        login = findViewById(R.id.login_layout);
        database = FirebaseDatabase.getInstance();


        login.setOnClickListener(view -> {
            String name = userName.getText().toString();
            String phone = phoneNumber.getText().toString();
            if (!name.isEmpty() && !phone.isEmpty()) {

                reference = database.getReference("UserInfo").push();
                UsersModel usersModel = new UsersModel(reference.getKey(), name, phone, score);
                reference.setValue(usersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }
                });


            } else {
                Toast.makeText(FirstActivity.this, "Please,fill all Information", Toast.LENGTH_SHORT).show();
            }


        });


    }
}