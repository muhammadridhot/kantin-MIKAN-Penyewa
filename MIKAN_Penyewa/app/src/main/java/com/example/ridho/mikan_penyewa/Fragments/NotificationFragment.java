package com.example.ridho.mikan_penyewa.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ridho.mikan_penyewa.Adapter.notifikasi_adapter;
import com.example.ridho.mikan_penyewa.Model.notifikasi_penyewa;
import com.example.ridho.mikan_penyewa.R;
import com.example.ridho.mikan_penyewa.detail_pesanan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class NotificationFragment extends Fragment {


    RecyclerView lstData;
    private DatabaseReference refNotifikasiPesanan;
    ArrayList<notifikasi_penyewa> lst;
    notifikasi_adapter adapter;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        linearLayout = (LinearLayout)view.findViewById(R.id.orderkosong);
        lstData = (RecyclerView)view.findViewById(R.id.lsttampilordernotif);
        lstData.setHasFixedSize(true);
        lstData.setLayoutManager(new LinearLayoutManager(getActivity()));
        lst = new ArrayList<>();

        refNotifikasiPesanan = FirebaseDatabase.getInstance().getReference("NotifikasiPenyewa");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        refNotifikasiPesanan.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lst.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    notifikasi_penyewa notifikasiPenyewa = snapshot.getValue(notifikasi_penyewa.class);
                    lst.add(notifikasiPenyewa);
                    Log.d("Test",notifikasiPenyewa.getIdOrder());
                }
                if (lst.size() > 0){
                    linearLayout.setVisibility(View.GONE);
                    lstData.setVisibility(View.VISIBLE);
                }
                adapter = new notifikasi_adapter(getActivity(), lst, new notifikasi_adapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        notifikasi_penyewa notifikasiPenyewa =lst.get(position);

                            Intent intent = new Intent(getActivity(), detail_pesanan.class);
                            intent.putExtra("IDORDER",notifikasiPenyewa.getIdOrder());
                            intent.putExtra("IDPENYEWA",notifikasiPenyewa.getIdPenyewa());
                            startActivity(intent);

                    }
                });
                lstData.setAdapter(adapter);
                Collections.reverse(lst);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
