package com.example.pilon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class notifikasi_view extends AppCompatActivity
{
    TextView TV1, TV2;
    String nama, des;
    ImageButton ib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.detail_pesan);
        TV1 = findViewById( R.id.idTV1);
        TV2 = findViewById( R.id.idTV2);
        ib = findViewById ( R.id.ib );

        nama =getIntent ().getStringExtra ( "nama" );
        des =getIntent ().getStringExtra ( "pesan" );
        TV1.setText(nama);
        TV2.setText(des);

        ib.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                onBackPressed ();
            }
        } );
    }
}
