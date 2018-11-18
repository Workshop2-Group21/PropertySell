package com.uyr.yusara.dreamhome;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ClickPostActivity extends AppCompatActivity
{
    private String PostKey,Decrip,PostImage;
    private ImageView postimages;
    private TextView Postdescription;
    private CollectionReference ClickPostRef;
    //private DatabaseReference ClickPostRef2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        Postdescription = findViewById(R.id.title_id);
        postimages = findViewById(R.id.imageView4);

        PostKey = getIntent().getExtras().get("PostKey").toString();
        Decrip = getIntent().getExtras().get("Description").toString();
        PostImage = getIntent().getExtras().get("PostImage").toString();
        PostImage = String.valueOf(Glide.with(ClickPostActivity.this).load(PostImage).into(postimages));

/*        ClickPostRef = FirebaseFirestore.getInstance().collection("Posts");
//        ClickPostRef2 = FirebaseDatabase.getInstance().getReference().child("Posts");

        ClickPostRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



            }
        });*/

        Postdescription.setText("Title : " + Decrip);
        Toast.makeText(this, "User ID = " + PostKey, Toast.LENGTH_SHORT).show();
    }
}
