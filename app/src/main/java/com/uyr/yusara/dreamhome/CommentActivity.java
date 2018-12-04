package com.uyr.yusara.dreamhome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.uyr.yusara.dreamhome.Modal.Comments;
import com.uyr.yusara.dreamhome.Modal.Posts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.Nullable;

public class CommentActivity extends AppCompatActivity {

    private ImageButton postCommentbtn;
    private EditText commentInputText;
    private RecyclerView CommentsList;

    private DocumentReference UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserid;

    private CollectionReference Postsref;

    private String PostKey;

    private int countPosts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        PostKey = getIntent().getExtras().get("PostKey").toString();

        CommentsList = (RecyclerView) findViewById(R.id.recycleView_comment);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);

        //Untuk sessions
        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);
        Postsref = FirebaseFirestore.getInstance().collection("Posts").document(PostKey).collection("comment");

        commentInputText = (EditText)findViewById(R.id.comment_input);
        postCommentbtn = (ImageButton) findViewById(R.id.btnPostComment);

        postCommentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                UsersRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
                    {
                        if(documentSnapshot.exists())
                        {
                            String userName = documentSnapshot.getString("name");
                            
                            validateComment(userName);

                            commentInputText.setText("");

                        }
                    }
                });

                //Untuk susun comment
                Postsref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                        {
                            QuerySnapshot document = task.getResult();
                            if (document != null)
                            {
                                countPosts = document.size();

                            }
                            else
                            {
                                countPosts = 0;
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query SortPostsInDecendingOrder = Postsref.orderBy("counter");

        FirestoreRecyclerOptions<Comments> options = new FirestoreRecyclerOptions.Builder<Comments>()
                .setQuery(SortPostsInDecendingOrder,Comments.class)
                .build();

/*        FirestoreRecyclerOptions<Comments> options = new FirestoreRecyclerOptions.Builder<Comments>()
                .setQuery(Postsref,Comments.class)
                .build();*/

        FirestoreRecyclerAdapter<Comments, CommentActivity.CommentsViewHolder> adapter = new FirestoreRecyclerAdapter<Comments, CommentActivity.CommentsViewHolder>(options)
        {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comments model)
            {

                holder.setUsername(model.getUsername());
                holder.setComment(model.getComment());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());

            }

            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comments_layout, parent, false);
                CommentActivity.CommentsViewHolder viewHolder = new CommentActivity.CommentsViewHolder(view);

                return viewHolder;
            }

        };

        CommentsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder
    {

        public CommentsViewHolder(View itemView)
        {
            super(itemView);
        }

        public void setUsername(String username)
        {
            TextView myName = (TextView) itemView.findViewById(R.id.comment_username);
            myName.setText("@" + username +"  ") ;

        }

        public void setComment(String comment)
        {

            TextView myComment = (TextView) itemView.findViewById(R.id.comment_text);
            myComment.setText(comment);

        }

        public void setDate(String date)
        {
            TextView mydate = (TextView) itemView.findViewById(R.id.comment_date);
            mydate.setText(date);
        }

        public void setTime(String time)
        {
            TextView mytime = (TextView) itemView.findViewById(R.id.comment_time);
            mytime.setText("  Time: "+time);
        }
    }

    private void validateComment(String userName)
    {
        String commentText = commentInputText.getText().toString();

        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(this, "Please right text to comment ...", Toast.LENGTH_LONG).show();
        }
        else
            {
                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String saveCurrentDate = currentDate.format(calFordDate.getTime());

                Calendar calFordTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:");
                final String saveCurrentTime = currentTime.format(calFordTime.getTime());

                final String RandomKey = currentUserid + saveCurrentDate + saveCurrentTime;

                HashMap commentsMap = new HashMap();
                commentsMap.put("uid", currentUserid);
                commentsMap.put("comment", commentText);
                commentsMap.put("date", saveCurrentDate);
                commentsMap.put("time", saveCurrentTime);
                commentsMap.put("username", userName);
                commentsMap.put("counter",countPosts);

                Postsref.add(commentsMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(CommentActivity.this, "Comment update successfully ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(CommentActivity.this, "Update Comment error ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

    }
}
