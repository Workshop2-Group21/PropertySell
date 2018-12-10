package com.uyr.yusara.dreamhome.Admin;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.uyr.yusara.dreamhome.Agent.ClickPostActivityAgent;
import com.uyr.yusara.dreamhome.ClickPostActivity;
import com.uyr.yusara.dreamhome.FindHouseActivity;
import com.uyr.yusara.dreamhome.Modal.FIndHouseType;
import com.uyr.yusara.dreamhome.Modal.Posts;
import com.uyr.yusara.dreamhome.PostActivity;
import com.uyr.yusara.dreamhome.R;

import java.util.HashMap;

import javax.annotation.Nullable;

public class AllPostPending extends AppCompatActivity {

    private RecyclerView postList;

    private DocumentReference UsersRef;
    private CollectionReference Postsref;
    private CollectionReference Postsref2;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private Toolbar mToolbar;

    private DocumentReference PostsRef2;

    //private Button bungalowbtn,singlestoreybtn;

    private TextView edit_property;

    private ImageButton SearchButton;
    private EditText SearchInputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post_pending);

        postList = findViewById(R.id.recyclerview_allpostpending);
        // postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);
        Postsref = FirebaseFirestore.getInstance().collection("Posts");

        SearchButton = (ImageButton) findViewById(R.id.search_house_button);
        SearchInputText = (EditText) findViewById(R.id.search_box_input);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBoxInput = SearchInputText.getText().toString();
                SearchHouseType(searchBoxInput);
            }
        });
        //Postsref = FirebaseDatabase.getInstance().getReference().child("Posts");

/*        bungalowbtn = (Button) findViewById(R.id.bungalowbtn);
        singlestoreybtn = (Button)findViewById(R.id.singlestoreybtn);*/

        edit_property = findViewById(R.id.edit_property);

        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("House Lists");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Query SortPostsInDecendingOrder1 = Postsref.orderBy("counter",Query.Direction.DESCENDING);

/*        bungalowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pendingapprove();
            }
        });

        singlestoreybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                singlestorey();
            }
        });*/

        edit_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //singlestorey();

                final CharSequence options[] = new CharSequence[]
                        {
                                "Bungalow",
                                "Single storey",
                                "2/1 storey",
                                "Triple storey",
                                "Semi detached",
                                "Others",
                                "Pending List",
                                "Approved List"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(AllPostPending.this);
                builder.setTitle("Select Options");


                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            edit_property.setText(options[0]);
                            bungalow();
                        }
                        if (which == 1) {
                            edit_property.setText(options[1]);
                            singlestorey();
                            //onResume();
                        }
                        if (which == 2) {
                            edit_property.setText(options[2]);
                            twostorey();
                        }
                        if (which == 3) {
                            edit_property.setText(options[3]);
                            triplestorey();
                        }
                        if (which == 4) {
                            edit_property.setText(options[4]);
                            semidetached();
                        }
                        if (which == 5) {
                            edit_property.setText(options[5]);
                            other();
                        }
                        if (which == 6) {
                            edit_property.setText(options[6]);
                            pendingapprove();
                        }
                        if (which == 7) {
                            edit_property.setText(options[7]);
                            approved();
                        }

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //singlestorey();
    }

    private void bungalow() {

        Query SortPostsInDecendingOrder = Postsref.orderBy("propertytype").startAt("Bungalow").endAt("Bungalow" + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortPostsInDecendingOrder, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostPending.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostPending.this).load(model.getPostImage()).into(holder.productimage);

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


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });

                holder.AprovePostButton.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public AllPostPending.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_pending_layout, viewGroup, false);
                AllPostPending.PostsViewHolder viewHolder = new AllPostPending.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();

    }

    private void singlestorey() {

        Query SortPostsInDecendingOrder = Postsref.orderBy("propertytype").startAt("Single storey").endAt("Single storey" + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortPostsInDecendingOrder, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostPending.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostPending.this).load(model.getPostImage()).into(holder.productimage);

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


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });

                holder.AprovePostButton.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public AllPostPending.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_pending_layout, viewGroup, false);
                AllPostPending.PostsViewHolder viewHolder = new AllPostPending.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();

    }

    private void twostorey() {

        Query SortPostsInDecendingOrder = Postsref.orderBy("propertytype").startAt("2/1 storey").endAt("2/1 storey" + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortPostsInDecendingOrder, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostPending.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostPending.this).load(model.getPostImage()).into(holder.productimage);

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


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });

                holder.AprovePostButton.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public AllPostPending.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_pending_layout, viewGroup, false);
                AllPostPending.PostsViewHolder viewHolder = new AllPostPending.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();

    }

    private void triplestorey() {

        Query SortPostsInDecendingOrder = Postsref.orderBy("propertytype").startAt("Triple storey").endAt("Triple storey" + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortPostsInDecendingOrder, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostPending.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostPending.this).load(model.getPostImage()).into(holder.productimage);

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


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });

                holder.AprovePostButton.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public AllPostPending.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_pending_layout, viewGroup, false);
                AllPostPending.PostsViewHolder viewHolder = new AllPostPending.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();

    }

    private void semidetached() {

        Query SortPostsInDecendingOrder = Postsref.orderBy("propertytype").startAt("Semi detached").endAt("Semi detached" + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortPostsInDecendingOrder, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostPending.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostPending.this).load(model.getPostImage()).into(holder.productimage);

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


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });

                holder.AprovePostButton.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public AllPostPending.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_pending_layout, viewGroup, false);
                AllPostPending.PostsViewHolder viewHolder = new AllPostPending.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();

    }

    private void other() {

        Query SortPostsInDecendingOrder = Postsref.orderBy("propertytype").startAt("Others").endAt("Others" + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortPostsInDecendingOrder, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostPending.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostPending.this).load(model.getPostImage()).into(holder.productimage);

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


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });

                holder.AprovePostButton.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public AllPostPending.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_pending_layout, viewGroup, false);
                AllPostPending.PostsViewHolder viewHolder = new AllPostPending.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();

    }

    private void pendingapprove() {
        Query SortPostsInDecendingOrder = Postsref.orderBy("status").startAt("pending").endAt("pending" + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortPostsInDecendingOrder, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostPending.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostPending.this).load(model.getPostImage()).into(holder.productimage);

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


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });

                holder.AprovePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String PostKey = getSnapshots().getSnapshot(position).getId();
                        PostsRef2 = FirebaseFirestore.getInstance().collection("Posts").document(PostKey);

                        HashMap userMap = new HashMap();
                        userMap.put("status", "approve");
                        PostsRef2.update(userMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AllPostPending.this, "Property Approve successfully ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AllPostPending.this, "Property error update ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }

            @NonNull
            @Override
            public AllPostPending.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_pending_layout, viewGroup, false);
                AllPostPending.PostsViewHolder viewHolder = new AllPostPending.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();
    }

    private void approved() {
        Query SortPostsInDecendingOrder = Postsref.orderBy("status").startAt("approve").endAt("approve" + "\uf8ff");

/*        //Untuk display semua post x tersusun
        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(Postsref,Posts.class)
                .build();*/

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(SortPostsInDecendingOrder, Posts.class)
                .build();

        FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder> adapter = new FirestoreRecyclerAdapter<Posts, AllPostPending.PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllPostPending.PostsViewHolder holder, final int position, @NonNull Posts model) {


                holder.productname.setText(model.getDescription());
                holder.productprice.setText("RM " + model.getPrice());
                holder.productdate.setText(model.getDate());
                Glide.with(AllPostPending.this).load(model.getPostImage()).into(holder.productimage);

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


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });

                holder.AprovePostButton.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public AllPostPending.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_posts_pending_layout, viewGroup, false);
                AllPostPending.PostsViewHolder viewHolder = new AllPostPending.PostsViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView productname, productprice, productdate;
        ImageView productimage;
        ImageView AprovePostButton;

        public PostsViewHolder(View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.post_product_name);
            productprice = itemView.findViewById(R.id.post_product_price);
            productdate = itemView.findViewById(R.id.post_product_date);
            productimage = itemView.findViewById(R.id.post_product_image);
            AprovePostButton = itemView.findViewById(R.id.approvebtn);
        }
    }

    private void SearchHouseType(String searchBoxInput) {
        Toast.makeText(this, "Searching ....", Toast.LENGTH_LONG).show();

        String query = searchBoxInput.toLowerCase();

        Query searchHouseTypeQuery = Postsref.orderBy("description").startAt(query).endAt(query + "\uf8ff");

        FirestoreRecyclerOptions<FIndHouseType> options = new FirestoreRecyclerOptions.Builder<FIndHouseType>()
                .setQuery(searchHouseTypeQuery, FIndHouseType.class)
                .build();

        FirestoreRecyclerAdapter<FIndHouseType, FindHouseActivity.FindHouseTypeViewHolder> adapter = new FirestoreRecyclerAdapter<FIndHouseType, FindHouseActivity.FindHouseTypeViewHolder>(options) {

            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull FindHouseActivity.FindHouseTypeViewHolder holder, final int position, @NonNull FIndHouseType model) {
                holder.setDescription(model.getDescription());
                holder.setProductprice(model.getPrice());
                holder.setDate(model.getDate());
                holder.setPostImage(getApplicationContext(), model.getPostImage());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Untuk dpat Id dalam table post
                        String PostKey = getSnapshots().getSnapshot(position).getId();
                        String Decrip = getSnapshots().get(position).getDescription();
                        String PostImg = getSnapshots().get(position).getPostImage();
                        String Price = getSnapshots().get(position).getPrice();
                        String PropertyType = getSnapshots().get(position).getPropertytype();


                        Intent click_post = new Intent(AllPostPending.this, ClickPostActivity.class);
                        click_post.putExtra("PostKey", PostKey);
                        click_post.putExtra("Description", Decrip);
                   /*     click_post.putExtra("Price", Price);
                        click_post.putExtra("PropertyType", PropertyType);*/
                        click_post.putExtra("PostImage", PostImg);

                        startActivity(click_post);
                    }
                });
            }

            @Override
            public FindHouseActivity.FindHouseTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout, parent, false);
                FindHouseActivity.FindHouseTypeViewHolder viewHolder = new FindHouseActivity.FindHouseTypeViewHolder(view);

                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class FindHouseTypeViewHolder extends RecyclerView.ViewHolder {
        TextView productname, productprice, productdate;
        ImageView productimage;

        public FindHouseTypeViewHolder(View itemView) {
            super(itemView);

        }

        public void setPostImage(Context ctx, String profileImage2) {
            ImageView myImage = (ImageView) itemView.findViewById(R.id.post_product_image);
            Glide.with(ctx).load(profileImage2).into(myImage);

            //Glide.with(FindHouseActivity.this).load(ctx).into(myImage);
        }

        public void setDescription(String description) {
            TextView myName = (TextView) itemView.findViewById(R.id.post_product_name);
            myName.setText(description);
        }

        public void setProductprice(String price) {
            TextView myName = (TextView) itemView.findViewById(R.id.post_product_price);
            myName.setText("RM " + price);
        }

        public void setDate(String date) {
            TextView myName = (TextView) itemView.findViewById(R.id.post_product_date);
            myName.setText(date);
        }
    }
}
