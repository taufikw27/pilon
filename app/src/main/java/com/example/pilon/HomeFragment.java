package com.example.pilon;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout srl_main;
    TextView TVnama;
    AdapterRecyclerView adapter;
    SharedPreferences shp, shp1;
    ImageView profile;
    ArrayList<String> array_id,array_nama,array_js,array_photo;
    ProgressDialog progressDialog;
    String nikk, name, foto;
    String s1[], s2[];
    int images[] = {R.drawable.calon1,
            R.drawable.calon2,
            R.drawable.calon3,
            R.drawable.calon4};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate ( R.layout.fragment_home, container, false );
        recyclerView = view.findViewById ( R.id.my_recycler );

        s1 = getResources ( ).getStringArray ( R.array.nama_calon );
        s2 = getResources ( ).getStringArray ( R.array.jumlah_suara );
        profile = view.findViewById ( R.id.profile_image );
        progressDialog = new ProgressDialog ( getActivity ( ) );
//        shp = getActivity ( ).getSharedPreferences ( "mydata", MODE_PRIVATE );
        shp1 = getActivity ( ).getSharedPreferences ( "mydata", MODE_PRIVATE );
//        nikk = shp.getString ( "nik", null );

//        srl_main.setOnRefreshListener ( new SwipeRefreshLayout.OnRefreshListener ( ) {
//            @Override
//            public void onRefresh() {
//                scrollRefresh ( );
//                srl_main.setRefreshing ( false );
//            }
//        } );
//        srl_main.setColorSchemeColors (
//                getResources ( ).getColor ( android.R.color.holo_blue_bright ),
//                getResources ( ).getColor ( android.R.color.holo_green_light ),
//                getResources ( ).getColor ( android.R.color.holo_orange_light ),
//                getResources ( ).getColor ( android.R.color.holo_red_light )
//
//        );
//
//        scrollRefresh ( );
//        progressDialog.setMessage ( "Memuat Data....." );
//        progressDialog.setCancelable ( false );
//        progressDialog.show ( );
//        getData ();
//        progressDialog.dismiss ();
        shp1 = getActivity ( ).getSharedPreferences ( "mydata", MODE_PRIVATE );
        name = shp1.getString ( "nama", null );
        foto = shp1.getString ( "photo", null );
        TVnama = view.findViewById ( R.id.idusernama );
        TVnama.setText ( name );
        if (foto.equals ( "" )) {
            Picasso.get ( ).load ( "http://tkjb2019.com/mobile/image/profile_default.png" ).into ( profile );
        } else {
            Picasso.get ( ).load ( "http://tkjb2019.com/mobile/image/" + foto).into ( profile );
        }

        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( getActivity ( ), RecyclerView.HORIZONTAL, false ) );
        getData ();


        return view;
    }
    void initializeArray(){
        array_id      = new ArrayList<String>();
        array_nama      = new ArrayList<String>();
        array_js       = new ArrayList<String>();
        array_photo     = new ArrayList<String>();

        array_id.clear();
        array_nama.clear();
        array_js.clear();
        array_photo.clear();

    }

    public void getData(){
        initializeArray();
        AndroidNetworking.get("http://tkjb2019.com/mobile/api_kelompok_5/getKandidat.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                for(int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);
                                    array_id.add ( jo.getString ( "id" ) );
                                    array_nama.add(jo.getString("nama"));
                                    array_js.add(jo.getString("jumlah_suara"));
                                    array_photo.add(jo.getString("photo"));

                                }
                                final AdapterRecyclerView adapter = new AdapterRecyclerView ( getActivity ( ), array_nama, array_js,array_photo);
                                recyclerView.setAdapter ( adapter );
                                recyclerView.setNestedScrollingEnabled ( true );


                            }else{
                                Toast.makeText(getActivity (), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
}
}
