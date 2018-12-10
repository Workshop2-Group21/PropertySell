package com.uyr.yusara.dreamhome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.uyr.yusara.dreamhome.Admin.AdminMainMenu;
import com.uyr.yusara.dreamhome.Customer.MainActivityCustomer;
import com.uyr.yusara.dreamhome.Modal.Posts;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllPostActivity2 extends AppCompatActivity {

    private RecyclerView postList;

    private DocumentReference UsersRef;
    private CollectionReference Postsref,allUserDatabaseRef;
    private CollectionReference Postsref2;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private Toolbar mToolbar;
    private ImageButton AddNewPostButton;

    private String HouseType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post2);

        postList = findViewById(R.id.recyclerview_allpost);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);
        Postsref = FirebaseFirestore.getInstance().collection("Posts");
        allUserDatabaseRef = FirebaseFirestore.getInstance().collection("Posts");
        //Postsref = FirebaseDatabase.getInstance().getReference().child("Posts");

        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("House Lists");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AddNewPostButton = (ImageButton) findViewById(R.id.btnPost);

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

                        AddNewPostButton.setVisibility(View.GONE);

                    }
                    else if (role.equals("Customer")) {

                        AddNewPostButton.setVisibility(View.GONE);

                    }
                    else if (role.equals("Admin")) {

                        AddNewPostButton.setVisibility(View.GONE);


                    } else {
                        Toast.makeText(AllPostActivity2.this, "Unable to find roles", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        AddNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(AllPostActivity2.this, PostActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        HouseType = (String) extras.get("PostBungalow");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMainActivity() {
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
                        Intent intent = new Intent(AllPostActivity2.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (role.equals("Customer")) {
                        Intent intent = new Intent(AllPostActivity2.this, MainActivityCustomer.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (role.equals("Admin")) {
                        Intent intent = new Intent(AllPostActivity2.this, AdminMainMenu.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(AllPostActivity2.this, "Unable to find roles", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Query SortPostsInDecendingOrder = Postsref.orderBy("counter");



        Query searchHouseTypeQuery = allUserDatabaseRef.orderBy("propertytype").startAt(HouseType).endAt(HouseType + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(searchHouseTypeQuery, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostActivity.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostActivity.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostActivity.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostActivity2.this).load(model.getPostImage()).into(holder.productimage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Untuk dpat id user
                        //String PostKey = getSnapshots().get(position).getUid();

                        // Untuk dpat Id dalam table post
                        String PostKey = getSnapshots().getSnapshot(position).getId();
                        String Decrip = getSnapshots().get(position).getDescription();
                        String PostImg = getSnapshots().get(position).getPostImage();
                        String Price = getSnapshots().get(position).getPrice();
                        String PropertyType = getSnapshots().get(position).getPropertytype();


                        Intent click_post = new Intent(AllPostActivity2.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });
            }

            @NonNull
            @Override
            public AllPostActivity.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_layout, viewGroup, false);
                AllPostActivity.PostsViewHolder viewHolder = new AllPostActivity.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView productname, productprice, productdate;
        CircleImageView productimage;

        public PostsViewHolder(View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.post_product_name);
            productprice = itemView.findViewById(R.id.post_product_price);
            productdate = itemView.findViewById(R.id.post_product_date);
            productimage = itemView.findViewById(R.id.post_product_image);
        }
    }
}
