package com.example.pilon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class signup extends AppCompatActivity {
    EditText NAMA,NIK, PASS1,HP, EMAIL;
    TextView textView, login;
    Button Signup;
    ProgressDialog progressDialog;
    String nama,nik, password, no_hp, email;
    float v =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_signup );
        textView = findViewById ( R.id.textView );
        NAMA = findViewById ( R.id.idSTnama );
        NIK = findViewById ( R.id.idSTnik );
        PASS1 = findViewById ( R.id.idstPas );
        HP = findViewById ( R.id.idstrno );
        EMAIL = findViewById ( R.id.idstemail );
        Signup = findViewById ( R.id.idsgnup );
        login = findViewById ( R.id.idback );

//        NAMA.setTranslationX ( 800 );
//        NIK.setTranslationX ( 800 );
//        PASS1.setTranslationX ( 800 );
//        HP.setTranslationX ( 800 );
//        EMAIL.setTranslationX ( 800 );
//        Signup.setTranslationX ( 800 );
//
//
//        NAMA.setAlpha ( v );
//        NIK.setAlpha ( v );
//        PASS1.setAlpha ( v );
//        HP.setAlpha ( v );
//        EMAIL.setAlpha ( v );
//        Signup.setAlpha ( v );
//
//        NAMA.animate ( ).translationX ( 0 ).alpha ( 1 ).setDuration ( 800 ).setStartDelay ( 300 ).start ( );
//        NIK.animate ( ).translationX ( 0 ).alpha ( 1 ).setDuration ( 800 ).setStartDelay ( 300 ).start ( );
//        PASS1.animate ( ).translationX ( 0 ).alpha ( 1 ).setDuration ( 800 ).setStartDelay ( 300 ).start ( );
//        HP.animate ( ).translationX ( 0 ).alpha ( 1 ).setDuration ( 800 ).setStartDelay ( 300 ).start ( );
//        EMAIL.animate ( ).translationX ( 0 ).alpha ( 1 ).setDuration ( 800 ).setStartDelay ( 300 ).start ( );
//        Signup.animate ( ).translationX ( 0 ).alpha ( 1 ).setDuration ( 800 ).setStartDelay ( 300 ).start ( );

        progressDialog = new ProgressDialog ( this );

        Signup.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                new androidx.appcompat.app.AlertDialog.Builder ( signup.this )
                        .setMessage("Apakah data anda sudah benar ?")
                        .setCancelable(false)
                        .setPositiveButton ( "Ya", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.setMessage ( "Menambahkan Data..." );
                                progressDialog.setCancelable ( false );
                                progressDialog.show ( );

                                nama = NAMA.getText ( ).toString ( );
                                nik = NIK.getText ( ).toString ( );
                                password = PASS1.getText ( ).toString ( );
                                email = EMAIL.getText ( ).toString ( );
                                no_hp = HP.getText ( ).toString ( );

                                new Handler ( ).postDelayed ( new Runnable ( ) {
                                    @Override
                                    public void run() {
                                        validasiData ( );
                                    }
                                }, 1000 );
                            }
                        })
                        .setNegativeButton ( "Tidak", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel ( );
                            }
                        } ).show ();
            }
        } );
        login.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                onBackPressed ();
            }
        } );
    }

    void validasiData(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(nama.equals( "") || nik.equals("") || no_hp.equals("") || email.equals("") || password.equals("")){
            progressDialog.dismiss();
            Toast.makeText(signup.this, "Maaf, Data belum lengkap", Toast.LENGTH_SHORT).show();
        }else if(password.length ()<8){
            progressDialog.dismiss ();
//            PASS1.setError ( "password kurang dari 8 karakter",getDrawable ( R.drawable.calon4 ) );
            Toast.makeText(signup.this, "Password kurang dari 8 karakter !", Toast.LENGTH_SHORT).show();
        }
        else if(!email.matches ( emailPattern )){
            progressDialog.dismiss ();
            EMAIL.setError ( "Alamat Email anda Invalid" );
        }

        else {
            kirimData();
        }
    }
    void kirimData(){
        AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/addUsers.php")
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("nik",""+nik)
                .addBodyParameter("no_hp",""+no_hp)
                .addBodyParameter("email",""+email)
                .addBodyParameter("password",""+password)
                .setPriority( Priority.MEDIUM)
                .setTag("Tambah Users")
                .build ()
                .getAsJSONObject ( new JSONObjectRequestListener ( ) {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekTambah",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(signup.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(signup.this)
                                        .setMessage("Berhasil Menambahkan Data !")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Intent i = getIntent();
                                                //setResult(RESULT_OK,i);
                                                //add_mahasiswa.this.finish();
                                                startActivity(new Intent( signup.this, Login.class));
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(signup.this)
                                        .setMessage("Gagal Menambahkan Data !")
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
                        Log.d("ErrorTambahData",""+anError.getErrorBody());
                    }
                } );


    }
}