package com.uyr.yusara.dreamhome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.service.chooser.ChooserTarget;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.Util;
import com.uyr.yusara.dreamhome.Admin.AdminMainMenu;
import com.uyr.yusara.dreamhome.Admin.AllPostPending;
import com.uyr.yusara.dreamhome.Customer.MainActivityCustomer;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClickPostActivity extends AppCompatActivity implements View.OnClickListener {
    private String PostKey,Decrip, Price, PropertyType, BathRoom, Bedrooms, PostImage, uid;
    private ImageView postimages;
    private TextView Postdescription;
    private Button btnComment;
    private TextView btnComment1;
    //private CollectionReference ClickPostRef;
    private DocumentReference ClickPostRef;//,BookmarkPostRef;
    private DocumentReference ClickAgentRef;

    private CollectionReference wishlistRef;
    private DocumentReference wishlistRefdb;

    //private DatabaseReference BookmarkPostRef;
    private TextView Postpricetxt, Postpropertytypetxt, Postbathroomtxt, Postbedroomstxt, Postsizetxt, Postfirmtypetxt, Postfirmnumbertxt, PostDescription2txt,Postaddresstxt,Posttitletypetxt;
    private TextView Postuser_id;
    private TextView username,userphone,useremail;
    private CircleImageView profile_image;
    private TextView locationaddress,imageurl;


    //private DatabaseReference ClickPostRef2;

    private ImageView wishlistBtn;
    private ImageButton BookmarkPostButton;
    private Boolean BookmarkCheckers;
    int countBookmark;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private Toolbar mToolbar;

    private String PostKeyUid;
    private String address;
    private String price;
    private String propertytype;
    private String titletype;
    private String image;
    private String strPostKey;
    private Intent chooser = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId()) {
                case R.id.navigation_phone:
                    //mTextMessage.setText(R.string.title_home);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+userphone.getText()));
                    startActivity(intent);
                    return true;
                case R.id.navigation_sms:
                    //mTextMessage.setText(R.string.title_dashboard);
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", userphone.getText().toString(), null));
                    intent1.putExtra("sms_body", "Hi Im interested in " + Postdescription.getText().toString());
                    startActivity(intent1);
                    return true;
                case R.id.navigation_comment:
                    //mTextMessage.setText(R.string.title_notifications);
                    Intent intent2 = new Intent(Intent.ACTION_SEND);
                    intent2.setData(Uri.parse("mailto:"));
                    String[] to = {useremail.getText().toString()};
                    intent2.putExtra(Intent.EXTRA_EMAIL, to);
                    intent2.putExtra(Intent.EXTRA_SUBJECT, "Hi Im interested in ...");
                    intent2.putExtra(Intent.EXTRA_TEXT,"Details about " + Postdescription.getText().toString());
                    intent2.setType("message/rfc822");
                    chooser=Intent.createChooser(intent2, "Send Email");
                    startActivity(chooser);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        Postdescription = findViewById(R.id.title_id);
        Postpricetxt = (TextView) findViewById(R.id.rm_id);
        Postaddresstxt = (TextView)findViewById(R.id.address);
        Postpropertytypetxt = (TextView) findViewById(R.id.propertytype);
        Postbathroomtxt = (TextView) findViewById(R.id.bathroom);
        Postbedroomstxt = (TextView) findViewById(R.id.bedroom);
        postimages = findViewById(R.id.imageView4);
        profile_image = findViewById(R.id.profile_image);
        Postsizetxt = findViewById(R.id.size);
        Postfirmtypetxt = findViewById(R.id.firm);
        Posttitletypetxt = findViewById(R.id.otherinfo);
        PostDescription2txt = (TextView) findViewById(R.id.description_id);

        locationaddress = (TextView)findViewById(R.id.openmaps);
        wishlistBtn = (ImageView)findViewById(R.id.wishlistbtn);
        imageurl = (TextView)findViewById(R.id.imageurl);

        Postuser_id = (TextView)findViewById(R.id.user_id);
        username = (TextView)findViewById(R.id.username);
        userphone = (TextView)findViewById(R.id.phone);
        useremail = (TextView)findViewById(R.id.email);

        btnComment1 = findViewById(R.id.btnSubmitComment);


        PostKey = getIntent().getExtras().get("PostKey").toString();
        Decrip = getIntent().getExtras().get("Description").toString();

        Intent to_maps = new Intent(ClickPostActivity.this,MapsActivity.class);
        to_maps.putExtra("PostKey", PostKey);

      /*  Price = getIntent().getExtras().get("Price").toString();
        PropertyType = getIntent().getExtras().get("PropertyType").toString();*/
        PostImage = getIntent().getExtras().get("PostImage").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();


        PostImage = String.valueOf(Glide.with(ClickPostActivity.this).load(PostImage).into(postimages));

        //Price = "7Hwf3lr98zHfZvcQuNlU";
        ClickPostRef = FirebaseFirestore.getInstance().collection("Posts").document(PostKey);
        wishlistRefdb = FirebaseFirestore.getInstance().collection("Wishlist").document(PostKey);
        wishlistRef = FirebaseFirestore.getInstance().collection("Wishlist");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(Decrip);
/*        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/



        //Postdescription.setText("Title : " + Decrip);
        //Toast.makeText(this, "User ID = " + PostKey, Toast.LENGTH_SHORT).show();


        ClickPostRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
            {
                String description = documentSnapshot.getString("description");
                price = documentSnapshot.getString("price");
                address = documentSnapshot.getString("address");
                propertytype = documentSnapshot.getString("propertytype");
                String bathroom = documentSnapshot.getString("bathroom");
                String bedrooms = documentSnapshot.getString("bedrooms");
                String size = documentSnapshot.getString("size");
                titletype = documentSnapshot.getString("titletype");
                String otherinfo = documentSnapshot.getString("otherinfo");
                String description2 = documentSnapshot.getString("description2");
                uid = documentSnapshot.getString("uid");

                Postdescription.setText(description);
                Postpricetxt.setText("RM : " + price);
                Postaddresstxt.setText(address);
                Postpropertytypetxt.setText("\u2022 Property : " + propertytype);
                Postbathroomtxt.setText("\u2022 Bathroom : " + bathroom);
                Postbedroomstxt.setText("\u2022 Bedrooms : " + bedrooms);
                Postsizetxt.setText("\u2022 Size : " + size + "ft");
                Posttitletypetxt.setText("\u2022 Title Type : " + titletype);
                Postfirmtypetxt.setText("\u2022 Other Info : " + otherinfo);
                Postuser_id.setText(uid);
                PostDescription2txt.setText(description2);

                /*Intent to_maps = new Intent(ClickPostActivity.this,MapsActivity.class);
                to_maps.putExtra("PostAddress", address);*/

                //Untuk tarik userID dri textfield
                PostKeyUid = Postuser_id.getText().toString();
                Postuser_id.setText(PostKeyUid);
                ClickAgentRef = FirebaseFirestore.getInstance().collection("Users").document(PostKeyUid);
                //Toast.makeText(ClickPostActivity.this, "User ID = " + PostKeyUid, Toast.LENGTH_SHORT).show();

                ClickAgentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
                    {
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");
                        String email = documentSnapshot.getString("email");
                        String image = documentSnapshot.getString("profileimage2");

                        username.setText(name);
                        userphone.setText(phone);
                        useremail.setText(email);
                        imageurl.setText(image);
                        Glide.with(ClickPostActivity.this).load(image).into(profile_image);

                    }
                });

                wishlistRefdb.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
                    {
                        String uid = documentSnapshot.getString("uid");

                        if(currentUserid == uid )
                        {
                            wishlistBtn.setImageDrawable(getDrawable(R.drawable.wishlist2));
                        }


                    }
                });

            }
        });


        findViewById(R.id.btnSubmitComment).setOnClickListener(this);
        findViewById(R.id.openmaps).setOnClickListener(this);
        findViewById(R.id.wishlistbtn).setOnClickListener(this);

    }

    private void savetoWishlist()
    {
        ClickPostRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())

                {


                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null){
                        HashMap postMap = new HashMap();
                        postMap.put("uid",currentUserid);
                        postMap.put("itemId",strPostKey);
                        postMap.put("address",address);
                        postMap.put("price",price);
                        postMap.put("propertytype",propertytype);
                        postMap.put("titletype",titletype);
                        postMap.put("postImage",imageurl.getText().toString());


                        wishlistBtn.setImageDrawable(getDrawable(R.drawable.wishlist2));

                        wishlistRef.add(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){


                                    Toast.makeText(ClickPostActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ClickPostActivity.this, "Added to error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{

                    }
                }else {

                }
            }
        });
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }*/

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
                        Intent intent = new Intent(ClickPostActivity.this, AllPostActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (role.equals("Customer")) {
                        Intent intent = new Intent(ClickPostActivity.this, AllPostActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (role.equals("Admin")) {
                        Intent intent = new Intent(ClickPostActivity.this, AllPostPending.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(ClickPostActivity.this, "Unable to find roles", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnSubmitComment:
                Intent commentsIntent = new Intent(ClickPostActivity.this, CommentActivity.class);
                commentsIntent.putExtra("PostKey", PostKey);
                startActivity(commentsIntent);
                break;
            case R.id.openmaps:
                Intent to_maps = new Intent(ClickPostActivity.this,MapsActivity.class);
                to_maps.putExtra("PostAddress", address);
                startActivity(to_maps);
                break;
            case R.id.wishlistbtn:


                savetoWishlist();

                break;

        }
    }

}
