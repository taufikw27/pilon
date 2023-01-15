package com.example.pilon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class UbahPassword extends AppCompatActivity {
    EditText ETpass, ETpass1;
    Button btnsave;
    TextView tverror, tverror1;
    ProgressDialog progressDialog;
    String pass, pass1, nik;
    SharedPreferences shp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_ubah_password );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar);
        ETpass = findViewById ( R.id.iduppas );
        ETpass1 = findViewById ( R.id.iduppas1 );
        btnsave = findViewById ( R.id.idupsave );
        tverror = findViewById ( R.id.idtverror );
        tverror1 = findViewById ( R.id.idtverror1 );
        shp1 = getSharedPreferences ( "mydata",MODE_PRIVATE );
        nik = shp1.getString ( "nik",null );
        progressDialog = new ProgressDialog ( this );
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon( R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        btnsave.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder ( UbahPassword.this )
                        .setMessage ( "Apakah anda yakin mengubah password anda?" )
                        .setCancelable ( false )
                        .setPositiveButton ( "Ya", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pass = ETpass.getText ().toString ();
                                pass1 = ETpass1.getText ().toString ();
                                if (pass1.equals (pass)&& pass.length ()>=8){
                                    progressDialog.setMessage ( "Mengupdate Password" );
                                    progressDialog.setCancelable ( false );
                                    progressDialog.show ();
                                    updatePassword ();
                                }else if(pass.length ()<8 && pass.length ()>1){
                                    tverror.setText ( "password harus lebih dari 8 karakter" );
                                }
                                else if(pass.length ()==0){
                                    tverror.setText ( "Masukkan Password" );
                                }
                                else {
                                    tverror1.setText ( "password tidak sama, Harap periksa kembali password anda" );
                                }


                            }
                        } )
                        .setNegativeButton ( "No", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel ();
                            }
                        } ).show ();
            }
        } );
    }
    void updatePassword(){
            AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/updatePassword.php")
                    .addBodyParameter("nik",""+nik)
                    .addBodyParameter("password",""+pass)
                    .setPriority( Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener () {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Log.d("responEdit",""+response);
                            try{
                                Boolean status = response.getBoolean("status");
                                String pesan = response.getString("result");
                                Toast.makeText(UbahPassword.this, ""+pesan, Toast.LENGTH_SHORT).show();
                                if(status){
                                    new AlertDialog.Builder(UbahPassword.this)
                                            .setMessage("Berhasil Mengupdate Data")
                                            .setCancelable(false)
                                            .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                Intent i = new Intent(editprofile.this,dashboard.class);
//                                                startActivity(i);
//                                                setResult(RESULT_OK,i);
//                                                editprofile.this.finish();
                                                    startActivity ( new Intent ( UbahPassword.this, dashboard.class ) );

                                                }
                                            })
                                            .show();
                                }else{
                                    new AlertDialog.Builder(UbahPassword.this)
                                            .setMessage("Gagal Mengupdate Data")
                                            .setCancelable(false)
                                            .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = getIntent();
                                                    setResult(RESULT_CANCELED,i);
                                                    UbahPassword.this.finish();
                                                }
                                            })
                                            .show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }
}