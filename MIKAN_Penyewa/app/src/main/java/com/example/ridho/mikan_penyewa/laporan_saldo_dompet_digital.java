package com.example.ridho.mikan_penyewa;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ridho.mikan_penyewa.Adapter.laporan_dompet_digital_adapter;
import com.example.ridho.mikan_penyewa.Model.laporan_saldo_dompet_digital_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class laporan_saldo_dompet_digital extends AppCompatActivity {

    private DatabaseReference refLaporan;
    laporan_dompet_digital_adapter adapter;
    RecyclerView recyclerView;
    ArrayList<laporan_saldo_dompet_digital_model> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_saldo_dompet_digital);

        data = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.lstLaporanDompetDigital);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refLaporan = FirebaseDatabase.getInstance().getReference("LaporanSaldoDompetDigital/Penyewa");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String idPenyewa =user.getUid();
        refLaporan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot:dataSnapshot.child(idPenyewa).getChildren()){
                    laporan_saldo_dompet_digital_model laporanSaldoDompetDigitalModel = snapshot.getValue(laporan_saldo_dompet_digital_model.class);
                    data.add(laporanSaldoDompetDigitalModel);
                }
                adapter = new laporan_dompet_digital_adapter(laporan_saldo_dompet_digital.this,data);
                recyclerView.setAdapter(adapter);
                Collections.reverse(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
