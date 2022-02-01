package com.bbi.catchmodo.ui.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.databinding.FragmentEditNameBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditNameFragment extends DialogFragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FragmentEditNameBinding binding;



    public EditNameFragment() {
        // Required empty public constructor
    }

    public static EditNameFragment newInstance() {
        EditNameFragment fragment = new EditNameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_name, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        Bundle bundle = this.getArguments();

        if(bundle != null){
            String name  = bundle.getString("name");
            binding.editText4.setText(name);
        }

        return binding.getRoot();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                String name = binding.editText4.getText().toString();
                FirebaseDatabase.getInstance().getReference("UserRegister")
                        .child(firebaseUser.getUid()).child("user_name").setValue(name).addOnCompleteListener(task -> {
                  dismiss();
                });



            }

        });

    }
}