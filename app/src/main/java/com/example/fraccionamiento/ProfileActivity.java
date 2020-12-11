package com.example.fraccionamiento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fraccionamiento.Classes.FirebaseClass;
import com.example.fraccionamiento.Classes.UserClass;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private EditText txtName, txtLastName;
    private TextView lblFulName, txtEmail, built, dep, date, rent, maintenance;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CircleImageView imgProfile;
    private StorageReference refStorage = FirebaseStorage.getInstance().getReference(FirebaseClass.IMAGE_PROFILE+""+user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button btnEditInfo = findViewById(R.id.btnEditUserInfo);

        // Title and Up
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.my_profile);
        }

        txtName = findViewById(R.id.txtEditName);
        txtLastName = findViewById(R.id.txtEditUserLastName);
        txtEmail = findViewById(R.id.txtEditEmail);

        lblFulName = findViewById(R.id.profileFullName);
        built = findViewById(R.id.txtEditBuild);
        dep = findViewById(R.id.txtEditDept);
        date = findViewById(R.id.txtEditDay);

        rent = findViewById(R.id.txtEditRent);
        maintenance = findViewById(R.id.txtEditMaintenance);

        imgProfile = findViewById(R.id.profileImage);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });

        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areNotEmptyFields()){
                    HashMap<String,Object> userInfo = new HashMap<>();
                    userInfo.put("name", txtName.getText().toString());
                    userInfo.put("lastName", txtLastName.getText().toString());
                    dbRef.child(FirebaseClass.USERS).child(user.getUid()).updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, R.string.user_info_updated, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, R.string.user_info_failed_updated, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        dbRef.child(FirebaseClass.USERS).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                String fullName = userClass.getName()+" "+userClass.getLastName();
                lblFulName.setText(fullName);
                txtEmail.setText(userClass.getEmail());
                txtLastName.setText(userClass.getLastName());
                txtName.setText(userClass.getName());

                built.setText(userClass.getBuild());
                dep.setText(String.valueOf(userClass.getDeptNum()));
                date.setText(String.valueOf(userClass.getPayDay()));
                rent.setText(String.valueOf(userClass.getRent()));
                maintenance.setText(String.valueOf(userClass.getMaintenance()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        upDatedImageUrl();


    }

    private void upDatedImageUrl() {
        refStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileActivity.this).load(uri).into(imgProfile);
            }
        });
    }

    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, getString(R.string.select_application)),10);
    }

    private boolean areNotEmptyFields() {
        if(!txtName.getText().toString().isEmpty()){
            if(!txtLastName.getText().toString().isEmpty()){
                return true;
            }else {
                txtName.setError(getString(R.string.error_empty_field));
                return false;
            }
        }else {
            txtName.setError(getString(R.string.error_empty_field));
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Toast.makeText(ProfileActivity.this, R.string.uploading_data, Toast.LENGTH_LONG).show();
            Uri patch = data.getData();
            if(patch!=null){
                UploadTask uploadTask = refStorage.putFile(patch);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return refStorage.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Map<String, Object> setUser = new HashMap<>();
                            Glide.with(ProfileActivity.this).load(downloadUri).into(imgProfile);
                            setUser.put("urlImg", downloadUri.toString());
                            dbRef.child(FirebaseClass.USERS).child(user.getUid()).updateChildren(setUser);
                            Toast.makeText(ProfileActivity.this, R.string.success_upload_image,Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, R.string.error_loading_image,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else {
                Toast.makeText(this, R.string.error_loading_image, Toast.LENGTH_SHORT).show();
            }
        }
    }



}

