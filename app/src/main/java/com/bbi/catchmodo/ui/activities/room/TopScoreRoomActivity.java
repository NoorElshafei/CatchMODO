package com.bbi.catchmodo.ui.activities.room;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.data.model.UserRoomModel;
import com.bbi.catchmodo.databinding.ActivityTopScoreRoomBinding;
import com.bbi.catchmodo.ui.adapters.TopScoreRoomAdapter;
import com.bbi.catchmodo.ui.adapters.TopUserAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class TopScoreRoomActivity extends AppCompatActivity {
    private ActivityTopScoreRoomBinding binding;
    private TopUserAdapter adapter;
    private ArrayList<RegisterModel> allUser;
    private FirebaseUser firebaseUser;
    private LinearLayout linearLayout;
    private DatabaseReference reference;
    private RegisterModel registerModel;
    private ProgressDialog progressDialog;
    private ArrayList<RegisterModel> TopUser;
    private RegisterModel userModel;
    private ArrayList<String> TopTenStrings;
    private int i;
    private FirebaseFirestore db;
    private TopScoreRoomAdapter topScoreRoomAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_score_room);
        declaration();
        getTopUser();
    }

    private void declaration() {
        db = FirebaseFirestore.getInstance();
    }

    private void getTopUser() {

        Query query =
                db.collection("rooms").document("Bcjd3RW3Vu4LGXlMWwQj")
                        .collection("users").orderBy("score", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<UserRoomModel> options =
                new FirestoreRecyclerOptions.Builder<UserRoomModel>()
                        .setQuery(query, UserRoomModel.class)
                        .build();

        topScoreRoomAdapter = new TopScoreRoomAdapter(this, this, options);
        binding.topScoreRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.topScoreRecycle.setAdapter(topScoreRoomAdapter);

        topScoreRoomAdapter.startListening();


    }
}