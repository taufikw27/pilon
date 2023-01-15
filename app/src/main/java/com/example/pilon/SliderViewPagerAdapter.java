package com.example.pilon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderViewPagerAdapter extends PagerAdapter {
    Context context;
    public SliderViewPagerAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService ( context.LAYOUT_INFLATER_SERVICE );
        View view = layoutInflater.inflate ( R.layout.slide_layout,container,false );
        ImageView logo = view.findViewById ( R.id.idSlide1 );
        TextView title = view.findViewById ( R.id.idjudul );
        TextView deskripsi = view.findViewById ( R.id.iddeskripsi );
        ImageView next = view.findViewById ( R.id.next );
        ImageView back = view.findViewById ( R.id.back );
        Button Btnget = view.findViewById ( R.id.btnget );
        Btnget.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent ( context, Login.class );
                intent.setFlags (intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity ( intent );
            }
        } );
        next.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
            onboarding.viewPager.setCurrentItem ( position+1 );
            }
        } );
        back.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
            onboarding.viewPager.setCurrentItem ( position-1 );
            }
        } );
        switch (position){
            case 0:
                logo.setImageResource ( R.drawable.maskot1 );

                title.setText ( R.string.halaman1 );
                deskripsi.setText ( R.string.deskripsi1 );
                back.setVisibility ( View.GONE );
                next.setVisibility ( View.GONE );
                Btnget.setVisibility ( View.GONE );
                break;
            case 1:
                logo.setImageResource ( R.drawable.maskot );


                title.setText ( R.string.halaman2 );
                deskripsi.setText ( R.string.deskripsi2 );
                back.setVisibility ( View.GONE);
                next.setVisibility ( View.GONE);
                Btnget.setVisibility ( View.GONE );
                break;
            case 2:
                logo.setImageResource ( R.drawable.gambar2 );


                title.setText ( R.string.halaman3 );
                deskripsi.setText ( R.string.deskripsi3 );

                next.setVisibility ( View.GONE );
                back.setVisibility ( View.GONE);

                break;

        }


        container.addView ( view );
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView ( (View) object );
    }
}
