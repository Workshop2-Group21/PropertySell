package com.uyr.yusara.dreamhome.Agent;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uyr.yusara.dreamhome.MainActivity;
import com.uyr.yusara.dreamhome.Modal.User;
import com.uyr.yusara.dreamhome.R;

public class AllAgentList extends AppCompatActivity {

    private RecyclerView agentList;

    private DocumentReference UsersRef;
    private CollectionReference Postsref;
    private CollectionReference Agentsref;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_agent_list);

        agentList = findViewById(R.id.recyclerview_allagentlist);
        agentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        agentList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);
        //Postsref = FirebaseFirestore.getInstance().collection("Posts");
        Agentsref = FirebaseFirestore.getInstance().collection("Users");
        //Postsref = FirebaseDatabase.getInstance().getReference().child("Posts");

        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Agent Lists");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        Intent mainIntent = new Intent(AllAgentList.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Query SortPostsInDecendingOrder = Postsref.orderBy("counter");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(Agentsref, User.class)
                .build();

        FirestoreRecyclerAdapter<User, AllAgentList.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<User, AllAgentList.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllAgentList.PostsViewHolder holder, final int position, @NonNull User model) {

                Glide.with(AllAgentList.this).load(model.getProfileimage()).into(holder.productimage2);

                holder.productname.setText(model.getName());
                //Glide.with(AllAgentList.this).load(model.getProfileimage()).into(holder.productimage2);
/*                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());*/

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Untuk dpat id user
                        //String PostKey = getSnapshots().get(position).getUid();

                        // Untuk dpat Id dalam table post
                        String PostKey = getSnapshots().getSnapshot(position).getId();

                        Intent click_post = new Intent(AllAgentList.this, AgentProfileDetailActivity.class);
                        click_post.putExtra("PostKey", PostKey);

                        startActivity(click_post);
                    }
                });
            }

            @NonNull
            @Override
            public AllAgentList.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_agentlist, viewGroup, false);
                AllAgentList.PostsViewHolder viewHolder = new AllAgentList.PostsViewHolder(view);

                return viewHolder;
            }
        };
        agentList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView productname, productprice, productdate;
        ImageView productimage2;

        public PostsViewHolder(View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.agentname);
/*            productprice = itemView.findViewById(R.id.post_product_price);
            productdate = itemView.findViewById(R.id.post_product_date);*/
            productimage2 = itemView.findViewById(R.id.agentimage);
        }
    }
}