package com.uyr.yusara.dreamhome.RecyclerViewTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.uyr.yusara.dreamhome.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewTest extends AppCompatActivity {

    private RecyclerView recycleView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_test);

        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        for(int i = 0; i<=10; i++)
        {
            ListItem listItem = new ListItem(
                    "heading" + i+1,
                    "Lorem ipsum dummy text"
            );

            listItems.add(listItem);
        }

        adapter = new MyAdapter(listItems, this);

        recycleView.setAdapter(adapter);

    }
}
