package com.uyr.yusara.dreamhome.Agent;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uyr.yusara.dreamhome.Admin.AdminMainMenu;
import com.uyr.yusara.dreamhome.AllPostActivity2;
import com.uyr.yusara.dreamhome.R;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgentProfileDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView agent_email;
    private TextView agent_fullname;
    private TextView agent_phone;
    private TextView agent_description;

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

    private Button unitsale,removeagent;

    private String AgentName;

    private int countPosts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile_detail);

        // Code gambar
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");
        ProfileImage = findViewById(R.id.agentprofileimage);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        PostKey = getIntent().getExtras().get("PostKey").toString();
        ClickPostRef = FirebaseFirestore.getInstance().collection("Users").document(PostKey);
        RoleRef = FirebaseFirestore.getInstance().collection("Users").document(PostKey);
        CountRef = FirebaseFirestore.getInstance().collection("Posts");


        unitsale = findViewById(R.id.button_sales);
        removeagent = findViewById(R.id.button_delete);

        IntializeFields();

        findViewById(R.id.button_sales).setOnClickListener(this);
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
                String email = documentSnapshot.getString("email");
                String fullname = documentSnapshot.getString("name");
                String phone = documentSnapshot.getString("phone");
                String profiledescription = documentSnapshot.getString("profiledescription");

                agent_email.setText(email);
                agent_fullname.setText(fullname);
                agent_phone.setText(phone);
                agent_description.setText(profiledescription);

/*                Postsref = FirebaseFirestore.getInstance().collection("Posts").document().collection(PostKey);

                Postsref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                        {
                            QuerySnapshot document = task.getResult();
                            if (document != null)
                            {
                                countPosts = document.size();
                                unitsale.setText(Integer.toString(countPosts));
                                Toast.makeText(AgentProfileDetailActivity.this, "Post = " + countPosts, Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                countPosts = 0;
                            }
                        }
                    }
                });*/

            }
        });

        ClickPostRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("profileimage2");
                    Glide.with(AgentProfileDetailActivity.this).load(image).into(ProfileImage);

                }

            }
        });


        RoleRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String role = document.getString("role");

                        Toast.makeText(AgentProfileDetailActivity.this, "Work :D " + role, Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(AgentProfileDetailActivity.this, "x work :(", Toast.LENGTH_SHORT).show();

                    }
                } else {

                }
            }
        });

        CountAgent();
    }

    private void CountAgent()
    {
        CountRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {
                    QuerySnapshot document = task.getResult();
                    if (document != null)
                    {
                        countPosts = document.size();
                        //unitsale.setText(" Unit On Sales: " + Integer.toString(countPosts));
                        Toast.makeText(AgentProfileDetailActivity.this, "Size is " + countPosts, Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        countPosts = 0;
                    }
                }
            }
        });

/*        CountRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                for(QuerySnapshot ds: queryDocumentSnapshots.getDocuments().size())

            }
        });*/
    }


    private void IntializeFields() {

        agent_email    = findViewById(R.id.agent_email);
        agent_fullname   = findViewById(R.id.agent_fullname);
        agent_phone    = findViewById(R.id.agent_phone);
        agent_description = findViewById(R.id.agent_description);

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
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_sales:
                Intent onsell = new Intent(AgentProfileDetailActivity.this, AllAgentListFromProfile.class);
                onsell.putExtra("PassAgentName", agent_fullname.getText().toString());
                startActivity(onsell);
                break;
            case R.id.button_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure about this?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClickPostRef = FirebaseFirestore.getInstance().collection("Users").document(PostKey);
                        ClickPostRef.delete();

                        Toast.makeText(AgentProfileDetailActivity.this, "Account has been deactivated", Toast.LENGTH_SHORT).show();

                        finish();
                        Intent deleteaccount = new Intent(AgentProfileDetailActivity.this, AdminMainMenu.class);
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
