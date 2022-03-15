package com.bbi.catchmodo.ui.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.ui.fragments.EditNameFragment;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    private TextView name, email, phone, score;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userid;
    private ProgressDialog progressDialog;
    private ImageView profile, edit_name, back, change_photo;
    private ImageView logOut;
    private FirebaseAuth firebaseAuth;
    private static final int IMAGE_REQUEST = 1;
    public static final int PICK_IMAGE = 1;
    private StorageTask uploadTask;
    private RegisterModel userModel;
    private Uri imageUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone3);
        score = findViewById(R.id.score);
        profile = findViewById(R.id.profile_photo);
        logOut = findViewById(R.id.logOut);
        edit_name = findViewById(R.id.edit_name);
        back = findViewById(R.id.back);
        change_photo = findViewById(R.id.camera);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        userid = firebaseUser.getUid();
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                userModel = snapshot.getValue(RegisterModel.class);
                name.setText(userModel.getUser_name());
                email.setText(userModel.getEmail());
                phone.setText(userModel.getPhone());
                score.setText(userModel.getScore());
                Glide.with(getApplicationContext()).load(userModel.getImage_url()).placeholder(R.drawable.fun_moodo).into(profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logOut.setOnClickListener(view -> {
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        edit_name.setOnClickListener(view -> {
            showEditDialog();
        });
        back.setOnClickListener(view ->
                onBackPressed());
        change_photo.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST);
        });


    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditNameFragment editFragment = EditNameFragment.newInstance();
        editFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), " upload in progress", Toast.LENGTH_SHORT).show();

            } else {
                uploadImage();
            }
        }


    }

    private void uploadImage() {

        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "."
                    + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }


            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    reference = FirebaseDatabase.getInstance().getReference("UserRegister").child(userid).child("image_url");
                    reference.setValue(downloadUri.toString());

                } else {
                    Toast.makeText(getApplicationContext(), "Failed:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();


            });


        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}