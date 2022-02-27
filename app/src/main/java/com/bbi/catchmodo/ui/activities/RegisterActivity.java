package com.bbi.catchmodo.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.local.UserSharedPreference;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.databinding.ActivityRegisterBinding;
import com.bbi.catchmodo.util.Language;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    // Declare variables
    private Button mButtonFacebook;

    private CallbackManager mCallbackManager;
    private ActivityRegisterBinding binding;
    private String email_pattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private boolean passIsVisible = false;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private String image_url;
    private UserSharedPreference userSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        Language.changeBackDependsLanguage(binding.back,getApplicationContext());
        progressDialog = new ProgressDialog(this);
        Log.d(TAG, "onSuccess: " + image_url);

        userSharedPreference = new UserSharedPreference(this);


        FacebookSdk.sdkInitialize(getApplicationContext());
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("898536207310-rbdkac0kdppab1lr6u5585svpg55gno1.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog.setMessage("please,waiting while SignUp ..");
                progressDialog.setCanceledOnTouchOutside(false);
                image_url = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large";
                signUpWithGoogleAndFacebook(loginResult.getAccessToken().getToken(), image_url, "facebook");

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        binding.btnRegister.setOnClickListener(view -> {
            register();
        });
        binding.back.setOnClickListener(view -> {
            onBackPressed();
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

        binding.buttonGoogle.setOnClickListener(view -> {
            googleSignUp();
        });


    }


    public void register() {
        String email = binding.email.getText().toString();
        String name = binding.userName.getText().toString();
        String phone = binding.phone.getText().toString();
        String password = binding.password.getText().toString();


        if (email.isEmpty() || name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();

        } else if (!email.matches(email_pattern)) {
            //  binding.email.setError("please,enter email context right");
            Toast.makeText(this, "please,enter email context right", Toast.LENGTH_LONG).show();
        } else if (password.isEmpty() || password.length() < 6) {
            // binding.password.setError("please,Enter the password correctly");
            Toast.makeText(this, "please,Enter the password correctly", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("please,wait while Registration..");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    saveUserInRealtime(name, email, phone, "", "form");
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }
    }

    private void googleSignUp() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                signUpWithGoogleAndFacebook(account.getIdToken(), "", "google");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }


    private void signUpWithGoogleAndFacebook(String token, String image_url, String platform) {
        progressDialog.show();
        AuthCredential credential;

        if (platform.equals("google"))
            credential = GoogleAuthProvider.getCredential(token, null);
        else
            credential = FacebookAuthProvider.getCredential(token);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String email = task.getResult().getUser().getEmail();
                            String userName = task.getResult().getUser().getDisplayName();

                            if (platform.equals("google")) {
                                String imageUrl = task.getResult().getUser().getPhotoUrl().toString();
                                saveUserInRealtime(userName, email, "", imageUrl, "google");

                            } else
                                saveUserInRealtime(userName, email, "", image_url, "facebook");

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void sendToNextPage(String platform) {
        progressDialog.dismiss();
        Intent intent;
        if (platform.equals("google")) {
            intent = new Intent(RegisterActivity.this, SignUpByGoogle.class);
        } else if (platform.equals("facebook")) {
            intent = new Intent(RegisterActivity.this, SignUpByFacebook.class);
        } else {
            intent = new Intent(RegisterActivity.this, StartActivity.class);
        }
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void saveUserInRealtime(String name, String email, String phone, String imageUrl, String type) {
        firebaseUser = firebaseAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid);
        RegisterModel registerModel = new RegisterModel(userid, name, email, phone, "0", imageUrl);

        userSharedPreference.add(registerModel);
        userSharedPreference.setHighScore(0);


        reference.setValue(registerModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userSharedPreference.add(registerModel);
                if (type.equals("form"))
                    sendToNextPage("form");
                else if (type.equals("google"))
                    sendToNextPage("google");
                else
                    sendToNextPage("facebook");

            } else {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}