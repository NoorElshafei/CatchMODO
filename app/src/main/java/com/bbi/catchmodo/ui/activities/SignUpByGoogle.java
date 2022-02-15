package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.data.model.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpByGoogle extends AppCompatActivity {
    private ImageView continueBtn, back,skip;
    EditText name, phoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    String image_google;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_by_google);
        continueBtn = findViewById(R.id.button5);

        phoneNumber = findViewById(R.id.editText5);
        back = findViewById(R.id.back);
        skip=findViewById(R.id.skip);
        progressDialog = new ProgressDialog(this);
        continueBtn.setOnClickListener(v -> {

            if (!phoneNumber.getText().toString().equals("")) {

                PerforAuth();
            } else {
                Toast.makeText(SignUpByGoogle.this, "Please, Enter Your Number phone ", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
        back.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpByGoogle.this, Splash.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        skip.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpByGoogle.this, StartActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

    private void PerforAuth() {
        progressDialog.setMessage("please,waiting while Signup ..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = mUser.getUid();
        String userPhone = phoneNumber.getText().toString();


        //init database && search from users
        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid).child("phone");
        // insert user information
        reference.setValue(userPhone).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                } else {
                    progressDialog.dismiss();
                }

            }
        });


    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(SignUpByGoogle.this, StartActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}