package com.bbi.catchmodo.ui.fragments.room_password;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.local.UserSharedPreference;
import com.bbi.catchmodo.data.model.RoomModel;
import com.bbi.catchmodo.data.model.UserRoomModel;
import com.bbi.catchmodo.databinding.FragmentRoomPasswordDialogBinding;
import com.bbi.catchmodo.ui.activities.room.StartRoomActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RoomPasswordDialogFragment extends DialogFragment {

    private FragmentRoomPasswordDialogBinding binding;
    private UserSharedPreference userSharedPreference;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;



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

        declaration();
        onCLick();

    }

    private void onCLick() {
        binding.saveBtn.setOnClickListener(view1 -> {

            checkPassword();

        });
    }

    private void checkPassword() {
        progressDialog.show();
        db.collection("rooms")
                .document(userSharedPreference.getRoomId()).get().addOnCompleteListener(task -> {
            RoomModel roomModel = task.getResult().toObject(RoomModel.class);
            if (roomModel.getPassword().equals(binding.editTextPassword.getText().toString())) {
                checkExisting();
            }else{
                progressDialog.dismiss();
                Toast.makeText(getContext(),"password Not Correct",Toast.LENGTH_SHORT).show();
            }
        });
        //checkExisting();
    }

    private void declaration() {
        userSharedPreference = new UserSharedPreference(getContext());
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Enter To Room");
        progressDialog.setMessage("please wait");

    }

    private void checkExisting() {
        String userId = userSharedPreference.getUserDetails().getId();
        String RoomId = userSharedPreference.getRoomId();
        Query docRef = db.collection("rooms")
                .document(RoomId).collection("users").whereEqualTo("user_id", userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserRoomModel userRoomModel = null;
                for (QueryDocumentSnapshot document : task.getResult()) {

                    userRoomModel = document.toObject(UserRoomModel.class);
                    Log.d("nooor", "DocumentSnapshot data: " + userRoomModel.getScore());
                }

                if (userRoomModel == null) {
                    createNewUserInRoom();
                } else {
                    goToNextPage();
                }
            } else {
                progressDialog.dismiss();
                Log.d("nooor", "get failed with ", task.getException());
            }
        });
    }

    private void createNewUserInRoom() {

        UserRoomModel userRoomModel = new UserRoomModel(userSharedPreference.getUserDetails().getId(), 0, "");

        DocumentReference documentReference = db.collection("rooms")
                .document(userSharedPreference.getRoomId()).collection("users")
                .document();

        userRoomModel.setId(documentReference.getId());

        documentReference.set(userRoomModel).addOnCompleteListener(task -> {
            goToNextPage();
        });
    }

    private void goToNextPage() {
        progressDialog.dismiss();
        Intent intent = new Intent(getContext(), StartRoomActivity.class);
        startActivity(intent);
        dismiss();
    }
}