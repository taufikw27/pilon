package com.example.pilon;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import org.json.JSONArray;
import org.json.JSONObject;

public class dashboard extends AppCompatActivity {
    MeowBottomNavigation bottomNav;
    FrameLayout frame;
    SharedPreferences shp, shp1;
    String nikk;
    String id, nama, NIK, pass, no_hp, email, photo;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_dashboard );
        bottomNav = findViewById ( R.id.idbottomnav );
        frame = findViewById ( R.id.idframe );
        progressDialog = new ProgressDialog ( this );
        shp = getSharedPreferences ( "mydata", MODE_PRIVATE );
        shp1 = getSharedPreferences ( "mydata", MODE_PRIVATE );
        nikk = shp.getString ( "nik", null );


        bottomNav.add ( new MeowBottomNavigation.Model ( 1, R.drawable.ic_home ) );
        bottomNav.add ( new MeowBottomNavigation.Model ( 2, R.drawable.ic_hasil ) );
        bottomNav.add ( new MeowBottomNavigation.Model ( 3, R.drawable.ic_barcode ) );
        bottomNav.add ( new MeowBottomNavigation.Model ( 4, R.drawable.ic_notif ) );
        bottomNav.add ( new MeowBottomNavigation.Model ( 5, R.drawable.ic_account ) );
        bottomNav.setOnShowListener ( new MeowBottomNavigation.ShowListener ( ) {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                Fragment fragment = null;
                switch (item.getId ( )) {
                    case 1:
                        fragment = new HomeFragment ( );
                        break;
                    case 2:
                        fragment = new ResultFragment ( );
                        break;
                    case 3:
                        fragment = new BarcodeFragment ( );
                        break;
                    case 4:
                        fragment = new NotificationFragment ( );
                        break;
                    case 5:
                        fragment = new AccountFragment ( );
                        break;
                }
                getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.idframe, fragment ).commit ( );
            }
        } );
        //set notifikasi
        bottomNav.setCount ( 4, "2" );
        //set barcode fragment initally selected
        bottomNav.show ( 1, true );

        bottomNav.setOnClickMenuListener ( new MeowBottomNavigation.ClickListener ( ) {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        } );

        bottomNav.setOnReselectListener ( new MeowBottomNavigation.ReselectListener ( ) {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        } );
        progressDialog.setMessage ( "Memuat Data....." );
        progressDialog.setCancelable ( false );
        progressDialog.show ( );
        getData ();

    }
    public void getData(){
        AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/getDash.php")
                .addBodyParameter("nik",nikk)
                .setPriority( Priority.MEDIUM)
                .setTag("getData")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener () {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekdata",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
//                            Toast.makeText(getActivity ().getApplicationContext (), ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                for(int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);
                                    id = jo.getString ( "id");
                                    nama = jo.getString("nama");
                                    NIK = jo.getString("nik");
                                    no_hp = jo.getString("no_hp");
                                    pass = jo.getString("password");
                                    email = jo.getString("email");
                                    photo = jo.getString("photo");

                                }
                                SharedPreferences.Editor editor = shp1.edit();
                                editor.clear ();
                                editor.commit ();
                                editor.putString ( "id",id );
                                editor.putString ( "nama",nama );
                                editor.putString ( "nik",NIK );
                                editor.putString ( "photo",photo );
                                editor.putString ( "no_hp",no_hp );
                                editor.putString ( "email",email );
                                editor.commit ();
//                                Toast.makeText(getActivity ().getApplicationContext (), "berhasil "+ nama, Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(dashboard.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
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