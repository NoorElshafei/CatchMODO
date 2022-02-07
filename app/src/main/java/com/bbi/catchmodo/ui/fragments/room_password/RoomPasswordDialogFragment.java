package com.bbi.catchmodo.ui.fragments.room_password;

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

public class RoomPasswordDialogFragment extends DialogFragment {

    private RoomPasswordDialogViewModel mViewModel;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RoomPasswordDialogViewModel.class);
        // TODO: Use the ViewModel
    }

}