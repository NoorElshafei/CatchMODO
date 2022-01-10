package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.databinding.ActivityRegisterBinding;
import com.bbi.catchmodo.databinding.ActivityTopUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    String email_pattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String score = "0";
    ProgressDialog progressDialog;
    private boolean passIsVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        binding.btnRegister.setOnClickListener(view -> {
            register();
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



    public void register() {
        String email = binding.email.getText().toString();
        String name = binding.userName.getText().toString();
        String phone = binding.phone.getText().toString();
        String password = binding.password.getText().toString();



        if (email.isEmpty() || name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
        }
        if (!email.matches(email_pattern)) {
            binding.email.setError("please,enter email context right");
        } else if (password.isEmpty() || password.length() < 6) {
            binding.password.setError("please,Enter the password correctly");
        } else {
            progressDialog.setMessage("please,wait while Registration..");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseUser = firebaseAuth.getCurrentUser();
                        String userid = firebaseUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid);
                        RegisterModel registerModel = new RegisterModel(userid, name, email,phone,score);
                        reference.setValue(registerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    sendNextPage();
                                    finish();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }


    }

    public void sendNextPage() {
        Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}