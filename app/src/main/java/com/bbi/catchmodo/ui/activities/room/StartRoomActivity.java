package com.bbi.catchmodo.ui.activities.room;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.databinding.ActivityStartBinding;
import com.bbi.catchmodo.databinding.ActivityStartRoomBinding;
import com.bbi.catchmodo.ui.activities.MainActivity;
import com.bbi.catchmodo.ui.activities.ProfileActivity;
import com.bbi.catchmodo.ui.activities.StartActivity;
import com.bbi.catchmodo.ui.activities.TopUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StartRoomActivity extends AppCompatActivity {
    private ActivityStartRoomBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_room);

        displayGif(R.drawable.sticker_gif, binding.stickerGif);

        Glide.with(StartRoomActivity.this).load(R.drawable.fly_nuts1).into(binding.fly1);
        Glide.with(StartRoomActivity.this).load(R.drawable.fly_nuts2).into(binding.fly2);
        Glide.with(StartRoomActivity.this).load(R.drawable.fly_nuts3).into(binding.fly3);
        Glide.with(StartRoomActivity.this).load(R.drawable.fly_nuts3).into(binding.fly4);
        Glide.with(StartRoomActivity.this).load(R.drawable.fly_nuts4).into(binding.fly5);



        onClick();
    }


    private void onClick() {
        binding.startBtn.setOnClickListener(view -> {
            startActivity(new Intent(StartRoomActivity.this, MainRoomActivity.class));
        });
        binding.exitBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartRoomActivity.this, StartActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.scoreBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartRoomActivity.this, TopScoreRoomActivity.class);
            startActivity(intent);
        });

    }

    private void displayGif(Integer drawable, ImageView imageView) {
        Glide.with(getApplicationContext())
                .asGif()
                .load(drawable) // Replace with a valid url
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1); // Place your loop count here.
                        return false;
                    }
                })
                .into(imageView);
    }
}