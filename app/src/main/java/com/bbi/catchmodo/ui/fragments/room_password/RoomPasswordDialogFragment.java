package com.bbi.catchmodo.ui.fragments.room_password;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.databinding.FragmentRoomPasswordDialogBinding;
import com.bbi.catchmodo.ui.activities.room.StartRoomActivity;

public class RoomPasswordDialogFragment extends DialogFragment {

    private FragmentRoomPasswordDialogBinding binding;


    public static RoomPasswordDialogFragment newInstance() {
        return new RoomPasswordDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_password_dialog, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveBtn.setOnClickListener(view1 ->{
            startActivity(new Intent(getContext(), StartRoomActivity.class));
            dismiss();
        });
    }
}