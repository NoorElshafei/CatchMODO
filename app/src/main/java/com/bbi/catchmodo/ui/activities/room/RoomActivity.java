package com.bbi.catchmodo.ui.activities.room;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RoomModel;
import com.bbi.catchmodo.databinding.ActivityRoomBinding;
import com.bbi.catchmodo.ui.adapters.RoomsAdapter;
import com.bbi.catchmodo.util.Language;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class RoomActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RoomsAdapter roomsAdapter;
    private ActivityRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room);
        Language.changeBackDependsLanguage(binding.back,getApplicationContext());
        declaration();
        retrieveLeads();
        onClick();

    }

    private void onClick() {
        binding.back.setOnClickListener(view -> {
            onBackPressed();
        });
    }


    private void declaration() {
        db = FirebaseFirestore.getInstance();
    }

    private void retrieveLeads() {

        Query query = db
                .collection("rooms")
                .whereEqualTo("status", "open");
        // .whereArrayContains("teams", sharedPreference!!.getUserDetails()!!.id!!)

        FirestoreRecyclerOptions<RoomModel> options =
                new FirestoreRecyclerOptions.Builder<RoomModel>()
                        .setQuery(query, RoomModel.class)
                        .build();

        roomsAdapter = new RoomsAdapter(this, this, options);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        binding.roomsRecycler.setLayoutManager(layoutManager);
        binding.roomsRecycler.setAdapter(roomsAdapter);

        roomsAdapter.startListening();

    }
}
  /*  Query query =
            db.collection("Marketing portal").orderBy("createdTime", Query.Direction.DESCENDING)

        query.addSnapshotListener((value, error) -> {
                if (error != null) {
                // Handle error
                //...
                return;
                }
                List<RoomModel> roomModelList = value.toObjects(RoomModel.class);

        roomModelList.forEach(roomModel -> {

        });


        lessonsAdapter = LeadsAdapter(leads)
        binding!!.leadsRecycler.layoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.leadsRecycler.adapter = lessonsAdapter

        });*/