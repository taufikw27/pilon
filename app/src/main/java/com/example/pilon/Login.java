package com.example.pilon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
   com.google.android.material.textfield.TextInputEditText ETNik, ETPassword;
    TextView forgot;
    Button Login;
    TextView register;
    ArrayList<String> id,nama,NIK,pass, no_hp, email,photo;
    String nik, password;
    TextView fg;
    ProgressDialog progressDialog;
    SharedPreferences shp,shp1;
    float v =0;
    @Override
    protected void onResume() {
        shp = getSharedPreferences("mydata", MODE_PRIVATE);
        if (shp.contains("id"))
        {
            Intent i = new Intent(getApplicationContext(),dashboard.class);
            startActivity(i);
        }
        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        ETNik = findViewById( R.id.idNIK);
        ETPassword = findViewById( R.id.idPas);
        forgot = findViewById( R.id.fg);
        Login = findViewById( R.id.idlogin);
        register = findViewById( R.id.rg);
        fg = findViewById ( R.id.fg );

//        text1 = findViewById ( R.id.textInputLayout);
//        text2 = findViewById ( R.id.textInputLayout2 );
        progressDialog      = new ProgressDialog(this);
        shp = getSharedPreferences("mydata", MODE_PRIVATE);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Proses Login");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if(ETNik.getText ().toString ().length ()==0){
                    progressDialog.dismiss ();
                    ETNik.setError ( "NIK Masih Kosong!" );
                }
                else if(ETPassword.getText ().toString ().length ()==0) {
                    progressDialog.dismiss ();
                    Toast.makeText(Login.this, "Password Belum terisi !", Toast.LENGTH_SHORT).show();
                }
                nik = ETNik.getText().toString();
                password = ETPassword.getText().toString();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validasiData();
                    }
                }, 1000);
            }
        });


        register.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( com.example.pilon.Login.this, signup.class));

            }
        } );
//        ETNik.setTranslationX (800);
//        ETPassword.setTranslationX (800);
//        forgot.setTranslationX (800);
//        Login.setTranslationX (800);
//        register.setTranslationX (800);
//        text1.setTranslationX (800);
//        text2.setTranslationX (800);
//
//        ETNik.setAlpha (v);
//        ETPassword.setAlpha (v);
//        forgot.setAlpha (v);
//        Login.setAlpha (v);
//        register.setAlpha (v);
//        text1.setAlpha (v);
//        text2.setAlpha (v);
//
//        ETNik.animate ().translationX (0).alpha (1).setDuration (800).setStartDelay (300).start ();
//        ETPassword.animate ().translationX (0).alpha (1).setDuration (800).setStartDelay (300).start ();
//        forgot.animate ().translationX (0).alpha (1).setDuration (800).setStartDelay (300).start ();
//        Login.animate ().translationX (0).alpha (1).setDuration (800).setStartDelay (300).start ();
//        register.animate ().translationX (0).alpha (1).setDuration (800).setStartDelay (300).start ();
//        text1.animate ().translationX (0).alpha (1).setDuration (800).setStartDelay (300).start ();
//        text2.animate ().translationX (0).alpha (1).setDuration (800).setStartDelay (300).start ();

    }

    void validasiData(){
        if(nik.equals("") && password.equals("")){
            progressDialog.dismiss();
            Toast.makeText(Login.this, "Periksa kembali data yang Anda masukkan !", Toast.LENGTH_SHORT).show();
        }else {
            kirimData();
            getData();
        }
    }
    void kirimData(){
        AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/loginUsers.php")
                .addBodyParameter("nik",""+nik)
                .addBodyParameter("password",""+password)
                .setPriority(Priority.MEDIUM)
                .setTag("Login")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekLogin",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(Login.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Login.this)
                                        .setMessage("Berhasil Login!")
                                        .setCancelable(false)
                                        .show();
                                startActivity(new Intent( Login.this, dashboard.class));
//
                            }
                            else{
                                new AlertDialog.Builder(Login.this)
                                        .setMessage("Gagal Login, Periksa Kembali NIK dan Password Anda!")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Intent i = getIntent();
                                                //setResult(RESULT_CANCELED,i);
                                                //add_mahasiswa.this.finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ErrorLogin",""+anError.getErrorBody());
                    }
                });
    }
    void initializeArray(){
        id      = new ArrayList<String>();
        nama    = new ArrayList<String>();
        NIK     = new ArrayList<String>();
        pass    = new ArrayList<String>();
        no_hp   = new ArrayList<String>();
        email   = new ArrayList<String>();
        photo   = new ArrayList<String>();

        id.clear();
        nama.clear();
        NIK.clear();
        pass.clear();
        no_hp.clear();
        email.clear();
        photo.clear ();

    }

    public void getData(){
        initializeArray ();
        AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/getLogin.php")
                .addBodyParameter("nik",""+nik)
                .addBodyParameter("password",""+password)
                .setPriority(Priority.MEDIUM)
                .setTag("Login")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekLogin",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Log.d("status",""+status);
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                for(int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);
                                    id.add ( jo.getString ( "id" ) );
                                    nama.add(jo.getString("nama"));
                                    NIK.add(jo.getString("nik"));
                                    no_hp.add(jo.getString("no_hp"));
                                    pass.add(jo.getString("password"));
                                    email.add(jo.getString("email"));
                                    photo.add(jo.getString("photo"));

                                }
                                SharedPreferences.Editor editor = shp.edit();
                                editor.putString ( "id", id.get ( 0 ) );
                                editor.putString ( "nama", nama.get ( 0 ) );
                                editor.putString ( "nik", NIK.get ( 0 ) );
                                editor.putString ( "no_hp", no_hp.get ( 0 ) );
                                editor.putString ( "password", pass.get ( 0 ) );
                                editor.putString ( "email", email.get ( 0 ) );
                                editor.putString ( "photo", photo.get ( 0 ) );
                                editor.commit();
                            }
                            else{
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ErrorLogin",""+anError.getErrorBody());
                    }
                });
    }
}