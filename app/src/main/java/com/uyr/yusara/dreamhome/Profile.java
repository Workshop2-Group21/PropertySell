package com.uyr.yusara.dreamhome;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.uyr.yusara.dreamhome.Admin.AdminMainMenu;
import com.uyr.yusara.dreamhome.Customer.MainActivityCustomer;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private TextView profiletitlename;
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextphone;
    private EditText editTextdescription;

    // Code gambar
    private CircleImageView ProfileImage;
    final static int gallerypick = 1;
    private StorageReference UserProfileImageRef;

    private DocumentReference SettinguserRef;
    private FirebaseAuth mAuth;

    private String currentUserid;

    private android.support.v7.widget.Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Code gambar
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");
        ProfileImage    = findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        SettinguserRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);


        //profiletitlename = (TextView) findViewById(R.id.profiletitlename);

        editTextEmail    = findViewById(R.id.edittext_email);
        editTextName   = findViewById(R.id.edittext_fullname);
        editTextphone    = findViewById(R.id.edittext_phone);
        editTextdescription = findViewById(R.id.editTextdescription);

        findViewById(R.id.button_update).setOnClickListener(this);
        findViewById(R.id.profile_image).setOnClickListener(this);

        SettinguserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.i("LOGGER",document.getString("email"));

                        String myprofilenametitle = document.getString("name");
                        String myProfileemail = document.getString("email");
                        String myProfilename = document.getString("name");
                        String myProfilephone = document.getString("phone");
                        String myProfiledescription = document.getString("profiledescription");

                        editTextEmail.setText(myProfileemail);
                        editTextName.setText(myProfilename);
                        editTextphone.setText(myProfilephone);
                        editTextdescription.setText(myProfiledescription);
                        //profiletitlename.setText(myprofilenametitle);

                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

        //DIsplay back Image
        SettinguserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("profileimage2");

                    //Picasso.get().load(image).placeholder(R.drawable.cc).into(ProfileImage);
                    Glide.with(Profile.this).load(image).into(ProfileImage);
                }

            }
        });

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" My Profile ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }


    public void UpdateProfileInfo()
    {
        String email = editTextEmail.getText().toString();
        String name = editTextName.getText().toString();
        String phone = editTextphone.getText().toString();
        String description = editTextdescription.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email .... ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name .... ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number .... ", Toast.LENGTH_SHORT).show();
        }
        else
            {
                UpdateProfileInfo(email,name,phone,description);
            }

    }

    private void UpdateProfileInfo(String email, String name, String phone, String description)
    {
        HashMap userMap = new HashMap();
        userMap.put("email", email);
        userMap.put("name", name);
        userMap.put("phone", phone);
        userMap.put("profiledescription", description);
        SettinguserRef.update(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    SendUserToMainActivity();
                    Toast.makeText(Profile.this, "Account update successfully ", Toast.LENGTH_SHORT).show();
                    SendUserToMainActivity();
                }
                else
                    {
                        Toast.makeText(Profile.this, "Account error update ", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void SendUserToMainActivity()
    {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<DocumentSnapshot> xx = database.collection("Users").document(uid).get();

        xx.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String role;
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    role = doc.get("role").toString();

                    if(role.equals("Agent")){
                        Intent intent = new Intent(Profile.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (role.equals("Customer")) {
                        Intent intent = new Intent(Profile.this, MainActivityCustomer.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (role.equals("Admin")) {
                        Intent intent = new Intent(Profile.this, AdminMainMenu.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Profile.this, "Unable to find roles", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void toGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallerypick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallerypick && resultCode == RESULT_OK && data!= null)
        {
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Toast.makeText(Profile.this, "msuk requestcode", Toast.LENGTH_LONG).show();

            if (resultCode == RESULT_OK) {
                Toast.makeText(Profile.this, "result masuk", Toast.LENGTH_LONG).show();
                Uri resultUri = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(currentUserid + ".jpg");

                //save the crop inside firebase storage
                //save the link inside firebase
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {


                        if (task.isSuccessful()) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final String downloadUrl = String.valueOf(filePath.getDownloadUrl());
                                    Log.i("downloadUrl", downloadUrl);

                                    //Important msukkan url ke dlam profileimage2
                                    String uriurl = uri.toString();
                                    Log.i("uri", uri.toString());

                                    Toast.makeText(Profile.this, "Your Image Successfully Uploaded ", Toast.LENGTH_SHORT).show();

                                    HashMap userMap = new HashMap();
                                    userMap.put("profileimage2", uriurl);
                                    //Pilihan untuk update
                                    //UsersRef.update(userMap).addOnCompleteListener(new OnCompleteListener() {

                                    SettinguserRef.set(userMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Profile.this, "Link update successfully ", Toast.LENGTH_SHORT).show();
                                                Intent selfIntent = new Intent(Profile.this, Profile.class);
                                                startActivity(selfIntent);
                                                Toast.makeText(Profile.this, "Profile Image Store to Firebase Success", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(Profile.this, "update image link error ", Toast.LENGTH_SHORT).show();
                                                String message = task.getException().getMessage();
                                                Toast.makeText(Profile.this, "Error Occured" + message, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            });
                            
                        }

                    }
                });

            } else {
                Toast.makeText(Profile.this, "Error Occured: Image cant be crop, try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_update:
                UpdateProfileInfo();
                break;
            case R.id.profile_image:
                toGallery();
                break;

        }

    }
}
