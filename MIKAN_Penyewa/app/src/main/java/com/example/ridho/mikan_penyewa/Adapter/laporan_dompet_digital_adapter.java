package com.example.ridho.mikan_penyewa.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ridho.mikan_penyewa.Model.laporan_saldo_dompet_digital_model;
import com.example.ridho.mikan_penyewa.R;

import java.util.ArrayList;

public class laporan_dompet_digital_adapter extends RecyclerView.Adapter<laporan_dompet_digital_adapter.ViewHolder> {
    Context ctx;
    ArrayList<laporan_saldo_dompet_digital_model> data;

    public laporan_dompet_digital_adapter(Context ctx, ArrayList<laporan_saldo_dompet_digital_model> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.tampilan_laporan_dompet_digital,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.heading.setText(data.get(i).getHeading());
        viewHolder.deskripsi.setText(data.get(i).getDeskripsi());
        if (data.get(i).getHeading().equals("Penarikan Saldo")){
            viewHolder.saldo.setText(" - "+String.valueOf(data.get(i).getSaldo()));
        }else{
            viewHolder.saldo.setText(String.valueOf(data.get(i).getSaldo()));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView heading,deskripsi,saldo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = (TextView)itemView.findViewById(R.id.txtHeading);
            deskripsi = (TextView)itemView.findViewById(R.id.txtDeskripsi);
            saldo = (TextView)itemView.findViewById(R.id.txtSaldo);
        }
    }
}
