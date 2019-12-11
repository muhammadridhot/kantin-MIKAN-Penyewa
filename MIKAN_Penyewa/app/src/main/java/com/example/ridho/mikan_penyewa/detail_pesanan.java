package com.example.ridho.mikan_penyewa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ridho.mikan_penyewa.Adapter.cart_adapter;
import com.example.ridho.mikan_penyewa.Model.Penyewa;
import com.example.ridho.mikan_penyewa.Model.laporan_saldo_dompet_digital_model;
import com.example.ridho.mikan_penyewa.Model.menu;
import com.example.ridho.mikan_penyewa.Model.notifikasi_penyewa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class detail_pesanan extends AppCompatActivity {

    private DatabaseReference refNotifPenyewa,refRiwayatPesanan, refUsersMikroskil, refUsersNonCivitas,refPenyewa,refLaporanDompetDigitalPelanggan,refLaporanDompetDigitalPenyewa;
    String nama,meja,image,idorder,idpenyewa,idPelanggan,jenisUsers,idMiso,email,updateTotalSaldo, metodePembayaran,status,statusNotifikasi,emailPenyewa;
    int total,saldoPenambahan;
    TextView txtNama,txtMeja,txtTotal;
    CircleImageView imageView;
    cart_adapter adapter;
    RecyclerView recyclerView;
    Button btnTolak,btnTerima,btnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        Intent intent = getIntent();
        idorder = intent.getStringExtra("IDORDER");
        idpenyewa = intent.getStringExtra("IDPENYEWA");

        imageView = (CircleImageView)findViewById(R.id.imgProfile);
        txtNama = (TextView)findViewById(R.id.txtNama);
        txtMeja = (TextView)findViewById(R.id.detail_meja);
        txtTotal = (TextView)findViewById(R.id.txtDetailTotal);
        recyclerView = (RecyclerView)findViewById(R.id.datamenu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnTolak = (Button)findViewById(R.id.btnTolakPesanan);
        btnTerima = (Button)findViewById(R.id.btnTerimaPesanan);
        btnStatus = (Button)findViewById(R.id.btnStatus);

        refNotifPenyewa = FirebaseDatabase.getInstance().getReference("NotifikasiPenyewa");
        refRiwayatPesanan = FirebaseDatabase.getInstance().getReference("Riwayat_Pesanan");
        refUsersMikroskil = FirebaseDatabase.getInstance().getReference("Users/Mikroskil");
        refUsersNonCivitas = FirebaseDatabase.getInstance().getReference("Users/NonCivitas");
        refPenyewa = FirebaseDatabase.getInstance().getReference("Users/Penyewa");
        refLaporanDompetDigitalPelanggan = FirebaseDatabase.getInstance().getReference("LaporanSaldoDompetDigital/Pelanggan");
        refLaporanDompetDigitalPenyewa = FirebaseDatabase.getInstance().getReference("LaporanSaldoDompetDigital/Penyewa");

        refNotifPenyewa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.child(idpenyewa).getChildren()){
                    notifikasi_penyewa notifikasiPenyewa = snapshot.getValue(notifikasi_penyewa.class);

                    if (notifikasiPenyewa.getIdOrder().equals(idorder)){
                        nama = notifikasiPenyewa.getNamaPemesan();
                        meja = notifikasiPenyewa.getMeja_makan();
                        total = notifikasiPenyewa.getTotal();
                        image = notifikasiPenyewa.getImage();
                        idPelanggan = notifikasiPenyewa.getIdPelanggan();
                        metodePembayaran = notifikasiPenyewa.getMetodePembayaran();
                        statusNotifikasi = notifikasiPenyewa.getStatus();
                        adapter = new cart_adapter(detail_pesanan.this,notifikasiPenyewa.getMenu());
                        recyclerView.setAdapter(adapter);
                    }
                }
                getSupportActionBar().setTitle(nama);

                txtNama.setText(nama);
                txtMeja.setText(meja);
                btnStatus.setText(metodePembayaran);
                txtTotal.setText(String.valueOf(total));
                Picasso.get().load(image).into(imageView);
                if (statusNotifikasi.equals("Diterima")){
                    btnTerima.setEnabled(false);
                    btnTolak.setEnabled(false);
                    btnTerima.setVisibility(View.GONE);
                    btnTolak.setVisibility(View.GONE);
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layoutstatus);
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        refUsersMikroskil.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("userid").getValue().equals(idPelanggan)){
                            jenisUsers = "Mikroskil";
                            idMiso = String.valueOf(snapshot.child("idMiso").getValue());
                            updateTotalSaldo = String.valueOf(snapshot.child("saldo").getValue());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        refUsersNonCivitas.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.child("userid").getValue().equals(idPelanggan)){
                            jenisUsers = "NonCivitas";
                            email = String.valueOf(snapshot.child("email").getValue());
                            updateTotalSaldo = String.valueOf(snapshot.child("saldo").getValue());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = user.getUid();

        refPenyewa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Penyewa penyewa = snapshot.getValue(Penyewa.class);
                    Log.d("DataPenyewa",penyewa.getUserid());
                    if (penyewa.getUserid().equals(userid)){
                        emailPenyewa = penyewa.getEmail();
                        saldoPenambahan = penyewa.getSaldo();
                        status = "True";
//                            Log.d("StatusANda",status);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTerima();
                finish();
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTolak();
                finish();
            }
        });
    }

    private void clickTerima() {
        if (metodePembayaran.equals("Dompet Digital")){
            if (status.equals("True")){
                refPenyewa.child(emailPenyewa.substring(0,emailPenyewa.indexOf("."))).child("saldo").setValue(saldoPenambahan+total);
                refRiwayatPesanan.child(idPelanggan).child(idorder).child("status").setValue("Diterima");
                refNotifPenyewa.child(idpenyewa).child(idorder).child("status").setValue("Diterima");
                laporan_saldo_dompet_digital_model laporanSaldoDompetDigitalModel = new laporan_saldo_dompet_digital_model("Pesanan",nama,idorder,idPelanggan,total);
                refLaporanDompetDigitalPenyewa.child(idpenyewa).child(idorder).setValue(laporanSaldoDompetDigitalModel);
            }
        }else if (metodePembayaran.equals("Tunai")){
            if (status.equals("True")){
                refRiwayatPesanan.child(idPelanggan).child(idorder).child("status").setValue("Diterima");
                refNotifPenyewa.child(idpenyewa).child(idorder).child("status").setValue("Diterima");
            }
        }
    }

    private void clickTolak() {
        refRiwayatPesanan.child(idPelanggan).child(idorder).child("status").setValue("Ditolak");
        refNotifPenyewa.child(idpenyewa).child(idorder).child("status").setValue("Ditolak");
        Log.d("InfoAnda",idPelanggan+"__"+idorder);
        if (jenisUsers.equals("Mikroskil")){
            if (metodePembayaran.equals("Dompet Digital")){
                refUsersMikroskil.child(idMiso).child("saldo").setValue(Integer.parseInt(updateTotalSaldo)+total);
                refLaporanDompetDigitalPelanggan.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.child(idPelanggan).getChildren()){
                            if (snapshot.child("idLaporan").getValue().equals(idorder)){
                                refLaporanDompetDigitalPelanggan.child(idPelanggan).child(String.valueOf(snapshot.child("idLaporan").getValue())).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }else if(jenisUsers.equals("NonCivitas")){
            if (metodePembayaran.equals("Dompet Digital")){
                refUsersNonCivitas.child(email.substring(0,email.indexOf("."))).child("saldo").setValue(Integer.parseInt(updateTotalSaldo)+total);
                refLaporanDompetDigitalPelanggan.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.child(idPelanggan).getChildren()){
                            if (snapshot.child("idLaporan").getValue().equals(idorder)){
                                refLaporanDompetDigitalPelanggan.child(idPelanggan).child(String.valueOf(snapshot.child("idLaporan").getValue())).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
