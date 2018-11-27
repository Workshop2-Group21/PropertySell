package com.uyr.yusara.dreamhome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.uyr.yusara.dreamhome.Modal.Posts;
import com.uyr.yusara.dreamhome.Modal.Product;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import static java.lang.Boolean.getBoolean;

public class ClickPostActivity extends AppCompatActivity implements View.OnClickListener {
    private String PostKey,Decrip, Price, PropertyType, BathRoom, Bedrooms, PostImage, uid;
    private ImageView postimages;
    private TextView Postdescription;
    private Button btnComment;
    //private CollectionReference ClickPostRef;
    private DocumentReference ClickPostRef; //,BookmarkPostRef;
    private DocumentReference ClickAgentRef;
    private CollectionReference BookmarkPostRef;
    //private DatabaseReference BookmarkPostRef;
    private TextView Postpricetxt, Postpropertytypetxt, Postbathroomtxt, Postbedroomstxt, Postsizetxt, Postfirmtypetxt, Postfirmnumbertxt, PostDescription2txt,Postaddresstxt,Posttitletypetxt;
    private TextView Postuser_id;
    private TextView username;
    //private DatabaseReference ClickPostRef2;


    private ImageButton BookmarkPostButton;
    private Boolean BookmarkCheckers;
    int countBookmark;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private Toolbar mToolbar;

    private String PostKeyUid;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId()) {
                case R.id.navigation_phone:
                    //mTextMessage.setText(R.string.title_home);

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+Postsizetxt.getText()));
                    startActivity(intent);
                    return true;
                case R.id.navigation_sms:
                    //mTextMessage.setText(R.string.title_dashboard);
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "0194510153", null));
                    intent1.putExtra("sms_body", "Hello Dreamhome");
                    return true;
                case R.id.navigation_comment:
                    //mTextMessage.setText(R.string.title_notifications);
                    Intent commentsIntent = new Intent(ClickPostActivity.this, CommentActivity.class);
                    commentsIntent.putExtra("PostKey", PostKey);
                    startActivity(commentsIntent);
                    return true;
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
        Postsizetxt = findViewById(R.id.size);
        Postfirmtypetxt = findViewById(R.id.firm);
        Posttitletypetxt = findViewById(R.id.otherinfo);
        PostDescription2txt = (TextView) findViewById(R.id.description_id);

        Postuser_id = (TextView)findViewById(R.id.user_id);
        username = (TextView)findViewById(R.id.username);

        btnComment = findViewById(R.id.btnSubmitComment);

        PostKey = getIntent().getExtras().get("PostKey").toString();
        Decrip = getIntent().getExtras().get("Description").toString();


      /*  Price = getIntent().getExtras().get("Price").toString();
        PropertyType = getIntent().getExtras().get("PropertyType").toString();*/
        PostImage = getIntent().getExtras().get("PostImage").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();


        PostImage = String.valueOf(Glide.with(ClickPostActivity.this).load(PostImage).into(postimages));

        //Price = "7Hwf3lr98zHfZvcQuNlU";
        ClickPostRef = FirebaseFirestore.getInstance().collection("Posts").document(PostKey);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

/*        ClickPostRef = FirebaseFirestore.getInstance().collection("Posts");
//        ClickPostRef2 = FirebaseDatabase.getInstance().getReference().child("Posts");

        ClickPostRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

            }
        });*/

        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(Decrip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        //Postdescription.setText("Title : " + Decrip);
        Toast.makeText(this, "User ID = " + PostKey, Toast.LENGTH_SHORT).show();


        ClickPostRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
            {
                String description = documentSnapshot.getString("description");
                String price = documentSnapshot.getString("price");
                String address = documentSnapshot.getString("address");
                String propertytype = documentSnapshot.getString("propertytype");
                String bathroom = documentSnapshot.getString("bathroom");
                String bedrooms = documentSnapshot.getString("bedrooms");
                String size = documentSnapshot.getString("size");
                String titletype = documentSnapshot.getString("titletype");
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

                //Untuk tarik userID dri textfield
                PostKeyUid = Postuser_id.getText().toString();
                Postuser_id.setText(PostKeyUid);
                ClickAgentRef = FirebaseFirestore.getInstance().collection("Users").document(PostKeyUid);
                Toast.makeText(ClickPostActivity.this, "User ID = " + PostKeyUid, Toast.LENGTH_SHORT).show();

                ClickAgentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
                    {
                        String name = documentSnapshot.getString("name");

                        username.setText(name);
                    }
                });
            }
        });


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
        Intent mainIntent = new Intent(ClickPostActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
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
        }
    }

}
