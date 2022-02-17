package com.bbi.catchmodo.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.data.model.UserSharedPreference;
import com.bbi.catchmodo.databinding.ActivityLoginBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    //TODO  17/2
    ActivityLoginBinding binding;
    String email_pattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    private boolean passIsVisible = false;
    private CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private UserSharedPreference userSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        userSharedPreference = new UserSharedPreference(this);
        binding.btnLogin.setOnClickListener(view -> {
            login();
        });
        binding.back.setOnClickListener(view -> {
            onBackPressed();

        });
        binding.forgetPassword.setOnClickListener(view -> {
            Intent intent2 = new Intent(LoginActivity.this, ForgetPassword.class);
            startActivity(intent2);

        });
        binding.eyePassImage.setOnClickListener(view -> {
            if (passIsVisible) {
                binding.eyePassImage.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                //binding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.password.setTransformationMethod(new PasswordTransformationMethod());
                passIsVisible = false;
            } else {
                binding.eyePassImage.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
                //binding.passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.password.setTransformationMethod(null);
                passIsVisible = true;
            }
            binding.password.setSelection(binding.password.length());

        });
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                firebaseAuthWithGoogleAndFacebook(loginResult.getAccessToken().getToken(),"facebook");

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("898536207310-rbdkac0kdppab1lr6u5585svpg55gno1.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.buttonGoogle.setOnClickListener(view -> {

            signIn();
        });

    }

    public void login() {
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        if (!email.matches(email_pattern)) {

            Toast.makeText(LoginActivity.this, "please,Enter your Email", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty() || password.length() < 6) {

            Toast.makeText(LoginActivity.this, "please,Enter your Password", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("please,wait while Login..");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    nextPage();
                } else {
                    progressDialog.dismiss();
                    Log.d(TAG, "onComplete1: " + task.getException().getMessage());
                    Toast.makeText(LoginActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogleAndFacebook(account.getIdToken(),"google");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }




    private void firebaseAuthWithGoogleAndFacebook(String idToken, String platform) {
        progressDialog.setMessage("please,waiting  while SignIn.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        AuthCredential credential;
        if (platform.equals("google"))
            credential = GoogleAuthProvider.getCredential(idToken, null);
        else
            credential = FacebookAuthProvider.getCredential(idToken);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        nextPage();

                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Log.w(TAG, "signInWithCredential:failure", task.getException());

                    }
                });
    }





    private void nextPage() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserRegister");
        ref.orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String keys = "";
                RegisterModel userModel = null;
                for (DataSnapshot datas : snapshot.getChildren()) {
                    userModel = datas.getValue(RegisterModel.class);

                    keys = datas.getKey();
                }
                if (!keys.equals("") && keys.equals(firebaseAuth.getCurrentUser().getUid())) {
                    progressDialog.dismiss();
                    userSharedPreference.add(userModel);
                    int score = Integer.parseInt(userModel.getScore());
                    userSharedPreference.setHighScore(score);

                    Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "please sign up to continue", Toast.LENGTH_LONG).show();
                    LoginManager.getInstance().logOut();
                    firebaseAuth.signOut();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}