package com.uyr.yusara.dreamhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private Button UpdatePostButton,propertytypeButton;
    private EditText PostDescription,PostSize,PostPrice,PostDescription2;
    private TextView Postproperty,Postbedroom, Postbathroom,Posttitletype,Postotherinfo;

    final static int gallerypick = 1;
    private Uri ImageUri;
    private String Description,propertystring,bedroomstring,bathroomstring,titletypestring,otherinfostring,sizestring,pricestring,description2string;
    private String downloadUrl,uriurl;


    private StorageReference PostImageRef;

    private FirebaseAuth mAuth;
    private String currentUserid;
    private DocumentReference UsersRef;
    private CollectionReference Postsref;

    private int countPosts = 0;

    private String saveCurrentDate, saveCurrentTime, postRandomName;

    private Toolbar mToolbar;


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
        PostDescription = findViewById(R.id.edit_desc);
        Postproperty = findViewById(R.id.edit_property);
        Postbedroom = findViewById(R.id.edit_bedrooms);
        Postbathroom = findViewById(R.id.edit_bathrooms);
        Posttitletype = findViewById(R.id.edit_titletype);
        Postotherinfo = findViewById(R.id.edit_otherinfo);
        PostSize = findViewById(R.id.edit_size);
        PostPrice = findViewById(R.id.edit_price);
        PostDescription2 = findViewById(R.id.edit_decription2);

        findViewById(R.id.select_post).setOnClickListener(this);
        findViewById(R.id.btnupdatepost).setOnClickListener(this);
        findViewById(R.id.edit_property).setOnClickListener(this);
        findViewById(R.id.edit_bedrooms).setOnClickListener(this);
        findViewById(R.id.edit_bathrooms).setOnClickListener(this);
        findViewById(R.id.edit_titletype).setOnClickListener(this);
        findViewById(R.id.edit_otherinfo).setOnClickListener(this);
        findViewById(R.id.edit_size).setOnClickListener(this);
        findViewById(R.id.edit_decription2).setOnClickListener(this);


        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" Sell House");
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

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
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
        propertystring = Postproperty.getText().toString();
        bedroomstring = Postbedroom.getText().toString();
        bathroomstring = Postbathroom.getText().toString();
        titletypestring = Posttitletype.getText().toString();
        otherinfostring = Postotherinfo.getText().toString();
        sizestring = PostSize.getText().toString();
        pricestring = PostPrice.getText().toString();

        description2string = PostDescription2.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Please select Image ...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(propertystring))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(bedroomstring))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(bathroomstring))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(titletypestring))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(otherinfostring))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(sizestring))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(pricestring))
        {
            Toast.makeText(this, "Please enter something...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(description2string))
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

    private void propertytypeItem1()
    {
        final CharSequence options[] = new CharSequence[]
                {
                    "Bungalow",
                    "Single storey",
                    "Two and a half storey",
                    "Triple storey",
                    "Semi detached",
                    "Others"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Select Options");


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0)
                {
                    Postproperty.setText(options[0]);
                }
                if (which == 1)
                {
                    Postproperty.setText(options[1]);
                }
                if (which == 2)
                {
                    Postproperty.setText(options[2]);
                }
                if (which == 3)
                {
                    Postproperty.setText(options[3]);
                }
                if (which == 4)
                {
                    Postproperty.setText(options[4]);
                }
                if (which == 5)
                {
                    Postproperty.setText(options[5]);
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void bedroomItem()
    {
        final CharSequence options[] = new CharSequence[]
                {
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "More than 5"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Select Options");


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0)
                {
                    Postbedroom.setText(options[0]);
                }
                if (which == 1)
                {
                    Postbedroom.setText(options[1]);
                }
                if (which == 2)
                {
                    Postbedroom.setText(options[2]);
                }
                if (which == 3)
                {
                    Postbedroom.setText(options[3]);
                }
                if (which == 4)
                {
                    Postbedroom.setText(options[4]);
                }
                if (which == 5)
                {
                    Postbedroom.setText(options[5]);
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void bathroomItem()
    {
        final CharSequence options[] = new CharSequence[]
                {
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "More than 5"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Select Options");


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0)
                {
                    Postbathroom.setText(options[0]);
                }
                if (which == 1)
                {
                    Postbathroom.setText(options[1]);
                }
                if (which == 2)
                {
                    Postbathroom.setText(options[2]);
                }
                if (which == 3)
                {
                    Postbathroom.setText(options[3]);
                }
                if (which == 4)
                {
                    Postbathroom.setText(options[4]);
                }
                if (which == 5)
                {
                    Postbathroom.setText(options[5]);
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void titletypeItem()
    {
        final CharSequence options[] = new CharSequence[]
                {
                        "Freehold",
                        "Leasehold"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Select Options");


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0)
                {
                    Posttitletype.setText(options[0]);
                }
                if (which == 1)
                {
                    Posttitletype.setText(options[1]);
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void otherinfoItem()
    {
        final CharSequence options[] = new CharSequence[]
                {
                        "Bumi Lot",
                        "Non Bumi Lot",
                        "Malay Reserved"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Other Info");


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0)
                {
                    Postotherinfo.setText(options[0]);
                }
                if (which == 1)
                {
                    Postotherinfo.setText(options[1]);
                }
                if (which == 2)
                {
                    Postotherinfo.setText(options[2]);
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void SavingPostInformationToDB()
    {
        //Untuk Susun
        Postsref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {
                    QuerySnapshot document = task.getResult();
                    if (document != null)
                    {
                        countPosts = document.size();

                    }
                    else
                        {
                            countPosts = 0;
                        }
                }
            }
        });


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
                        postMap.put("propertytype", propertystring);
                        postMap.put("bedrooms", bedroomstring);
                        postMap.put("bathroom", bathroomstring);
                        postMap.put("titletype", titletypestring);
                        postMap.put("otherinfo", otherinfostring);
                        postMap.put("size", sizestring);
                        postMap.put("price", pricestring);
                        postMap.put("description2", description2string);
                        postMap.put("postImage", uriurl);
                        postMap.put("name", name);
                        postMap.put("counter", countPosts);
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
            case R.id.edit_property:
                propertytypeItem1();
                break;
            case R.id.edit_bedrooms:
                bedroomItem();
                break;
            case R.id.edit_bathrooms:
                bathroomItem();
                break;
            case R.id.edit_titletype:
                titletypeItem();
                break;
            case R.id.edit_otherinfo:
                otherinfoItem();
                break;
        }
    }
}
