package com.example.travelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroductoryActivity extends AppCompatActivity {
    ImageView splash;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        splash=findViewById(R.id.img);
        lottieAnimationView=findViewById(R.id.lottie);

        splash.animate().translationY(-3900).setDuration(2500).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1400).setDuration(10000).setStartDelay(4000);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(IntroductoryActivity.this,GirisActivity.class));
                finish();
            }
        }, secondsDelayed * 6000);
    }
}