package com.clinicapp.catchmodo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinicapp.catchmodo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstActivity extends AppCompatActivity {
    TextView moodo;
    EditText userName, phone;
    LinearLayout login;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        userName = findViewById(R.id.userName);
        phone = findViewById(R.id.phone);
        moodo = findViewById(R.id.login_text);
        login = findViewById(R.id.login_layout);
        database = FirebaseDatabase.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!userName.getText().toString().equals("") && phone.getText().toString().equals("")) {

                    reference = database.getReference("UserInfo").push();
                } else {
                    Toast.makeText(FirstActivity.this, "Please,fill all info   ", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}