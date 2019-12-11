package com.example.ridho.mikan_penyewa.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ridho.mikan_penyewa.Adapter.menu_adapter;
import com.example.ridho.mikan_penyewa.Model.menu;
import com.example.ridho.mikan_penyewa.R;
import com.example.ridho.mikan_penyewa.tambah_menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MenuFragment extends Fragment {


    private DatabaseReference refMenu;
    FloatingActionButton floatAddMenu;
    RecyclerView recyclerView;
    ArrayList<menu> lstDataMenu;
    menu_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.lsttampilmenu);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        refMenu = FirebaseDatabase.getInstance().getReference("Menu");
//        if (lstDataMenu != null){
//            adapter = new menu_adapter(getActivity(),lstDataMenu);
//            recyclerView.setAdapter(adapter);
//
//        }

        lstDataMenu = new ArrayList<>();
        floatAddMenu = (FloatingActionButton)view.findViewById(R.id.floatAddMenu);

        loadData();

        floatAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddMenu();
            }
        });
        return view;
    }

    private void clickAddMenu() {
        startActivity(new Intent(getActivity(), tambah_menu.class));
    }

    private void loadData() {
        refMenu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                lstDataMenu.clear();
                for (DataSnapshot snapshot:dataSnapshot.child(firebaseUser.getUid()).getChildren()){
                    menu data = snapshot.getValue(menu.class);
                    lstDataMenu.add(data);
                }
                adapter = new menu_adapter(getActivity(),lstDataMenu);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
