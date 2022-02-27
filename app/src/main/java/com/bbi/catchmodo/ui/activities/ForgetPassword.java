package com.bbi.catchmodo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.databinding.ActivityForgetPasswordBinding;
import com.bbi.catchmodo.databinding.ActivityLoginBinding;
import com.bbi.catchmodo.util.Language;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        Language.changeBackDependsLanguage(binding.back,getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        binding.btnSendEmail.setOnClickListener(view -> {
            sendEmail();
        });
        binding.back.setOnClickListener(view -> {
            onBackPressed();

        });

    }
    public void sendEmail() {
        String email = binding.email.getText().toString();
        if (email.equals("")) {
            Toast.makeText(ForgetPassword.this, "please write email ", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetPassword.this, "please check your Email", Toast.LENGTH_SHORT).show();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(ForgetPassword.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
