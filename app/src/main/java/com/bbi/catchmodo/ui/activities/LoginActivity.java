package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;
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
    ProgressDialog progressDialog;
    private boolean passIsVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        binding.btnLogin.setOnClickListener(view -> {
            login();
        });
        binding.forgetPassword.setOnClickListener(view -> {
            Intent intent2= new Intent(LoginActivity.this,ForgetPassword.class);
            startActivity(intent2);

        });
        binding.eyePassImage.setOnClickListener(view -> {
            if (passIsVisible) {
                binding.eyePassImage.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                //binding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.password.setTransformationMethod(new PasswordTransformationMethod());
                passIsVisible = false;
            } else {
                binding.eyePassImage.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
                //binding.passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.password.setTransformationMethod(null);
                passIsVisible = true;
            }
            binding.password.setSelection(binding.password.length());

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
            progressDialog.setMessage("please,wait while Login..");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();


                    } else {
                        progressDialog.dismiss();
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