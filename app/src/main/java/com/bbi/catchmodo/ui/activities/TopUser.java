package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
    ArrayList<RegisterModel> allUser;
    FirebaseUser firebaseUser;
LinearLayout linearLayout;
    DatabaseReference reference;
    RegisterModel registerModel;
    ProgressDialog progressDialog;
    ArrayList<RegisterModel> TopUser;
    RegisterModel userModel;
    ArrayList<String> TopTenStrings;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_user);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        allUser = new ArrayList<>();
        TopTenStrings=new ArrayList<>();
        linearLayout=findViewById(R.id.linearLayout6);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //   Glide.with(TopUser.this).load(R.drawable.logo_gif).into(binding.logo);
        TopUser = new ArrayList();
        binding.back.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        getMyData();
        getTopUser();


binding.constraint1.setVisibility(View.INVISIBLE);
    }


    private void getMyData() {

        String userid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userModel = snapshot.getValue(RegisterModel.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTopUser() {

        reference = FirebaseDatabase.getInstance().getReference("UserRegister");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allUser.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    registerModel = snapshot1.getValue(RegisterModel.class);
                    progressDialog.dismiss();
                    allUser.add(registerModel);


                }

                Collections.sort(allUser, (lhs, rhs) -> Integer.parseInt(rhs.getScore()) - Integer.parseInt(lhs.getScore()));
                for ( i= 0; i <= 9; i++) {

                    TopUser.add(allUser.get(i));

                    TopTenStrings.add(allUser.get(i).getId());
                }
                adapter = new TopUserAdapter(TopUser.this, TopUser);
                binding.recycle.setAdapter(adapter);
                if (!TopTenStrings.contains(firebaseUser.getUid())) {
                    binding.name.setText(userModel.getUser_name()+'('+"You"+')');
                    binding.score.setText(userModel.getScore());
                    Glide.with(getApplicationContext()).load(userModel.getImage_url()).placeholder(R.drawable.moodo_icon).into(binding.image);
                    binding.constraint1.setVisibility(View.VISIBLE);

                }else {
                    binding.constraint1.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}