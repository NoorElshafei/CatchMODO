package com.bbi.catchmodo.ui.activities.room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.local.UserSharedPreference;
import com.bbi.catchmodo.data.model.UserRoomModel;
import com.bbi.catchmodo.databinding.ActivityResultRoomBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ResultRoomActivity extends AppCompatActivity {
    private String game = "";
    private UserSharedPreference userSharedPreference;
    private ActivityResultRoomBinding binding;
    private FirebaseFirestore db;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result_room);


        game = getIntent().getExtras().getString("GAME");

        score = getIntent().getIntExtra("SCORE", 0);
        binding.scoreLabel3.setText(score + "");

        // High Score
        checkHighScore();


        if (game.equals("timeOut")) {
            Glide.with(ResultRoomActivity.this).load(R.drawable.time_out).into(binding.gameOverText);
            Glide.with(ResultRoomActivity.this).load(R.drawable.fun_moodo).into(binding.profilePhoto);
        } else {
            Glide.with(ResultRoomActivity.this).load(R.drawable.game_over).into(binding.gameOverText);
        }


        binding.exitBtn.setOnClickListener(view -> {
            onBackPressed();

        });
    }

    private void checkHighScore() {


        userSharedPreference = new UserSharedPreference(this);
        db = FirebaseFirestore.getInstance();
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
                long highScore = userRoomModel.getScore();
                //TODO Tomorrow
                if (score > highScore) {
                    // Update HighScore
                    userRoomModel.setScore(score);
                    db.collection("rooms")
                            .document(userSharedPreference.getRoomId()).collection("users")
                            .document(userRoomModel.getId()).set(userRoomModel);

                    binding.highScoreLabel.setText(score + "");

                } else {
                    binding.highScoreLabel.setText(highScore + "");
                }
            } else {
                Log.d("nooor", "get failed with ", task.getException());
            }


        });
    }

    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainRoomActivity.class));
        finish();
    }

    public void topScore(View view) {
        startActivity(new Intent(getApplicationContext(), TopScoreRoomActivity.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}