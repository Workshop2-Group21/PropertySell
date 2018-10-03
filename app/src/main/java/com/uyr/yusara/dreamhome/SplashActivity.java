package com.uyr.yusara.dreamhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.uyr.yusara.dreamhome.Modal.Product;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    View view;
    ImageView ccesplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        view= this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.white);

        ccesplash = (ImageView)findViewById(R.id.ccesplash);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        ccesplash.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashActivity.this, Login.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
