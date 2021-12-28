package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.databinding.ActivityLoginBinding;
import com.bbi.catchmodo.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    String email_pattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(view -> {
            login();
        });
    }

    public void login() {
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        if (!email.matches(email_pattern)) {
            binding.email.setError("please,enter email context right");
        } else if (password.isEmpty() || password.length() < 6) {
            binding.password.setError("please,Enter the password correctly");
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        sendUserToNextActivity();


                    } else {

                        Toast.makeText(LoginActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}