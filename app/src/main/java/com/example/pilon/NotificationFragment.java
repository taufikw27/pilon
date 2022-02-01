package com.example.pilon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    ArrayList<String> array_nama,array_pesan, array_id, array_photo;
    ProgressDialog progressDialog;
    private Context mContext;
    ListView LV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_notification, container, false);
            LV = view.findViewById( R.id.idLV);
            progressDialog = new ProgressDialog(getActivity ());
        progressDialog.setMessage("Mengambil Data.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getData ();
        return view;
    }
    void initializeArray(){
        array_id      = new ArrayList<String>();
        array_nama      = new ArrayList<String>();
        array_pesan       = new ArrayList<String>();
        array_photo     = new ArrayList<String>();

        array_id.clear();
        array_nama.clear();
        array_pesan.clear();
        array_photo.clear();

    }

    public void getData(){
        initializeArray();
        AndroidNetworking.get("http://tkjb2019.com/mobile/api_kelompok_5/getPesan.php")
                .setTag("Get Data")
                .setPriority( Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener () {
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
                                    array_pesan.add(jo.getString("pesan"));
                                    array_photo.add(jo.getString("gambar"));

                                }

                                //Menampilkan data berbentuk adapter menggunakan class CLVDataUser
                                final list_adapter_pesan adapter = new list_adapter_pesan (getActivity (),array_nama,array_pesan,array_photo);
                                //Set adapter to list
                                LV.setAdapter(adapter);

                                //edit and delete
                                LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TestKlik",""+array_nama.get(position));
                                        Toast.makeText(getActivity (), array_nama.get(position), Toast.LENGTH_SHORT).show();

                                        //Setelah proses koneksi keserver selesai, maka aplikasi akan berpindah class
                                        //DataActivity.class dan membawa/mengirim data-data hasil query dari server.
                                        Intent i = new Intent(getActivity ().getApplicationContext (), notifikasi_view.class);
                                        i.putExtra("id",array_id.get(position));
                                        i.putExtra("nama",array_nama.get(position));
                                        i.putExtra("pesan",array_pesan.get(position));
                                        i.putExtra("photo",array_photo.get(position));
                                        startActivity ( i );
                                    }
                                });


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