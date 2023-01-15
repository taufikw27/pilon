package com.example.pilon;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UbahBahasa extends AppCompatActivity {
    com.google.android.material.radiobutton.MaterialRadioButton rbleft, rbright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_ubah_bahasa);
        rbleft = findViewById( R.id.rbleft);
        rbright = findViewById( R.id.rbright);
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon( R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }

        });
    }


    public void onRadioButtonClicked(View view){
        boolean isSelected = ((com.google.android.material.radiobutton.MaterialRadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.rbleft:
                if(isSelected){
                    rbleft.setTextColor(Color.WHITE);
                    rbright.setTextColor(Color.BLACK);
                }
                break;
            case R.id.rbright:
                if(isSelected){
                    rbright.setTextColor(Color.WHITE);
                    rbleft.setTextColor(Color.BLACK);
                }
                break;
        }

    }
}