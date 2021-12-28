package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.btnRegister.setOnClickListener(view -> {
            register();
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

                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {

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