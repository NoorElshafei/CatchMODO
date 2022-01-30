package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.databinding.ActivityTopUserBinding;
import com.bbi.catchmodo.ui.adapters.TopUserAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TopUser extends AppCompatActivity {
    ActivityTopUserBinding binding;
    TopUserAdapter adapter;
    ArrayList<RegisterModel> registerModelArrayList;
    FirebaseUser firebaseUser;

    DatabaseReference reference;
    RegisterModel registerModel;
    ProgressDialog progressDialog;
    ArrayList TopUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_user);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        registerModelArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
     //   Glide.with(TopUser.this).load(R.drawable.logo_gif).into(binding.logo);
        TopUser  =new ArrayList();
        binding.backIcon.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        reference = FirebaseDatabase.getInstance().getReference("UserRegister");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    registerModel = snapshot1.getValue(RegisterModel.class);
                    progressDialog.dismiss();
                    registerModelArrayList.add(registerModel);


                }
                Collections.sort(registerModelArrayList, (lhs, rhs) -> Integer.parseInt(rhs.getScore()) - Integer.parseInt(lhs.getScore()));
                for(int i=0;i<=9;i++){

                    TopUser.add(registerModelArrayList.get(i));
                }
                adapter = new TopUserAdapter(TopUser.this, TopUser);
                binding.recycle.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}