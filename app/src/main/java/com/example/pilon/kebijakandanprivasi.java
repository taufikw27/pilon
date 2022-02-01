package com.example.pilon;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class kebijakandanprivasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_kebijakandanprivasi);
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
}