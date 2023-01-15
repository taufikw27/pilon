package com.example.pilon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class onboarding extends AppCompatActivity {
    public static ViewPager viewPager;
    SliderViewPagerAdapter sliderViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_onboarding );

        viewPager = findViewById ( R.id.idVP );
        sliderViewPagerAdapter = new SliderViewPagerAdapter ( this );
        viewPager.setAdapter ( sliderViewPagerAdapter );
        if(isOpenAlready()){
            Intent intent = new Intent (onboarding.this, Login.class );
            intent.setFlags (intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity ( intent );
        }
        else {
            SharedPreferences.Editor editor=getSharedPreferences ( "slide", MODE_PRIVATE).edit ();
            editor.putBoolean ( "slide",true );
            editor.commit ();
        }
        viewPager.setTranslationX ( 800 );
        viewPager.setAlpha (0);
        viewPager.animate ().translationX (0).alpha (1).setDuration (800).setStartDelay (300).start ();
    }
    private boolean isOpenAlready() {
        SharedPreferences sharedPreferences=getSharedPreferences ( "slide",MODE_PRIVATE );
        boolean result = sharedPreferences.getBoolean ( "slide",false );
        return result;

    }

}
