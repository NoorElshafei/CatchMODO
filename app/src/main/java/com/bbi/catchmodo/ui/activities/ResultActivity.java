package com.bbi.catchmodo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.local.UserSharedPreference;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultActivity extends AppCompatActivity {
    private AlertDialog.Builder dialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private String game = "";
    private UserSharedPreference userSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView scoreLabel = findViewById(R.id.scoreLabel3);
        TextView highScoreLabel = findViewById(R.id.highScoreLabel);
        ImageView exit = findViewById(R.id.exit_btn);
        ImageView gameOver = findViewById(R.id.game_over_text);
        ImageView moodo = findViewById(R.id.profile_photo);
        userSharedPreference = new UserSharedPreference(this);
        game = getIntent().getExtras().getString("GAME");

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // High Score
        ;
        int highScore = userSharedPreference.getHighScore();

        if (score > highScore) {
            // Update HighScore
            userSharedPreference.setHighScore(score);

            highScoreLabel.setText(score + "");
            //   highScoreLabel.setText(getString(R.string.high_score, score));
            reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(firebaseUser.getUid()).child("score");
            reference.setValue(score + "");

        } else {
            highScoreLabel.setText(highScore + "");
            //  highScoreLabel.setText(getString(R.string.high_score, highScore));

        }


        if (game.equals("timeOut")) {
            Glide.with(ResultActivity.this).load(R.drawable.time_out).into(gameOver);
            Glide.with(ResultActivity.this).load(R.drawable.fun_moodo).into(moodo);
        } else {
            Glide.with(ResultActivity.this).load(R.drawable.game_over).into(gameOver);
        }


        exit.setOnClickListener(view -> {
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void topScore(View view) {
        startActivity(new Intent(getApplicationContext(), TopUser.class));
    }


    public void exitAppCLICK(View view) {
        finishAffinity();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
