package com.bbi.catchmodo.ui.activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpByFacebook extends AppCompatActivity {
    private Button continueBtn;
    EditText phoneNumber;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private FirebaseUser mUser;
    DatabaseReference reference;
    private Intent intent;
    String image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_by_facebook);
        continueBtn = findViewById(R.id.button4);
        phoneNumber = findViewById(R.id.editText5);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneNumber.getText().toString().equals("")) {

                    PerforAuth();
                } else {
                    Toast.makeText(SignUpByFacebook.this, "Please, Enter Your Number phone " , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });


    }

    private void PerforAuth() {
        progressDialog.setMessage("please,waiting while Signup ..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String email = mUser.getEmail();
        String userName = mUser.getDisplayName();
        String userPhone = phoneNumber.getText().toString();


        image_url = getIntent().getExtras().getString("image");
        Log.d(TAG, "PerforAuth: "+image_url);

        String userid = mUser.getUid();
        //init database && search from users
        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid);
        // insert user information
        RegisterModel userModel = new RegisterModel(userid, userName, email, userPhone, "0",image_url);
        reference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    sendUserToNextActivity();
                    finish();
                } else {
                    progressDialog.dismiss();
                }

            }
        });

    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(SignUpByFacebook.this, StartActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);



    }
}