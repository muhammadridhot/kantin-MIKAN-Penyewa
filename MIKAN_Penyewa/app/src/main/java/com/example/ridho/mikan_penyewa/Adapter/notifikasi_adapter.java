package com.example.ridho.mikan_penyewa.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ridho.mikan_penyewa.Model.notifikasi_penyewa;
import com.example.ridho.mikan_penyewa.R;

import java.util.ArrayList;

public class notifikasi_adapter extends RecyclerView.Adapter<notifikasi_adapter.ViewHolder> {

    Context ctx;
    ArrayList<notifikasi_penyewa> data;
    private OnItemClickListener monItemClickListener;

    public interface OnItemClickListener{
        public void OnItemClick(View view,int position);
    }

    public notifikasi_adapter(Context ctx, ArrayList<notifikasi_penyewa> data, OnItemClickListener monItemClickListener) {
        this.ctx = ctx;
        this.data = data;
        this.monItemClickListener = monItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.tampilan_notifikasi_pesanan,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.nama.setText(data.get(i).getNamaPemesan());
        viewHolder.meja.setText(data.get(i).getMeja_makan());
        viewHolder.tgl.setText(data.get(i).getTgl_pesan());
        if (data.get(i).getStatus().equals("Menunggu") || data.get(i).getStatus().equals("Diterima")){
            viewHolder.container.setEnabled(true);
        }else {
            viewHolder.container.setEnabled(false);
        }
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monItemClickListener.OnItemClick(v,i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama, meja, tgl;
        View container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.txtNamaPemesan);
            meja = (TextView)itemView.findViewById(R.id.txtMeja);
            tgl = (TextView)itemView.findViewById(R.id.RiwayattglOrder);
            container = itemView;
        }
    }
}
