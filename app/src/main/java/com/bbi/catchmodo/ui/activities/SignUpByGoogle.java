package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private Button continueBtn;
    EditText name,phoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_by_google);
        continueBtn=findViewById(R.id.button5);

        phoneNumber=findViewById(R.id.editText5);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PerforAuth();
            }
        });

    }

    private void PerforAuth() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String email = mUser.getEmail();
        String userName = mUser.getDisplayName();
        String userPhone = phoneNumber.getText().toString();

        String userid = mUser.getUid();
        //init database && search from users
        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid);
        // insert user information
        RegisterModel userModel =new RegisterModel(userid,userName,email,userPhone,"0");
        reference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    sendUserToNextActivity();
                    finish();
                }

            }
        });

    }





    private void sendUserToNextActivity() {
        Intent intent = new Intent(SignUpByGoogle.this, StartActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}