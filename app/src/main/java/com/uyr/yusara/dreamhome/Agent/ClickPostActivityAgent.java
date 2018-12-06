package com.uyr.yusara.dreamhome.Agent;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import com.uyr.yusara.dreamhome.ClickPostActivity;
import com.uyr.yusara.dreamhome.MainActivity;
import com.uyr.yusara.dreamhome.Profile;
import com.uyr.yusara.dreamhome.R;

import java.util.HashMap;

import javax.annotation.Nullable;

public class ClickPostActivityAgent extends AppCompatActivity implements View.OnClickListener {

    private EditText PostDescription,PostSize,PostPrice,PostDescription2;
    private TextView Postproperty,Postbedroom, Postbathroom,Posttitletype,Postotherinfo;

    private ImageView editimage;
    final static int gallerypick = 1;

    private String Description,propertystring,bedroomstring,bathroomstring,titletypestring,otherinfostring,sizestring,pricestring,description2string;

    private StorageReference PostProfileImageRef;

    private DocumentReference PostsRef;
    private FirebaseAuth mAuth;
    private String currentUserid;

    private Button UpdatePostButton,DeletePostButton;

    private String saveCurrentDate, saveCurrentTime, postRandomName;
    private String PostKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post_agent);

        PostProfileImageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        PostKey = getIntent().getExtras().get("PostKey").toString();
        PostsRef = FirebaseFirestore.getInstance().collection("Posts").document(PostKey);


        /*SelectPostImage = findViewById(R.id.select_post);*/
        UpdatePostButton = findViewById(R.id.button_update);
        DeletePostButton = findViewById(R.id.button_delete);

        PostDescription = findViewById(R.id.edittext_title);
        Postproperty = findViewById(R.id.edittext_propertytype);
        Postbedroom = findViewById(R.id.edittext_bedrooms);
        Postbathroom = findViewById(R.id.edittext_bathroom);
        Posttitletype = findViewById(R.id.edittext_titletype);
        PostSize = findViewById(R.id.edittext_size);
        Postotherinfo = findViewById(R.id.edittext_otherinfo);
        PostPrice = findViewById(R.id.edittext_price);
        PostDescription2 = findViewById(R.id.edittext_decription2);
        editimage = (ImageView) findViewById(R.id.editimage);

        findViewById(R.id.button_update).setOnClickListener(this);
        findViewById(R.id.button_delete).setOnClickListener(this);
        findViewById(R.id.edittext_title).setOnClickListener(this);
        findViewById(R.id.edittext_propertytype).setOnClickListener(this);
        findViewById(R.id.edittext_bedrooms).setOnClickListener(this);
        findViewById(R.id.edittext_bathroom).setOnClickListener(this);
        findViewById(R.id.edittext_titletype).setOnClickListener(this);
        findViewById(R.id.edittext_otherinfo).setOnClickListener(this);
        findViewById(R.id.edittext_size).setOnClickListener(this);
        findViewById(R.id.edittext_price).setOnClickListener(this);
        findViewById(R.id.edittext_decription2).setOnClickListener(this);
        findViewById(R.id.editimage).setOnClickListener(this);

        PostsRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
            {
                String description = documentSnapshot.getString("description");
                String price = documentSnapshot.getString("price");
                String propertytype = documentSnapshot.getString("propertytype");
                String bathroom = documentSnapshot.getString("bathroom");
                String bedrooms = documentSnapshot.getString("bedrooms");
                String otherinfo = documentSnapshot.getString("otherinfo");
                String size = documentSnapshot.getString("size");
                String firmtype = documentSnapshot.getString("titletype");
                String description2 = documentSnapshot.getString("description2");

                PostDescription.setText(description);
                PostPrice.setText(price);
                Postproperty.setText(propertytype);
                Postbathroom.setText(bathroom);
                Postbedroom.setText(bedrooms);
                Postotherinfo.setText(otherinfo);
                PostSize.setText(size);
                Posttitletype.setText(firmtype);
                PostDescription2.setText(description2);
            }
        });

        PostsRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("postImage");

                    //Picasso.get().load(image).placeholder(R.drawable.cc).into(ProfileImage);
                    Glide.with(ClickPostActivityAgent.this).load(image).into(editimage);
                }

            }
        });
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
            UpdatePost(Description,propertystring,bedroomstring,bathroomstring,titletypestring,otherinfostring,sizestring,pricestring,description2string);
        }
    }

    private void UpdatePost(String description, String propertystring, String bedroomstring, String bathroomstring, String titletypestring, String otherinfostring, String sizestring, String pricestring, String description2string)
    {
        HashMap userMap = new HashMap();
        userMap.put("description", description);
        userMap.put("propertytype", propertystring);
        userMap.put("bedrooms", bedroomstring);
        userMap.put("bathroom", bathroomstring);
        userMap.put("titletype", titletypestring);
        userMap.put("otherinfo", otherinfostring);
        userMap.put("size", sizestring);
        userMap.put("price", pricestring);
        userMap.put("description2", description2string);
        PostsRef.update(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    SendUserToMainActivity();
                    Toast.makeText(ClickPostActivityAgent.this, "Property update successfully ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ClickPostActivityAgent.this, "Property error update ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(ClickPostActivityAgent.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void deleteProduct()
    {
        PostsRef.delete();
        SendUserToMainActivity();
        Toast.makeText(ClickPostActivityAgent.this, "Property delete successfully ", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivityAgent.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivityAgent.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivityAgent.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivityAgent.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivityAgent.this);
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
            Toast.makeText(ClickPostActivityAgent.this, "msuk requestcode", Toast.LENGTH_LONG).show();

            if (resultCode == RESULT_OK) {
                Toast.makeText(ClickPostActivityAgent.this, "result masuk", Toast.LENGTH_LONG).show();
                Uri resultUri = result.getUri();

                final StorageReference filePath = PostProfileImageRef.child(currentUserid + ".jpg");

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

                                    Toast.makeText(ClickPostActivityAgent.this, "Your Image Successfully Uploaded ", Toast.LENGTH_SHORT).show();

                                    HashMap userMap = new HashMap();
                                    userMap.put("postImage", uriurl);
                                    //Pilihan untuk update
                                    //UsersRef.update(userMap).addOnCompleteListener(new OnCompleteListener() {

                                    PostsRef.set(userMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ClickPostActivityAgent.this, "Link update successfully ", Toast.LENGTH_SHORT).show();
                                                Intent selfIntent = new Intent(ClickPostActivityAgent.this, ClickPostActivityAgent.class);
                                                startActivity(selfIntent);
                                                Toast.makeText(ClickPostActivityAgent.this, "Profile Image Store to Firebase Success", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(ClickPostActivityAgent.this, "update image link error ", Toast.LENGTH_SHORT).show();
                                                String message = task.getException().getMessage();
                                                Toast.makeText(ClickPostActivityAgent.this, "Error Occured" + message, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }

                    }
                });

            } else {
                Toast.makeText(ClickPostActivityAgent.this, "Error Occured: Image cant be crop, try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_update:
                UpdateBtnPost();
                break;
            case R.id.button_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure about this?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
                break;
            case R.id.edittext_propertytype:
                propertytypeItem1();
                break;
            case R.id.edittext_bedrooms:
                bedroomItem();
                break;
            case R.id.edittext_bathroom:
                bathroomItem();
                break;
            case R.id.edittext_titletype:
                titletypeItem();
                break;
            case R.id.edittext_otherinfo:
                otherinfoItem();
                break;
            case R.id.editimage:
                toGallery();
                break;
        }

    }


}
