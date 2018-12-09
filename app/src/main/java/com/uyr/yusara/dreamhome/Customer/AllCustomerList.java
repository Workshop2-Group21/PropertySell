package com.uyr.yusara.dreamhome.Customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.google.firebase.firestore.Query;
import com.uyr.yusara.dreamhome.AllPostActivity;
import com.uyr.yusara.dreamhome.ClickPostActivity;
import com.uyr.yusara.dreamhome.Modal.Posts;
import com.uyr.yusara.dreamhome.Modal.User;
import com.uyr.yusara.dreamhome.R;

public class AllCustomerList extends AppCompatActivity {

    private RecyclerView customerList;

    private DocumentReference UsersRef;
    private CollectionReference UsersRef2;
    private CollectionReference Postsref2;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customer_list);

        customerList = findViewById(R.id.recyclerview_allcustomerlist);
        customerList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        customerList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);
        UsersRef2 = FirebaseFirestore.getInstance().collection("Users");

        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Customer Lists");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query customerlist = UsersRef2.orderBy("role").startAt("Customer").endAt("Customer" + "\uf8ff");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(customerlist,User.class)
                .build();

        FirestoreRecyclerAdapter<User,AllCustomerList.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<User, AllCustomerList.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder holder, final int position, @NonNull User model) {



                holder.customername.setText(model.getName());
                holder.customerphone.setText(model.getPhone());

                Glide.with(AllCustomerList.this).load(model.getProfileimage2()).into(holder.customerimage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        //Untuk dpat id user
                        //String PostKey = getSnapshots().get(position).getUid();

                        // Untuk dpat Id dalam table post
                        String PostKey = getSnapshots().getSnapshot(position).getId();


                        Intent click_post = new Intent(AllCustomerList.this,CustomerProfileDetailActivity.class);
                        click_post.putExtra("PostKey", PostKey);

                        startActivity(click_post);
                    }
                });
            }

            @NonNull
            @Override
            public AllCustomerList.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_customerlist, viewGroup, false);
                AllCustomerList.PostsViewHolder viewHolder = new AllCustomerList.PostsViewHolder(view);

                return viewHolder;
            }
        };

        customerList.setAdapter(adapter);
        adapter.startListening();
    }

    private class PostsViewHolder extends RecyclerView.ViewHolder {

        TextView customername, customerphone;
        ImageView customerimage;

        public PostsViewHolder(View itemView) {
            super(itemView);

            customername = itemView.findViewById(R.id.customername);
            customerphone = itemView.findViewById(R.id.customernumber);

            customerimage = itemView.findViewById(R.id.customerimage);

        }
    }
}


