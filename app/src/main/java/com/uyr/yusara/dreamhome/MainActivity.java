package com.uyr.yusara.dreamhome;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.uyr.yusara.dreamhome.Agent.AllAgentList;
import com.uyr.yusara.dreamhome.Agent.AllPostActivityAgent;
import com.uyr.yusara.dreamhome.Menu.Login;
import com.uyr.yusara.dreamhome.Modal.Wishlist;
import com.uyr.yusara.dreamhome.News.NewsMainActivity;
import com.uyr.yusara.dreamhome.WishList.WishlistActivity;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView userEmail;
    FirebaseUser firebaseUser;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private DocumentReference UsersRef;

    private CircleImageView NavProfileImage;

    ViewFlipper flipper2;

    private ImageButton img1, img2, img3, img4, img5, img6;
    private TextView Bungalow, Single, Triple, halfstorey, semi,others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //View navView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View navView = navigationView.getHeaderView(0);
        NavProfileImage = (CircleImageView)navView.findViewById(R.id.nav_image);

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
        img3 = (ImageButton) findViewById(R.id.img3);
        img4 = (ImageButton) findViewById(R.id.img4);
        img5 = (ImageButton) findViewById(R.id.img5);
        img6 = (ImageButton) findViewById(R.id.img6);

        Bungalow = (TextView) findViewById(R.id.bungtext);
        Single = (TextView) findViewById(R.id.singletext);
        Triple = (TextView) findViewById(R.id.tripletext);
        halfstorey = (TextView) findViewById(R.id.halfstoreytext);
        semi = (TextView)findViewById(R.id.semitexr);
        others = (TextView)findViewById(R.id.otherstext);

        navigationView.setNavigationItemSelectedListener(this);

        UsersRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
            {

                if(documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("profileimage2");

                    //Picasso.get().load(image).placeholder(R.drawable.cc).into(NavProfileImage);
                    Glide.with(MainActivity.this).load(image).into(NavProfileImage);
                }

            }
        });


        int images[] = {R.drawable.promo1,R.drawable.promo2,R.drawable.promo3};

        flipper2 = findViewById(R.id.flipper2);

        for (int i = 0; i < images.length; i++)
        {
            flipperImages(images[i]);
        }

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);

    }

    public void flipperImages(int image)
    {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        flipper2.addView(imageView);
        flipper2.setFlipInterval(4000);
        flipper2.setAutoStart(true);

        flipper2.setInAnimation(this, android.R.anim.slide_in_left);
        flipper2.setOutAnimation(this, android.R.anim.slide_in_left);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        userEmail = findViewById(R.id.txtemail);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail.setText(firebaseUser.getEmail());

/*        UsersRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("profileimage2");

                    //Picasso.get().load(image).placeholder(R.drawable.cc).into(NavProfileImage);
                    //Glide.with(MainActivity.this).load(image).into(NavProfileImage);
                }

            }
        });*/



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();

        } else if (id == R.id.nav_profile) {

            Intent profile = new Intent(MainActivity.this, Profile.class);
            startActivity(profile);

        } else if (id == R.id.nav_sell) {

            Intent sell = new Intent(MainActivity.this, PostActivity.class);
            startActivity(sell);

        } else if (id == R.id.nav_onsell) {

            Intent onsell = new Intent(MainActivity.this, AllPostActivity.class);
            startActivity(onsell);

        }else if (id == R.id.nav_find) {

            Intent allpost = new Intent(MainActivity.this, FindHouseActivity .class);
            startActivity(allpost);

        } else if (id == R.id.nav_mysellitem) {

            Intent find = new Intent(MainActivity.this,  AllPostActivityAgent.class);
            startActivity(find);

        }else if (id == R.id.nav_fav) {

            Intent wish = new Intent(MainActivity.this, WishlistActivity.class);
            startActivity(wish);

        } else if (id == R.id.nav_news) {

            Intent news = new Intent(MainActivity.this, NewsMainActivity.class);
            startActivity(news);

        } else if (id == R.id.nav_share) {
            Intent post = new Intent(MainActivity.this, AllAgentList.class);
            startActivity(post);


        } else if (id == R.id.nav_about) {

            MyCustomAlertDialog();

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, Login.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void MyCustomAlertDialog(){
        final Dialog MyDialog = new Dialog(MainActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        MyDialog.setContentView(R.layout.about_dialog);
        MyDialog.setTitle("My Custom Dialog");

        /*hello = (Button)MyDialog.findViewById(R.id.hello);*/
        Button close = (Button)MyDialog.findViewById(R.id.closebtn);

        close.setEnabled(true);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.img1:
                Intent allpost2 = new Intent(MainActivity.this, AllPostActivity2.class);
                allpost2.putExtra("PostBungalow", Bungalow.getText().toString());
                startActivity(allpost2);
                break;
            case R.id.img2:
                allpost2 = new Intent(MainActivity.this, AllPostActivity2.class);
                allpost2.putExtra("PostBungalow", Single.getText().toString());
                startActivity(allpost2);
                break;
            case R.id.img3:
                allpost2 = new Intent(MainActivity.this, AllPostActivity2.class);
                allpost2.putExtra("PostBungalow", Triple.getText().toString());
                startActivity(allpost2);
                break;
            case R.id.img4:
                allpost2 = new Intent(MainActivity.this, AllPostActivity2.class);
                allpost2.putExtra("PostBungalow", halfstorey.getText().toString());
                startActivity(allpost2);
                break;
            case R.id.img5:
                allpost2 = new Intent(MainActivity.this, AllPostActivity2.class);
                allpost2.putExtra("PostBungalow", semi.getText().toString());
                startActivity(allpost2);
                break;
            case R.id.img6:
                allpost2 = new Intent(MainActivity.this, AllPostActivity2.class);
                allpost2.putExtra("PostBungalow", others.getText().toString());
                startActivity(allpost2);
                break;
        }
    }
}
