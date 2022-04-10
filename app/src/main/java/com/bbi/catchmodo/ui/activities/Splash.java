package com.bbi.catchmodo.ui.activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.databinding.ActivitySplashBinding;
import com.bbi.catchmodo.util.ContextCustomize;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash extends AppCompatActivity {
    private FirebaseUser firebaseUser;

    private ActivitySplashBinding binding;
    private int MY_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (ContextCustomize.isValidContextForGlide(Splash.this)) {
            displayGif(R.drawable.sticker_gif, binding.stickerGif);
            displayGif(R.drawable.logo2, binding.mLogoGif);
            Glide.with(Splash.this).load(R.drawable.fly_nuts1).into(binding.fly1);
            Glide.with(Splash.this).load(R.drawable.fly_nuts2).into(binding.fly2);
            Glide.with(Splash.this).load(R.drawable.fly_nuts3).into(binding.fly3);
            Glide.with(Splash.this).load(R.drawable.fly_nuts3).into(binding.fly4);
            Glide.with(Splash.this).load(R.drawable.fly_nuts4).into(binding.fly5);
        }
        new Handler().postDelayed(() -> {
            checkAppUpdate();

        }, 3000);

        //getReleaseHashKey();


    }

    private void checkAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.update_app))
                        .setMessage(getString(R.string.you_should_update_app))
                        .setIcon(getDrawable(R.drawable.ic_baseline_update_24))
                        .setPositiveButton(getString(R.string.update), (dialog, whichButton) -> {
                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                        appUpdateInfo,
                                        // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                        AppUpdateType.IMMEDIATE,
                                        // The current activity making the update request.
                                        this,
                                        // Include a request code to later monitor this update request.
                                        MY_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }

                        })
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                            nextCheck();
                            dialog.dismiss();
                        }).show();
            } else {
                nextCheck();
                Log.d("sadsadsadas11", "onActivityCreated: ");
            }
        }).addOnFailureListener(command -> {
            Log.d("sadsadsadas", "onActivityCreated: " + command.getMessage());
            nextCheck();
        });
    }

    private void nextCheck() {
        Intent intent;
        if (firebaseUser == null) {
            intent = new Intent(Splash.this, WelcomeActivity.class);
        } else {
            intent = new Intent(Splash.this, StartActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
        finish();
    }

    private void displayGif(Integer drawable, ImageView imageView) {

        Glide.with(this)
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
                }).into(imageView);

    }

    private void getReleaseHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("releaseHashKey", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


}