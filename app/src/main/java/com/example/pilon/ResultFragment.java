package com.example.pilon;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultFragment extends Fragment {
    TextView tvR, tvPython, tvCPP, tvJava, tvjumlah, tv1, tv2, tv3, tv4, tvtotal;
    ArrayList<String> id, kandidat;
    ArrayList<Integer> jumsu;
    PieChart pieChart;
    SharedPreferences RF;
    String jumlah, kandidat1,kandidat2,kandidat3,kandidat4, total;
    Integer  hasil,calon1,calon2, calon3,calon4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_result, container, false);
        tvR = view.findViewById( R.id.tvR);
        tvPython = view.findViewById( R.id.tvPython);
        tvCPP = view.findViewById( R.id.tvCPP);
        tvJava = view.findViewById( R.id.tvJava);
        pieChart = view.findViewById( R.id.piechart);
        tvjumlah = view.findViewById ( R.id.idjumlah );
        tv1 = view.findViewById ( R.id.idtv1 );
        tv2 = view.findViewById ( R.id.idtv2 );
        tv3 = view.findViewById ( R.id.idtv3 );
        tv4 = view.findViewById ( R.id.idtv4 );
        tvtotal = view.findViewById ( R.id.idtotal );


        id          = new ArrayList<String>();
        kandidat    = new ArrayList<String>();
        jumsu       = new ArrayList<Integer>();

        id.clear();
        kandidat.clear();
        jumsu.clear();
        RF = getActivity ().getSharedPreferences ( "hasil", Context.MODE_PRIVATE );
        getJumlahSuara ();
        getSuaramasuk ();
        getKandidat ();
        //pemnaggilan SharePreferences
        jumlah = RF.getString ( "jumlah",null );
        calon1 = RF.getInt ( "calon1", 0 );
        calon2 = RF.getInt ( "calon2", 0 );
        calon3 = RF.getInt ( "calon3", 0 );
        calon4 = RF.getInt ( "calon4", 0 );
        kandidat1 = RF.getString ( "kandidat1",null );
        kandidat2 = RF.getString ( "kandidat2",null );
        kandidat3 = RF.getString ( "kandidat3",null );
        kandidat4 = RF.getString ( "kandidat4",null );
        total = RF.getString ( "total",null );
//        hasil = Integer.toString ( jumlah );

        tv1.setText ( kandidat1 );
        tv2.setText ( kandidat2 );
        tv3.setText ( kandidat3 );
        tv4.setText ( kandidat4 );
        tvtotal.setText ( "Jumlah Suara Masuk : "+total );

        tvjumlah.setText ( "Jumlah Pemilih : "+jumlah);
        setData();
        return view;
    }
    private void setData()
    {

        // Set the percentage of language used
        tvR.setText(Integer.toString(calon1));
        tvPython.setText(Integer.toString(calon2));
        tvCPP.setText(Integer.toString(calon3));
        tvJava.setText(Integer.toString(calon4));

        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel (
                        ""+kandidat1,
                        Integer.parseInt(tvR.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        ""+kandidat2,
                        Integer.parseInt(tvPython.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        ""+kandidat3,
                        Integer.parseInt(tvCPP.getText().toString()),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        ""+kandidat4,
                        Integer.parseInt(tvJava.getText().toString()),
                        Color.parseColor("#29B6F6")));

        // To animate the pie chart
        pieChart.startAnimation();
    }
    public void getJumlahSuara(){
        AndroidNetworking.get("http://tkjb2019.com/mobile/api_kelompok_5/getJS.php")
                .setTag("Get Data")
                .setPriority( Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener () {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                jumlah = response.getString ("result");
                                SharedPreferences.Editor editor = RF.edit ();
                                editor.clear ();
                                editor.commit ();
                                editor.putString ( "jumlah", jumlah );
                                editor.commit ();
//                                Toast.makeText(getActivity ().getApplicationContext (), ""+jumlah, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity ().getApplicationContext (), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

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
    }public void getSuaramasuk(){
        AndroidNetworking.get("http://tkjb2019.com/mobile/api_kelompok_5/getSuaramasuk.php")
                .setTag("Get Data")
                .setPriority( Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener () {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                total = response.getString ("result");
                                SharedPreferences.Editor editor = RF.edit ();
                                editor.putString ( "total", total );
                                editor.commit ();
//                                Toast.makeText(getActivity ().getApplicationContext (), ""+total, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity ().getApplicationContext (), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

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
    public void getKandidat(){
        AndroidNetworking.get("http://tkjb2019.com/mobile/api_kelompok_5/getKandidat.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                for(int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);
                                    id.add ( jo.getString ( "id" ) );
                                    kandidat.add(jo.getString("nama"));
                                    jumsu.add ( jo.getInt ( "jumlah_suara" ) );

                                }
                                SharedPreferences.Editor editor = RF.edit ();
                                editor.putString ( "kandidat1",kandidat.get ( 0 ) );
                                editor.putString ( "kandidat2",kandidat.get ( 1 ) );
                                editor.putString ( "kandidat3",kandidat.get ( 2 ) );
                                editor.putString ( "kandidat4",kandidat.get ( 3 ) );
                                editor.putInt ( "calon1", jumsu.get ( 0 ) );
                                editor.putInt ( "calon2", jumsu.get ( 1) );
                                editor.putInt ( "calon3", jumsu.get ( 2 ) );
                                editor.putInt ( "calon4", jumsu.get ( 3 ) );
                                editor.commit ();
//                                Toast.makeText(getActivity ().getApplicationContext (), ""+kandidat.get ( 1 ), Toast.LENGTH_SHORT).show();


                            }else{
                                Toast.makeText(getActivity ().getApplicationContext (), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

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