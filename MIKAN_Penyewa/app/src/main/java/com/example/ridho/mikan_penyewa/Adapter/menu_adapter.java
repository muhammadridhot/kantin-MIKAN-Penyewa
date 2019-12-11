package com.example.ridho.mikan_penyewa.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ridho.mikan_penyewa.Model.menu;
import com.example.ridho.mikan_penyewa.R;
import com.example.ridho.mikan_penyewa.update_menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class menu_adapter extends RecyclerView.Adapter<menu_adapter.ViewHolder> {

    private Context ct;
    private ArrayList<menu> data;

    public menu_adapter(Context ct, ArrayList<menu> data) {
        this.ct = ct;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(ct).inflate(R.layout.tampilan_data_menu,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.nama.setText(data.get(i).getNama());
        viewHolder.harga.setText(String.valueOf(data.get(i).getHarga()));
        Picasso.get().load(data.get(i).getImage()).into(viewHolder.img);

        final String nama = data.get(i).getNama();
        final int harga = data.get(i).getHarga();
        final String urlImage = data.get(i).getImage();
        final String idMenu = data.get(i).getIdMenu();
        viewHolder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlert = new AlertDialog.Builder(ct,R.style.AlertDialogDelete);
                myAlert.setTitle("Delete");
                myAlert.setMessage("Apakah kamu yakin ingin menghapus menu ?");
                myAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Menu");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String idStan = user.getUid();
                        reference.child(idStan).child(idMenu).removeValue();
                    }
                });
                myAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                myAlert.show().getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct, update_menu.class);
                intent.putExtra("NAMA",nama);
                intent.putExtra("HARGA",harga);
                intent.putExtra("URLIMAGE",urlImage);
                intent.putExtra("IDMENU",idMenu);
                ct.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama,harga;
        ImageView img;
        Button btnHapus,btnUpdate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.txtNamaDataMenu);
            harga = (TextView)itemView.findViewById(R.id.txtHargaDataMenu);
            btnHapus = (Button)itemView.findViewById(R.id.btnHapusMenu);
            btnUpdate = (Button)itemView.findViewById(R.id.btnUpdateMenu);
            img = (ImageView)itemView.findViewById(R.id.imgDataMenu);
        }
    }
}
