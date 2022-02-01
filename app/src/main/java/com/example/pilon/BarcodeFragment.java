package com.example.pilon;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONObject;


public class BarcodeFragment extends Fragment {
    private CodeScanner mCodeScanner;
    boolean CameraPermission = false;
    final int CAMERA_PERM = 1;
    String id, nama, nik, pilihan;
    SharedPreferences shp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity activity = getActivity ( );
        View view = inflater.inflate ( R.layout.fragment_barcode, container, false );
        CodeScannerView scannerView = view.findViewById ( R.id.scanner_view );
        shp = getActivity ().getSharedPreferences ( "mydata", Context.MODE_PRIVATE );
        id = shp.getString ( "id", null);
        nik = shp.getString ( "nik", null);
        mCodeScanner = new CodeScanner ( activity, scannerView );
        askPermission ( );
        mCodeScanner.setDecodeCallback ( new DecodeCallback ( ) {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread ( new Runnable ( ) {
                    @Override
                    public void run() {
                        pilihan = result.getText ();
                        Toast.makeText ( activity, pilihan, Toast.LENGTH_SHORT ).show ( );
                        kirimData();

//                        new Handler ( ).postDelayed ( new Runnable ( ) {
//                            @Override
//                            public void run() {
//                                validasiData ( );
//                            }
//                        }, 1000 );
                    }
                } );
            }
        } );
        scannerView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview ( );
            }
        } );
        return view;
    }

    private void askPermission(){

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){


            if (ActivityCompat.checkSelfPermission(getActivity (), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){

                ActivityCompat.requestPermissions(getActivity (),new String[]{Manifest.permission.CAMERA},CAMERA_PERM);


            }else {

                mCodeScanner.startPreview();
                CameraPermission = true;
            }

        }


    }
    @Override
    public void onPause() {
        if (CameraPermission){
            mCodeScanner.releaseResources();
        }

        super.onPause();
    }

    void kirimData(){
        AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/addBarcode.php")
                .addBodyParameter("nik",nik)
                .addBodyParameter("pilihan",pilihan)
                .setPriority( Priority.MEDIUM)
                .setTag("Tambah Data")
                .build ()
                .getAsJSONObject ( new JSONObjectRequestListener ( ) {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cekTambah",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
//                            Toast.makeText(getActivity (), ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(getActivity ())
                                        .setMessage("Berhasil Memilih !")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                updateSuara ();
                                                startActivity(new Intent ( getActivity ().getApplicationContext (), dashboard.class));
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(getActivity ())
                                        .setMessage("Maaf Anda sudah memilih !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity ( new Intent ( getActivity ().getApplicationContext (), dashboard.class ) );
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
    void updateSuara(){
        AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/updateJumlah.php")
                .addBodyParameter("pilihan",pilihan)
                .setTag("Update Suara")
                .setPriority( Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener () {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("respondJumlahSuara",""+response);
                        try{
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
//                            Toast.makeText(getActivity ().getApplicationContext (), ""+pesan, Toast.LENGTH_SHORT).show();
                            if(status){
//                                Toast.makeText(getActivity ().getApplicationContext (), "berhasil", Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(getActivity ().getApplicationContext (), "gagal", Toast.LENGTH_SHORT).show();
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