package com.example.ridho.mikan_penyewa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class penarikan_saldo extends AppCompatActivity {

    CardView atm,kantin;
    int saldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penarikan_saldo);

        kantin = (CardView)findViewById(R.id.cardKantin);
        atm = (CardView)findViewById(R.id.cardATM);

        final Intent intent = getIntent();
        saldo = intent.getIntExtra("SALDO",0);
        kantin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(penarikan_saldo.this,"Masih dalam tahap pengembangan",Toast.LENGTH_SHORT).show();
            }
        });
        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(penarikan_saldo.this,penarikan_saldo_atm.class));
                Intent intent1 = new Intent(penarikan_saldo.this,penarikan_saldo_atm.class);
                intent1.putExtra("JLHSALDO",saldo);
                startActivity(intent1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
