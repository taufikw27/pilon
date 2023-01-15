package com.example.pilon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    Animation Atop,Abottom;
    ImageView IVimage,IVjudul;
    TextView  TVslogan;
    ConstraintLayout constraintLayout;
    SharedPreferences shp;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login);

        //koneksi ke animation
        Atop = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Abottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //koneksi ke layout
        IVimage = findViewById( R.id.idlogo);
        IVjudul = findViewById( R.id.idjudul);
        TVslogan = findViewById( R.id.idslogan);
        constraintLayout = findViewById( R.id.splash);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        IVimage.setAnimation(Atop);
        IVjudul.setAnimation(Abottom);
        TVslogan.setAnimation(Abottom);

//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(getApplicationContext(),login.class));
//            }
//        }, 5000L); //5000L = 5detik



    }
    @Override
    protected void onResume() {
        super.onResume();

        animationDrawable.start();

        checkAnimationStatus(50, animationDrawable);
    }

    private void checkAnimationStatus(final int time, final AnimationDrawable animationDrawable) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animationDrawable.getCurrent() != animationDrawable.getFrame(animationDrawable.getNumberOfFrames() - 1))
                    checkAnimationStatus(time, animationDrawable);
                else  startActivity(new Intent (getApplicationContext(),onboarding.class));
            }
        }, time);
    }

}