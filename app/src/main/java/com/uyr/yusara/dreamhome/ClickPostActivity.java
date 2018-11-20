package com.uyr.yusara.dreamhome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.uyr.yusara.dreamhome.Modal.Posts;
import com.uyr.yusara.dreamhome.Modal.Product;

import java.util.List;

import javax.annotation.Nullable;

public class ClickPostActivity extends AppCompatActivity implements View.OnClickListener {
    private String PostKey,Decrip, Price, PropertyType, BathRoom, Bedrooms, PostImage;
    private ImageView postimages;
    private TextView Postdescription;
    private Button btnComment;
    //private CollectionReference ClickPostRef;
    private DocumentReference ClickPostRef;
    private TextView Postpricetxt, Postpropertytypetxt, Postbathroomtxt, Postbedroomstxt, Postsizetxt, Postfirmtypetxt, Postfirmnumbertxt, PostDescription2txt;
    //private DatabaseReference ClickPostRef2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        Postdescription = findViewById(R.id.title_id);
        Postpricetxt = (TextView) findViewById(R.id.price_id);
        Postpropertytypetxt = (TextView) findViewById(R.id.propertytype);
        Postbathroomtxt = (TextView) findViewById(R.id.bathroom);
        Postbedroomstxt = (TextView) findViewById(R.id.bedroom);
        postimages = findViewById(R.id.imageView4);
        Postsizetxt = findViewById(R.id.size);
        Postfirmtypetxt = findViewById(R.id.firm);
        PostDescription2txt = (TextView) findViewById(R.id.description_id);

        btnComment = findViewById(R.id.btnSubmitComment);

        PostKey = getIntent().getExtras().get("PostKey").toString();
/*        Decrip = getIntent().getExtras().get("Description").toString();
        Price = getIntent().getExtras().get("Price").toString();
        PropertyType = getIntent().getExtras().get("PropertyType").toString();*/
        PostImage = getIntent().getExtras().get("PostImage").toString();


        PostImage = String.valueOf(Glide.with(ClickPostActivity.this).load(PostImage).into(postimages));

        //Price = "7Hwf3lr98zHfZvcQuNlU";
        ClickPostRef = FirebaseFirestore.getInstance().collection("Posts").document(PostKey);

/*        ClickPostRef = FirebaseFirestore.getInstance().collection("Posts");
//        ClickPostRef2 = FirebaseDatabase.getInstance().getReference().child("Posts");

        ClickPostRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



            }
        });*/

        //Postdescription.setText("Title : " + Decrip);
        Toast.makeText(this, "User ID = " + PostKey, Toast.LENGTH_SHORT).show();

        ClickPostRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
            {
                String description = documentSnapshot.getString("description");
                String price = documentSnapshot.getString("price");
                String propertytype = documentSnapshot.getString("propertytype");
                String bathroom = documentSnapshot.getString("bathroom");
                String bedrooms = documentSnapshot.getString("bedrooms");
                String size = documentSnapshot.getString("size");
                String firmtype = documentSnapshot.getString("titletype");
                String description2 = documentSnapshot.getString("description2");

                Postdescription.setText("Title : " + description);
                Postpricetxt.setText("RM : " + price);
                Postpropertytypetxt.setText("Property : " + propertytype);
                Postbathroomtxt.setText("Bathroom : " + bathroom);
                Postbedroomstxt.setText("Bedrooms : " + bedrooms);
                Postsizetxt.setText("Size : " + size + "ft");
                Postfirmtypetxt.setText("Firm Type : " + firmtype);
                PostDescription2txt.setText(description2);
            }
        });

        findViewById(R.id.btnSubmitComment).setOnClickListener(this);

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
