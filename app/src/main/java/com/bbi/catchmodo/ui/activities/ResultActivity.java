package com.bbi.catchmodo.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbi.catchmodo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultActivity extends AppCompatActivity {
    private AlertDialog.Builder dialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView highScoreLabel = findViewById(R.id.highScoreLabel);
        ImageView exit = findViewById(R.id.imageView);
        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText("YourScore: " + score);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



        // High Score
        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            // Update HighScore
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.apply();

            highScoreLabel.setText("High Score : " + score);
            //   highScoreLabel.setText(getString(R.string.high_score, score));
            reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(firebaseUser.getUid()).child("score");
            reference.setValue(score+"");

        } else {
            highScoreLabel.setText("High Score : " + highScore);
            //  highScoreLabel.setText(getString(R.string.high_score, highScore));

        }



        exit.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(ResultActivity.this, WelcomeActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });
    }

    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit From Game");
        dialog.setMessage("You Are sure Exit ?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitAppCLICK(null);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog dialog2 = dialog.create();
        dialog2.show();

    }
}