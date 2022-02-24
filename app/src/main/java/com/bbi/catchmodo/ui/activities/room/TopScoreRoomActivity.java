package com.bbi.catchmodo.ui.activities.room;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.local.UserSharedPreference;
import com.bbi.catchmodo.data.model.UserRoomModel;
import com.bbi.catchmodo.databinding.ActivityTopScoreRoomBinding;
import com.bbi.catchmodo.ui.adapters.TopScoreRoomAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TopScoreRoomActivity extends AppCompatActivity {
    private ActivityTopScoreRoomBinding binding;
    private FirebaseFirestore db;
    private TopScoreRoomAdapter topScoreRoomAdapter;
    private UserSharedPreference sharedPreference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_score_room);

        sharedPreference = new UserSharedPreference(this);
        declaration();
        getTopUser();

        onclick();
    }

    private void onclick() {
        binding.back.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void declaration() {
        db = FirebaseFirestore.getInstance();
    }

    private void getTopUser() {

        Query query =
                db.collection("rooms").document(sharedPreference.getRoomId())
                        .collection("users").orderBy("score", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<UserRoomModel> options =
                new FirestoreRecyclerOptions.Builder<UserRoomModel>()
                        .setQuery(query, UserRoomModel.class)
                        .build();


        topScoreRoomAdapter = new TopScoreRoomAdapter(this, this, options);

     /*   binding.topScoreRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false){
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                // TODO
            });*/

        binding.topScoreRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));




        binding.topScoreRecycle.setAdapter(topScoreRoomAdapter);

        topScoreRoomAdapter.startListening();


    }
}