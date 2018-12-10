package com.uyr.yusara.dreamhome.Admin;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uyr.yusara.dreamhome.Agent.AgentProfileDetailActivity;
import com.uyr.yusara.dreamhome.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdminGraph extends AppCompatActivity {

    private BarChart mChart;

    private CollectionReference CountRef,CountRef2,CountRef3,CountRef4;
    private int countPosts = 0;
    TextView user,post,wishlist;
    ArrayList<String> xAxis;
    ArrayList<IBarDataSet> dataSets;
    ArrayList<BarEntry> valueSet;
    BarDataSet barDataSet;
    YAxis yAxisRight;
    BarData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_graph);

        CountRef = FirebaseFirestore.getInstance().collection("Posts");
        CountRef2 = FirebaseFirestore.getInstance().collection("Users");
        CountRef3 = FirebaseFirestore.getInstance().collection("Wishlist");
        CountRef4 = FirebaseFirestore.getInstance().collection("Notification");

/*        user = (TextView) findViewById(R.id.user);
        post = (TextView) findViewById(R.id.post);
        wishlist = (TextView) findViewById(R.id.wishlist);*/


        mChart = (BarChart) findViewById(R.id.chart1);
        xAxis = new ArrayList<>();
        dataSets = null;
        valueSet = new ArrayList<>();

        CountRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {
                    QuerySnapshot document = task.getResult();
                    if (document != null)
                    {
                        countPosts = document.size();

                        xAxis.add("Post");
                        xAxis.add("User");
                        xAxis.add("Wishlist");
                        xAxis.add("Notifi");

                        valueSet.add(new BarEntry(countPosts, 0));
                        //valueSet.add(new BarEntry(20, 1));
                        //valueSet.add(new BarEntry(40, 2));
                        //valueSet.add(new BarEntry(30, 3));

                        CountRef2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot document = task.getResult();
                                    countPosts = document.size();

                                    valueSet.add(new BarEntry(countPosts, 1));

/*                                    barDataSet = new BarDataSet(valueSet, "Represent ");
                                    barDataSet.setColors(new int[]{Color.RED, Color.GREEN, Color.MAGENTA, Color.BLUE});

                                    dataSets = new ArrayList<>();
                                    dataSets.add(barDataSet);

                                    yAxisRight = mChart.getAxisRight();
                                    yAxisRight.setEnabled(false);*/

                                    data = new BarData(xAxis, dataSets);
                                    mChart.setExtraOffsets(0, 0, 0, 0);
                                    mChart.setData(data);
                                    mChart.animateXY(2000, 2000);
                                    mChart.invalidate();
                                }

                            }
                        });

                        CountRef3.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot document = task.getResult();
                                    countPosts = document.size();

                                    valueSet.add(new BarEntry(countPosts, 2));

/*                                    barDataSet = new BarDataSet(valueSet, "Represent ");
                                    barDataSet.setColors(new int[]{Color.RED, Color.GREEN, Color.MAGENTA, Color.BLUE});

                                    dataSets = new ArrayList<>();
                                    dataSets.add(barDataSet);

                                    yAxisRight = mChart.getAxisRight();
                                    yAxisRight.setEnabled(false);*/

                                    data = new BarData(xAxis, dataSets);
                                    mChart.setExtraOffsets(0, 0, 0, 0);
                                    mChart.setData(data);
                                    mChart.animateXY(2000, 2000);
                                    mChart.invalidate();
                                }
                            }
                        });

                        CountRef4.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot document = task.getResult();
                                    countPosts = document.size();

                                    valueSet.add(new BarEntry(countPosts, 3));

/*                                    barDataSet = new BarDataSet(valueSet, "Represent ");
                                    barDataSet.setColors(new int[]{Color.RED, Color.GREEN, Color.MAGENTA, Color.BLUE});

                                    dataSets = new ArrayList<>();
                                    dataSets.add(barDataSet);

                                    yAxisRight = mChart.getAxisRight();
                                    yAxisRight.setEnabled(false);*/

                                    data = new BarData(xAxis, dataSets);
                                    mChart.setExtraOffsets(0, 0, 0, 0);
                                    mChart.setData(data);
                                    mChart.animateXY(2000, 2000);
                                    mChart.invalidate();
                                }
                            }
                        });

                    }
                    else
                    {
                        countPosts = 0;
                    }

                    barDataSet = new BarDataSet(valueSet, "Represent ");
                    barDataSet.setColors(new int[] {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE});

                    dataSets = new ArrayList<>();
                    dataSets.add(barDataSet);

                    yAxisRight = mChart.getAxisRight();
                    yAxisRight.setEnabled(false);

                    data = new BarData(xAxis,dataSets);
                    mChart.setExtraOffsets(0,0,0,0);
                    mChart.setData(data);
                    mChart.animateXY(2000,2000);
                    mChart.invalidate();
                }
            }
        });






/*        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);

        setData(10);
        mChart.setFitBars(true);*/

    }

/*    private void setData(int count)
    {
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for(int i = 0; i < count; i++)
        {
            float value = (float) (Math.random()*100);
            yVals.add(new BarEntry(i, (int) value));

        }

        BarDataSet set = new BarDataSet(yVals, "Data Sets");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);

        mChart.setData(data);
        mChart.invalidate();
        mChart.animateY(400);
    }*/
}
