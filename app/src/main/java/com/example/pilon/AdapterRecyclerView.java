package com.example.pilon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {
   String data1[], data2[];
   int images[];
    private final Activity context;
    private ArrayList<String> vNama;
    private ArrayList<String> vjs;
    private ArrayList<String> vPhoto;

   public AdapterRecyclerView(Context ct, ArrayList<String> nama,ArrayList<String> js, ArrayList<String> photo){
       this.vNama = nama;
       this.vjs = js;
       this.vPhoto = photo;
       this.context = (Activity) ct;
   }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_grid,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.namacalon.setText(vNama.get ( position ));
       holder.jumlahsuara.setText(vjs.get ( position ));
        if (vPhoto.get(position).equals(""))
        {
            Picasso.get().load("http://tkjb2019.com/mobile/image/profile.png").into( holder.imgcalon);
        }
        else
        {
            Picasso.get().load("http://tkjb2019.com/mobile/image/"+vPhoto.get(position)).into( holder.imgcalon);
        }
    }

    @Override
    public int getItemCount() {

        return vNama.size ();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namacalon,jumlahsuara;
        ImageView imgcalon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namacalon = itemView.findViewById( R.id.namacalon);
            jumlahsuara = itemView.findViewById( R.id.jumlahsuara);
            imgcalon = itemView.findViewById( R.id.imgcalon);
        }
    }


    }


