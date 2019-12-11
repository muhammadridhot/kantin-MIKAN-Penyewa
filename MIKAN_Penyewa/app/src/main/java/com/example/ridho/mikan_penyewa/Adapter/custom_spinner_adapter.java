package com.example.ridho.mikan_penyewa.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ridho.mikan_penyewa.R;

public class custom_spinner_adapter extends BaseAdapter {

    private Context context;
    private int img[];
    private String[] namaBank;
    private LayoutInflater inflater;

    public custom_spinner_adapter(Context context, int[] img, String[] namaBank) {
        this.context = context;
        this.img = img;
        this.namaBank = namaBank;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.tampilan_spinner,null);
        ImageView icon = (ImageView)convertView.findViewById(R.id.imageView);
        TextView nama = (TextView)convertView.findViewById(R.id.textView);
        icon.setImageResource(img[position]);
        nama.setText(namaBank[position]);
        if (position == 0){
            icon.setVisibility(View.GONE);
            nama.setTextSize(17f);
//            nama.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        return convertView;
    }


}
