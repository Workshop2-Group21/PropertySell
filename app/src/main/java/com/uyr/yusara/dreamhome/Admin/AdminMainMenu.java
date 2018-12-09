package com.uyr.yusara.dreamhome.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.uyr.yusara.dreamhome.Agent.AllAgentList;
import com.uyr.yusara.dreamhome.ClickPostActivity;
import com.uyr.yusara.dreamhome.CommentActivity;
import com.uyr.yusara.dreamhome.Customer.AllCustomerList;
import com.uyr.yusara.dreamhome.Customer.MainActivityCustomer;
import com.uyr.yusara.dreamhome.MainActivity;
import com.uyr.yusara.dreamhome.Menu.Login;
import com.uyr.yusara.dreamhome.News.NewsMainActivity;
import com.uyr.yusara.dreamhome.R;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminMainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView userEmail;
    FirebaseUser firebaseUser;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private DocumentReference UsersRef;

    private CircleImageView NavProfileImage;

    private CardView bankcardId,bankcardId2, bankcardId1, bankcardId3,bankcardId5;
    private LinearLayout L1;
    private Animation left, right;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View navView = navigationView.getHeaderView(0);
        NavProfileImage = (CircleImageView)navView.findViewById(R.id.nav_image);

        navigationView.setNavigationItemSelectedListener(this);

        UsersRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
            {

                if(documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("profileimage2");

                    //Picasso.get().load(image).placeholder(R.drawable.cc).into(NavProfileImage);
                    Glide.with(AdminMainMenu.this).load(image).into(NavProfileImage);
                }

            }
        });

        bankcardId = findViewById(R.id.bankcardId);
        bankcardId1 = findViewById(R.id.bankcardId1);
        bankcardId2 = findViewById(R.id.bankcardId2);
        bankcardId3 = findViewById(R.id.bankcardId3);
        bankcardId5 = findViewById(R.id.bankcardId5);


        findViewById(R.id.bankcardId).setOnClickListener(this);
        findViewById(R.id.bankcardId1).setOnClickListener(this);
        findViewById(R.id.bankcardId2).setOnClickListener(this);
        findViewById(R.id.bankcardId3).setOnClickListener(this);
        findViewById(R.id.bankcardId5).setOnClickListener(this);


        L1 = findViewById(R.id.L1);
        left = AnimationUtils.loadAnimation(this, R.anim.lefttoright);
        L1.setAnimation(left);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        userEmail = findViewById(R.id.txtemail);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail.setText(firebaseUser.getEmail());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action

            Intent homeIntent = new Intent(AdminMainMenu.this, AdminMainMenu.class);
            startActivity(homeIntent);
            finish();

        } else if (id == R.id.nav_find) {



        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, Login.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bankcardId:
                Intent post = new Intent(AdminMainMenu.this, AllAgentList.class);
                startActivity(post);
                break;
            case R.id.bankcardId2:
                Intent news = new Intent(AdminMainMenu.this, NewsMainActivity.class);
                startActivity(news);
                break;
            case R.id.bankcardId1:
                Intent customer = new Intent(AdminMainMenu.this, AllCustomerList.class);
                startActivity(customer);
                break;
            case R.id.bankcardId3:
                Intent houses = new Intent(AdminMainMenu.this, AllPostPending.class);
                startActivity(houses);
                break;
            case R.id.bankcardId5:
                Intent report = new Intent(AdminMainMenu.this, AdminGraph.class);
                startActivity(report);
        }
    }
}
