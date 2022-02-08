package com.bbi.catchmodo.ui.activities.room;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RoomModel;
import com.bbi.catchmodo.databinding.ActivityRoomBinding;
import com.bbi.catchmodo.ui.adapters.RoomsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

        declaration();
        retrieveLeads();


    }



    private void declaration() {
        db = FirebaseFirestore.getInstance();
    }

    private void retrieveLeads() {

        Query query = db
                .collection("rooms");
        // .whereArrayContains("teams", sharedPreference!!.getUserDetails()!!.id!!)

        FirestoreRecyclerOptions<RoomModel> options =
                new FirestoreRecyclerOptions.Builder<RoomModel>()
                        .setQuery(query, RoomModel.class)
                        .build();

        roomsAdapter = new RoomsAdapter(this,this,options);
        binding.roomsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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