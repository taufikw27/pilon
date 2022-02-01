package com.example.pilon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class list_adapter_pesan extends ArrayAdapter<String> {
    //Declarasi variable
    private final Activity context;
    private ArrayList<String> vNama;
    private ArrayList<String> vpesan;
    private ArrayList<String> vPhoto;

    public list_adapter_pesan(Activity context, ArrayList<String> nama, ArrayList<String> pesan, ArrayList<String> Photo)
    {
        super(context, R.layout.list_notif,nama);
        this.context    = context;
        this.vNama     = nama;
        this.vpesan      = pesan;
        this.vPhoto     = Photo;
    }



    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView= inflater.inflate( R.layout.list_notif, null, true);

        //Declarasi komponen
        TextView name       = rowView.findViewById( R.id.idTVnama);
        TextView pesan        =  rowView.findViewById( R.id.idTVdes);
        ImageView photo     = rowView.findViewById( R.id.IVgambar);

        //Set Parameter Value sesuai widget textview
        name.setText(vNama.get(position));
        pesan.setText(vpesan.get(position));

        if (vPhoto.get(position).equals(""))
        {
            Picasso.get().load("http://tkjb2019.com/mobile/image/profile.png").into(photo);
        }
        else
        {
            Picasso.get().load("http://tkjb2019.com/mobile/image/"+vPhoto.get(position)).into(photo);
        }
        ;

        return rowView;
    }


}

