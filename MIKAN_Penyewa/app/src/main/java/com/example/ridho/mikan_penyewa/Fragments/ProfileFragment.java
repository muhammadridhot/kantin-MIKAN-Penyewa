package com.example.ridho.mikan_penyewa.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ridho.mikan_penyewa.Model.Penyewa;
import com.example.ridho.mikan_penyewa.R;
import com.example.ridho.mikan_penyewa.laporan_saldo_dompet_digital;
import com.example.ridho.mikan_penyewa.login;
import com.example.ridho.mikan_penyewa.penarikan_saldo;
import com.example.ridho.mikan_penyewa.update_profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    Button btnUpdateProfile,btnHistory,btnKeluar,btnTarikSaldo;
    CircleImageView imageView;
    FirebaseUser user;
    TextView nama,saldo,hasilstatus;
    private DatabaseReference refPenyewa;
    Switch aSwitch;
    int saldoAnda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnTarikSaldo = (Button)view.findViewById(R.id.btnTarikSaldo);
        btnUpdateProfile = (Button)view.findViewById(R.id.btnUpdateProfile);
        btnHistory = (Button)view.findViewById(R.id.btnHistoryTopUp);
        btnKeluar = (Button)view.findViewById(R.id.btnKeluar);
        imageView = (CircleImageView)view.findViewById(R.id.profile_image);
        nama = (TextView)view.findViewById(R.id.username);
        saldo = (TextView)view.findViewById(R.id.txtsaldouser);
        hasilstatus = (TextView)view.findViewById(R.id.hasilstatus);
        aSwitch = (Switch)view.findViewById(R.id.setstatus);
        hasilstatus.setText("Tutup");
        hasilstatus.setTextColor(Color.parseColor("#fa0000"));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (aSwitch.isChecked()){
                    hasilstatus.setText("Buka");
                    hasilstatus.setTextColor(Color.parseColor("#4caf50"));
                }else {
                    hasilstatus.setText("Tutup");
                    hasilstatus.setTextColor(Color.parseColor("#fa0000"));
                }
            }
        });

        refPenyewa = FirebaseDatabase.getInstance().getReference("Users/Penyewa");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            if (user.getPhotoUrl() != null && user.getDisplayName() != null){
                Picasso.get().load(user.getPhotoUrl()).into(imageView);
                nama.setText(user.getDisplayName());
            }else{
                nama.setText("MIKAN");
                Picasso.get().load(R.drawable.logo_mikan).into(imageView);
            }
            refPenyewa.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Penyewa penyewa = snapshot.getValue(Penyewa.class);
                        if (penyewa.getUserid().equals(user.getUid())){
                            saldo.setText("Rp. "+String.valueOf(penyewa.getSaldo()));
                            saldoAnda = penyewa.getSaldo();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        btnTarikSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), penarikan_saldo.class));
                Intent intent = new Intent(getActivity(),penarikan_saldo.class);
                intent.putExtra("SALDO",saldoAnda);
                startActivity(intent);
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),update_profile.class));
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), laporan_saldo_dompet_digital.class));
            }
        });
        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity(),R.style.AlertDialogDelete);
                myAlert.setTitle("Keluar");
                myAlert.setMessage("Apakah kamu yakin ingin keluar ?");
                myAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        click_keluar();
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

        return view;
    }

    private void click_keluar() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), login.class));
        getActivity().finish();
    }


}
