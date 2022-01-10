package com.bbi.catchmodo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.databinding.ActivityTopUserBinding;
import com.bbi.catchmodo.ui.adapters.TopUserAdapter;
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

                    adapter = new TopUserAdapter(TopUser.this, registerModelArrayList);
                    binding.recycle.setAdapter(adapter);
                    Collections.sort(registerModelArrayList, new Comparator<RegisterModel>() {
                        @Override
                        public int compare(RegisterModel lhs, RegisterModel rhs) {
                            return Integer.parseInt(rhs.getScore()) - Integer.parseInt(lhs.getScore());


                        }});

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}