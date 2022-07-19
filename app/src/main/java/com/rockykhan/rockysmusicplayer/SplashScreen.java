package com.rockykhan.rockysmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    ImageView icon;
    Animation scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        icon = findViewById(R.id.icon);

        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        icon.setAnimation(scale);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(intent);

                finish();

                overridePendingTransition(0, 0);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            }
        }, 1000);

    }
}