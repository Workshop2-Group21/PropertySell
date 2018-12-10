package com.uyr.yusara.dreamhome;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.uyr.yusara.dreamhome.Modal.FIndHouseType;
import com.uyr.yusara.dreamhome.Modal.Posts;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindHouseActivity extends AppCompatActivity {


    private ImageButton SearchButton;
    private EditText SearchInputText;

    private RecyclerView SearchResultList;

    private CollectionReference allUserDatabaseRef;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_house);


    SearchResultList = (RecyclerView) findViewById(R.id.search_result_list);
    SearchResultList.setHasFixedSize(true);
    SearchResultList.setLayoutManager(new LinearLayoutManager(this));

    SearchButton = (ImageButton) findViewById(R.id.search_house_button);
    SearchInputText = (EditText) findViewById(R.id.search_box_input);

    allUserDatabaseRef = FirebaseFirestore.getInstance().collection("Posts");

    SearchButton.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String searchBoxInput = SearchInputText.getText().toString();
            SearchHouseType(searchBoxInput);
        }
    });

    //dpat dri layout activity_find_house toolbar
        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle("Find House");
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
        Intent mainIntent = new Intent(FindHouseActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SearchHouseType(String searchBoxInput)
    {
        Toast.makeText(this, "Searching ....", Toast.LENGTH_LONG).show();

        String query = searchBoxInput.toLowerCase();

        Query searchHouseTypeQuery = allUserDatabaseRef.orderBy("description").startAt(query).endAt(query + "\uf8ff");

        FirestoreRecyclerOptions<FIndHouseType> options = new FirestoreRecyclerOptions.Builder<FIndHouseType>()
                .setQuery(searchHouseTypeQuery,FIndHouseType.class)
                .build();

        FirestoreRecyclerAdapter<FIndHouseType,FindHouseTypeViewHolder> adapter = new FirestoreRecyclerAdapter<FIndHouseType, FindHouseTypeViewHolder>(options)
        {

            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull FindHouseTypeViewHolder holder, final int position, @NonNull FIndHouseType model)
            {
                holder.setDescription(model.getDescription());
                holder.setProductprice(model.getPrice());
                holder.setDate(model.getDate());
                holder.setPostImage(getApplicationContext(), model.getPostImage());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        // Untuk dpat Id dalam table post
                        String PostKey = getSnapshots().getSnapshot(position).getId();
                        String Decrip = getSnapshots().get(position).getDescription();
                        String PostImg = getSnapshots().get(position).getPostImage();
                        String Price = getSnapshots().get(position).getPrice();
                        String PropertyType = getSnapshots().get(position).getPropertytype();


                        Intent click_post = new Intent(FindHouseActivity.this,ClickPostActivity.class);
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
            public FindHouseTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout, parent, false);
                FindHouseTypeViewHolder viewHolder = new FindHouseTypeViewHolder(view);

                return viewHolder;
            }
        };

        SearchResultList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class FindHouseTypeViewHolder extends RecyclerView.ViewHolder
    {
        TextView productname, productprice, productdate;
        ImageView productimage;

        public FindHouseTypeViewHolder(View itemView)
        {
            super(itemView);

        }

        public void setPostImage(Context ctx, String profileImage2)
        {
            ImageView myImage = (ImageView) itemView.findViewById(R.id.post_product_image);
            Glide.with(ctx).load(profileImage2).into(myImage);

            //Glide.with(FindHouseActivity.this).load(ctx).into(myImage);
        }

        public void setDescription(String description)
        {
            TextView myName = (TextView) itemView.findViewById(R.id.post_product_name);
            myName.setText(description);
        }

        public void setProductprice(String price)
        {
            TextView myName = (TextView) itemView.findViewById(R.id.post_product_price);
            myName.setText("RM " + price);
        }

        public void setDate(String date)
        {
            TextView myName = (TextView) itemView.findViewById(R.id.post_product_date);
            myName.setText(date);
        }

    }
}
