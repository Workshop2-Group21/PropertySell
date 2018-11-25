package com.uyr.yusara.dreamhome;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.uyr.yusara.dreamhome.Modal.User;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgentProfileDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView agent_email;
    private TextView agent_fullname;
    private TextView agent_phone;

    // Code gambar
    private CircleImageView ProfileImage;
    final static int gallerypick = 1;
    private StorageReference UserProfileImageRef;

    private DocumentReference ClickPostRef;
    private DocumentReference RoleRef;
    private FirebaseAuth mAuth;

    private String currentUserid;

    private android.support.v7.widget.Toolbar mToolbar;

    private String PostKey, CURRENT_STATE, agent;

    private Button ApproveAgentBtn,RemoveAgentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile_detail);

        // Code gambar
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");
        ProfileImage    = findViewById(R.id.agentprofileimage);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        agent=

        PostKey = getIntent().getExtras().get("PostKey").toString();
        ClickPostRef = FirebaseFirestore.getInstance().collection("Users").document(PostKey);
        RoleRef = FirebaseFirestore.getInstance().collection("Users").document(PostKey);

        IntializeFields();

        findViewById(R.id.button_approve).setOnClickListener(this);
        findViewById(R.id.button_remove).setOnClickListener(this);


        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" Agent Profile ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Toast.makeText(this, "User ID = " + PostKey, Toast.LENGTH_SHORT).show();


        ClickPostRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
            {
                String email = documentSnapshot.getString("email");
                String fullname = documentSnapshot.getString("name");
                String phone = documentSnapshot.getString("phone");

                agent_email.setText(email);
                agent_fullname.setText(fullname);
                agent_phone.setText(phone);
            }
        });

        ClickPostRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("profileimage2");
                    Glide.with(AgentProfileDetailActivity.this).load(image).into(ProfileImage);
                }

            }
        });

        RemoveAgentBtn.setVisibility(View.INVISIBLE);
        RemoveAgentBtn.setEnabled(false);

        RoleRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String role = document.getString("role");

                        Toast.makeText(AgentProfileDetailActivity.this, "Work :D " + role , Toast.LENGTH_SHORT).show();

                        if(!RoleRef.equals(role))
                        {
                            RemoveAgentBtn.setVisibility(View.VISIBLE);
                            RemoveAgentBtn.setEnabled(false);

                        }


                    } else
                        {
                            Toast.makeText(AgentProfileDetailActivity.this, "x work :(" , Toast.LENGTH_SHORT).show();

                    }
                } else {

                }

            }
        });



        if(!currentUserid.equals(RoleRef))
        {


            ApproveAgentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ApproveAgentBtn.setEnabled(false);
                }
            });
        }
        else
            {
                RemoveAgentBtn.setVisibility(View.INVISIBLE);
                ApproveAgentBtn.setVisibility(View.INVISIBLE);
            }

    }

    private void IntializeFields() {

        agent_email    = findViewById(R.id.agent_email);
        agent_fullname   = findViewById(R.id.agent_fullname);
        agent_phone    = findViewById(R.id.agent_phone);

        ApproveAgentBtn = findViewById(R.id.button_approve);
        RemoveAgentBtn = findViewById(R.id.button_remove);

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


    private void RemoveBtnPost()
    {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_remove:
                RemoveBtnPost();
                break;
            case R.id.button_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure about this?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //deleteProduct();
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
