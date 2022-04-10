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
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String game = "";
    private TextView highScoreLabel;
    private UserSharedPreference userSharedPreference;
    private TextView scoreLabel,coinsText;
    private ImageView exit;
    private ImageView gameOver;
    private ImageView moodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        scoreLabel = findViewById(R.id.scoreLabel3);
        highScoreLabel = findViewById(R.id.highScoreLabel);
        exit = findViewById(R.id.exit_btn);
        gameOver = findViewById(R.id.game_over_text);
        moodo = findViewById(R.id.profile_photo);
        coinsText = findViewById(R.id.coins_text);
        userSharedPreference = new UserSharedPreference(this);
        game = getIntent().getExtras().getString("GAME");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(firebaseUser.getUid());

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        long coins = getIntent().getLongExtra("COINS", 0);

        coinsText.setText(coins+"");

        storeCoins(coins);

        checkScore(score);


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

    private void checkScore(int score) {
        // High Score

        int highScore = userSharedPreference.getHighScore();

        if (score > highScore) {
            userSharedPreference.setHighScore(score);

            highScoreLabel.setText(score + "");
            reference.child("score").setValue(score + "");
        } else {
            highScoreLabel.setText(highScore + "");
        }
    }

    private void storeCoins(long coins) {
        userSharedPreference.setCoins(userSharedPreference.getCoins()+coins);
        //reference.child("coins").setValue(userSharedPreference.getCoins());

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
