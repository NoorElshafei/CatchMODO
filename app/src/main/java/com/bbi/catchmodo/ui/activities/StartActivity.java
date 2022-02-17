package com.bbi.catchmodo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.databinding.ActivityStartBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {
    private ActivityStartBinding binding;
    private AlertDialog.Builder dialog;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private String userid;
    private RegisterModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        displayGif(R.drawable.sticker_gif, binding.stickerGif);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userid = firebaseUser.getUid();

        Glide.with(StartActivity.this).load(R.drawable.fly_nuts1).into(binding.fly1);
        Glide.with(StartActivity.this).load(R.drawable.fly_nuts2).into(binding.fly2);
        Glide.with(StartActivity.this).load(R.drawable.fly_nuts3).into(binding.fly3);
        Glide.with(StartActivity.this).load(R.drawable.fly_nuts3).into(binding.fly4);
        Glide.with(StartActivity.this).load(R.drawable.fly_nuts4).into(binding.fly5);
        getImageProfile();


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

    @Override
    public void onBackPressed() {

        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit From Game");
        dialog.setMessage("You Are sure Exit ?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Exit", (dialog, which) -> {

            finishAffinity();
            System.exit(0);
        });
        dialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog dialog2 = dialog.create();
        dialog2.show();

    }

    private void getImageProfile() {
        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userModel = snapshot.getValue(RegisterModel.class);
                Log.d("amany", "onCreate: " + userModel);
                Glide.with(StartActivity.this).load(userModel.getImage_url()).placeholder(R.drawable.fun_moodo).into(binding.profilePhoto);
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
