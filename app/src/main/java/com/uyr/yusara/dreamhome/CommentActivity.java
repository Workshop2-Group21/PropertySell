package com.uyr.yusara.dreamhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CommentActivity extends AppCompatActivity {

    private ImageButton postCommentbtn;
    private EditText commentInputText;
    private RecyclerView CommentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        CommentsList = (RecyclerView) findViewById(R.id.recycleView_comment);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);

        commentInputText = (EditText)findViewById(R.id.comment_input);
        postCommentbtn = (ImageButton) findViewById(R.id.btnPostComment);

    }
}
