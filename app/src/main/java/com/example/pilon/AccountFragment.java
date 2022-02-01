package com.example.pilon;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class AccountFragment extends Fragment {
    ListView listView,listview1;
    SimpleAdapter adapter,adapter1;
    ImageView IVprofile, profile;
    SharedPreferences shp1, shp, RF;
    String nama, nik, photo;
    HashMap<String,String> map;
    HashMap<String,String> map1;
    TextView tvakun, tvNik;
    ArrayList<HashMap<String,String>> mylist;
    ArrayList<HashMap<String,String>> mylist1;
    String s1[],s2[],s3[], gambar[],gambar1[];
    CardView logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_account, container, false);
        // Inflate the layout for this fragment
//        info = view.findViewById ( R.id.idTVinfo );
        IVprofile = view.findViewById ( R.id.IVprofle );
        profile = view.findViewById ( R.id.profile_image );
        listView = view.findViewById( R.id.idLV);
        listview1 = view.findViewById( R.id.idLV_seputarpilon);
        logout = view.findViewById ( R.id.idcv3);
        tvakun = view.findViewById ( R.id.idakun );
        tvNik = view.findViewById ( R.id.idnik );
        shp1 = getActivity().getSharedPreferences ("mydata", MODE_PRIVATE);
        shp = getActivity().getSharedPreferences ("mydata", MODE_PRIVATE);
        RF = getActivity().getSharedPreferences ("hasil", MODE_PRIVATE);

        s1 = getResources().getStringArray( R.array.pengaturan);
        s2 = getResources().getStringArray( R.array.deskripsi);
        s3 = getResources().getStringArray( R.array.seputar_pilon);
        gambar = new String[]{
                Integer.toString( R.drawable.ic_lock),
                Integer.toString( R.drawable.ic_languages)
        };
        gambar1 = new String[]{
                Integer.toString( R.drawable.logopilon),
                Integer.toString( R.drawable.ic_syarat),
                Integer.toString( R.drawable.ic_privacy)
        };
//        info.setOnClickListener ( new View.OnClickListener ( ) {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), infoFragment.class);
//                startActivity(intent);
//            }
//        } );
         nama = shp1.getString ( "nama",null );
         nik = shp1.getString ( "nik",null );
        photo = shp1.getString ( "photo",null );
        tvakun.setText ( nama );
        tvNik.setText ( nik );
        if(photo.equals ( "" )){
            Picasso.get().load("http://tkjb2019.com/mobile/image/profile_default.png").into(profile);
        }
        else{
            Picasso.get().load("http://tkjb2019.com/mobile/image/"+photo).into ( profile);
        }
        logout.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                clear ();
                clear1 ();
                SharedPreferences.Editor editor = shp.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);

            }
        } );

        mylist = new ArrayList<HashMap<String, String>>();
        //perulangan untuk mendapatkan data menggunakan hashmap
        for (int i=0; i<s1.length; i++){
            map = new HashMap<String, String>();
            map.put("pengaturan", s1[i]);
            map.put("deskripsi", s2[i]);
            map.put("gambar",gambar[i]);
            mylist.add(map);
        }

        //membuat simple adapter untuk menghubungkan data dengan layout
        adapter = new SimpleAdapter(getActivity(), mylist, R.layout.item_setting,
                new String[]{"pengaturan", "deskripsi","gambar"}, new int[]{(R.id.idpengaturan),(R.id.iddeskripsi),(R.id.idgambar)});
        //menghubungkan adapter ke lisView pada layout activit_main.xml
        listView.setAdapter(adapter);
        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    Intent intent = new Intent(getActivity ().getApplicationContext () , UbahPassword.class).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                    getActivity ().getApplicationContext ().startActivity(intent);
                }
                else if(i==1){
                    Intent intent = new Intent(getActivity ().getApplicationContext () , UbahBahasa.class).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                    getActivity ().getApplicationContext ().startActivity(intent);
                }
            }
        } );

        mylist1 = new ArrayList<HashMap<String, String>>();
        //perulangan untuk mendapatkan data menggunakan hashmap
        for (int i=0; i<s3.length; i++){
            map1 = new HashMap<String, String>();
            map1.put("seputar_pilon", s3[i]);
            map1.put("gambar",gambar1[i]);
            mylist1.add(map1);
        }
        //membuat simple adapter untuk menghubungkan data dengan layout
        adapter1 = new SimpleAdapter(getActivity(), mylist1, R.layout.item_seputar_pilon,
                new String[]{"seputar_pilon","gambar"}, new int[]{(R.id.idseputarpilon),(R.id.idgambar_seputarpilon)});
        //menghubungkan adapter ke lisView pada layout activit_main.xml
        listview1.setAdapter(adapter1);
        listview1.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Intent intent = new Intent(getActivity ().getApplicationContext () , infoFragment.class).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                    getActivity ().getApplicationContext ().startActivity(intent);
                }
                if(i==1){
                    Intent intent = new Intent(getActivity ().getApplicationContext () , sdk.class).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                    getActivity ().getApplicationContext ().startActivity(intent);
                }
                if(i==2){
                    Intent intent = new Intent(getActivity ().getApplicationContext () , kebijakandanprivasi.class).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                    getActivity ().getApplicationContext ().startActivity(intent);
                }
            }
        } );

        IVprofile.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), editprofile.class);
                startActivity(intent);
            }
        } );
        return view;
    }
    public void clear(){
        SharedPreferences.Editor editor = shp1.edit();
        editor.clear ();
        editor.commit ();
    } public void clear1(){
        SharedPreferences.Editor editor = RF.edit();
        editor.clear ();
        editor.commit ();
    }
}