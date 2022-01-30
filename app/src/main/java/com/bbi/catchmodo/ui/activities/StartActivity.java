package com.bbi.catchmodo.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.databinding.ActivityLoginBinding;
import com.bbi.catchmodo.databinding.ActivityStartBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
    ActivityStartBinding binding;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        displayGif(R.drawable.sticker_gif,binding.stickerGif);



        Glide.with(StartActivity.this).load(R.drawable.fly_nuts1).into(binding.fly1);
        Glide.with(StartActivity.this).load(R.drawable.fly_nuts2).into(binding.fly2);
        Glide.with(StartActivity.this).load(R.drawable.fly_nuts3).into(binding.fly3);
        Glide.with(StartActivity.this).load(R.drawable.fly_nuts3).into(binding.fly4);
        Glide.with(StartActivity.this).load(R.drawable.fly_nuts4).into(binding.fly5);



        binding.startBtn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
        binding.exitBtn.setOnClickListener(view -> {

            finishAffinity();
            System.exit(0);
        });
        binding.profileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        binding.scoreBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartActivity.this, TopUser.class);
            startActivity(intent);
        });
    }

private void displayGif(Integer drawable,ImageView imageView){
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
    @Override
    public void onBackPressed() {

        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit From Game");
        dialog.setMessage("You Are sure Exit ?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
                System.exit(0);
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
