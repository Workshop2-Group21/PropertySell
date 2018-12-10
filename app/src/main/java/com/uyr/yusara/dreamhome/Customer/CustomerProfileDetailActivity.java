package com.uyr.yusara.dreamhome.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uyr.yusara.dreamhome.Admin.AdminMainMenu;
import com.uyr.yusara.dreamhome.Agent.AgentProfileDetailActivity;
import com.uyr.yusara.dreamhome.Agent.AllAgentListFromProfile;
import com.uyr.yusara.dreamhome.R;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView customer_email;
    private TextView customer_fullname;
    private TextView customer_phone;
    private TextView customer_description;

    // Code gambar
    private CircleImageView ProfileImage;
    final static int gallerypick = 1;
    private StorageReference UserProfileImageRef;

    private DocumentReference ClickPostRef;
    private DocumentReference RoleRef;
    private CollectionReference CountRef;
    private CollectionReference Postsref;
    private FirebaseAuth mAuth;

    private String currentUserid;

    private android.support.v7.widget.Toolbar mToolbar;

    private String PostKey, CURRENT_STATE, agent;

    private Button removecustomer;

    private String AgentName;

    private int countPosts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_detail);

        // Code gambar
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");
        ProfileImage = findViewById(R.id.customerprofileimage);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        PostKey = getIntent().getExtras().get("PostKey").toString();
        ClickPostRef = FirebaseFirestore.getInstance().collection("Users").document(PostKey);
        RoleRef = FirebaseFirestore.getInstance().collection("Users").document(PostKey);
        CountRef = FirebaseFirestore.getInstance().collection("Posts");


        removecustomer = findViewById(R.id.button_delete);

        IntializeFields();

        findViewById(R.id.button_delete).setOnClickListener(this);


        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" Agent Profile ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Toast.makeText(this, "User ID = " + PostKey, Toast.LENGTH_SHORT).show();


        ClickPostRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("profileimage2");
                    String email = documentSnapshot.getString("email");
                    String fullname = documentSnapshot.getString("name");
                    String phone = documentSnapshot.getString("phone");
                    String profiledescription = documentSnapshot.getString("profiledescription");

                    Glide.with(CustomerProfileDetailActivity.this).load(image).into(ProfileImage);
                    customer_email.setText(email);
                    customer_fullname.setText(fullname);
                    customer_phone.setText(phone);
                    customer_description.setText(profiledescription);

                }

            }
        });

    }

    private void IntializeFields()
    {
        customer_email    = findViewById(R.id.customer_email);
        customer_fullname   = findViewById(R.id.customer_fullname);
        customer_phone    = findViewById(R.id.customer_phone);
        customer_description = findViewById(R.id.customer_description);

        CURRENT_STATE = "not_agents";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            //SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure about this?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClickPostRef = FirebaseFirestore.getInstance().collection("Users").document(PostKey);
                        ClickPostRef.delete();

                        Toast.makeText(CustomerProfileDetailActivity.this, "Account has been deactivated", Toast.LENGTH_SHORT).show();

                        finish();
                        Intent deleteaccount = new Intent(CustomerProfileDetailActivity.this, AdminMainMenu.class);
                        startActivity(deleteaccount);
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
        }

    }
}
