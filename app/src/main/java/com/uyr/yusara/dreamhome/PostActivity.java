package com.uyr.yusara.dreamhome;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.Nullable;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton SelectPostImage;
    private Button UpdatePostButton;
    private EditText PostDescription;

    final static int gallerypick = 1;
    private Uri ImageUri;
    private String Description;
    private String downloadUrl,uriurl;


    private StorageReference PostImageRef;

    private FirebaseAuth mAuth;
    private String currentUserid;
    private DocumentReference UsersRef;
    private CollectionReference Postsref;

    private String saveCurrentDate, saveCurrentTime, postRandomName;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        PostImageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);
        Postsref = FirebaseFirestore.getInstance().collection("Posts");

        SelectPostImage = findViewById(R.id.select_post);
        UpdatePostButton = findViewById(R.id.btnupdatepost);
        PostDescription = findViewById(R.id.edit_Post);

        findViewById(R.id.select_post).setOnClickListener(this);
        findViewById(R.id.btnupdatepost).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallerypick && resultCode == RESULT_OK && data!= null)
        {
            //Display user in image button
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);

        }
    }

    public void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallerypick);
    }

    public void UpdateBtnPost()
    {
        Description = PostDescription.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Please select Image ...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        else
            {
                StoringImageToStorage();
            }
    }

    private void StoringImageToStorage()
    {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = PostImageRef.child("Post Images").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {

                if(task.isSuccessful())
                {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            Toast.makeText(PostActivity.this, "Image uploaded success..",Toast.LENGTH_SHORT).show();
                            uriurl = uri.toString();
                            downloadUrl = filePath.getDownloadUrl().toString();
                            SavingPostInformationToDB();
                        }
                    });
                }
                else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(PostActivity.this, "Error occurt" + message,Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }

    private void SavingPostInformationToDB()
    {

        UsersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        //Log.i("LOGGER",document.getString("email"));

                        String name = document.getString("name");
                        String userprofile = document.getString("profileimage2");

                        HashMap postMap = new HashMap();
                        postMap.put("uid", currentUserid);
                        postMap.put("date", saveCurrentDate);
                        postMap.put("time", saveCurrentTime);
                        postMap.put("description", Description);
                        postMap.put("postImage", uriurl);
                        postMap.put("name", name);
                        postMap.put("profileimage2", userprofile);

                        Toast.makeText(PostActivity.this, "Masuk Saving Post info ", Toast.LENGTH_SHORT).show();

                        Postsref.add(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(PostActivity.this, "Post update successfully ", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(PostActivity.this, "Update Post error ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.select_post:
                OpenGallery();
                break;
            case R.id.btnupdatepost:
                UpdateBtnPost();
                break;
        }
    }
}
