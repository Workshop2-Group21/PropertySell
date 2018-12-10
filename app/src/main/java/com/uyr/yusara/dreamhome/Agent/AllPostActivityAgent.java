package com.uyr.yusara.dreamhome.Agent;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.uyr.yusara.dreamhome.MainActivity;
import com.uyr.yusara.dreamhome.Modal.Posts;
import com.uyr.yusara.dreamhome.R;

public class AllPostActivityAgent extends AppCompatActivity {

    private RecyclerView postList;

    private DocumentReference UsersRef;
    private CollectionReference Postsref;
    //private DocumentReference Postsref2;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private Toolbar mToolbar;

    private String PostKey2;
    private DocumentReference PostsRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post_agent);

        postList = findViewById(R.id.recyclerview_allpostagent);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);
        Postsref = FirebaseFirestore.getInstance().collection("Posts");
        //Postsref = FirebaseFirestore.getInstance().collection("Posts").document(PostKey).collection("comment");




        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Sell House List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        Intent mainIntent = new Intent(AllPostActivityAgent.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Query SortPostsInDecendingOrder = Postsref.orderBy("counter");
        Query SortAgentPost = Postsref.orderBy("uid").startAt(currentUserid).endAt(currentUserid + "\uf8ff");

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortAgentPost,Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts,AllPostActivityAgent.PostsViewHolder > adapter = new FirestoreRecyclerAdapter<Posts, AllPostActivityAgent.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostActivityAgent.PostsViewHolder holder, final int position, @NonNull Posts model)
            {



                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostActivityAgent.this).load(model.getPostImage()).into(holder.productimage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        //Untuk dpat id user
                        //String PostKey = getSnapshots().get(position).getUid();

                        // Untuk dpat Id dalam table post
                        String PostKey = getSnapshots().getSnapshot(position).getId();
                        String Decrip = getSnapshots().get(position).getDescription();
                        String PostImg = getSnapshots().get(position).getPostImage();
                        String Price = getSnapshots().get(position).getPrice();
                        String PropertyType = getSnapshots().get(position).getPropertytype();


                        Intent click_post = new Intent(AllPostActivityAgent.this,ClickPostActivityAgent.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);

                    }
                });

                holder.layout_action1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AllPostActivityAgent.this);
                        builder.setTitle("Are you sure about this?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String PostKey = getSnapshots().getSnapshot(position).getId();

                                PostsRef2 = FirebaseFirestore.getInstance().collection("Posts").document(PostKey);

                                PostsRef2.delete();
                                Toast.makeText(AllPostActivityAgent.this, "Property delete successfully ", Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog ad = builder.create();
                        ad.show();
                    }
                });

                holder.layout_action2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        //Untuk dpat id user
                        //String PostKey = getSnapshots().get(position).getUid();

                        // Untuk dpat Id dalam table post
                        String PostKey = getSnapshots().getSnapshot(position).getId();
                        String Decrip = getSnapshots().get(position).getDescription();
                        String PostImg = getSnapshots().get(position).getPostImage();
                        String Price = getSnapshots().get(position).getPrice();
                        String PropertyType = getSnapshots().get(position).getPropertytype();


                        Intent click_post = new Intent(AllPostActivityAgent.this,ClickPostActivityAgent.class);
                        click_post.putExtra("PostKey", PostKey);
                        //    click_post.putExtra("Description", Decrip);
              /*          click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);

                    }
                });
            }

            @NonNull
            @Override
            public AllPostActivityAgent.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_post_layout_agent, viewGroup, false);
                AllPostActivityAgent.PostsViewHolder viewHolder = new AllPostActivityAgent.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        TextView productname, productprice, productdate;
        ImageView productimage;
        LinearLayout layout_action1,layout_action2;

        public PostsViewHolder(View itemView)
        {
            super(itemView);

            productname = itemView.findViewById(R.id.post_product_name);
            productprice = itemView.findViewById(R.id.post_product_price);
            productdate = itemView.findViewById(R.id.post_product_date);
            productimage = itemView.findViewById(R.id.post_product_image);
            layout_action1 = itemView.findViewById(R.id.layout_action1);
            layout_action2 = itemView.findViewById(R.id.layout_action2);
        }
    }
}
