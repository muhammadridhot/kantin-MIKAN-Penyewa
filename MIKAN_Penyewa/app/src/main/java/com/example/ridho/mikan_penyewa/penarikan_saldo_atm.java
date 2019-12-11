package com.example.ridho.mikan_penyewa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ridho.mikan_penyewa.Adapter.custom_spinner_adapter;
import com.example.ridho.mikan_penyewa.Model.laporan_saldo_dompet_digital_model;
import com.example.ridho.mikan_penyewa.Model.tarik_saldo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class penarikan_saldo_atm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] namaBank = {"Pilih Bank","MAYBANK"};
    int[] imgBank = {0,R.drawable.bank_maybank};
    String noRek, jenisBank;
    int nominal,saldo;
    Button btnTariKSaldo;
    EditText txtNoRek,txtNominal;
    private DatabaseReference refTarikSaldo,refLaporanDompetDigitalPenyewa,refPenyewa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penarikan_saldo_atm);

        refLaporanDompetDigitalPenyewa = FirebaseDatabase.getInstance().getReference("LaporanSaldoDompetDigital/Penyewa");
        refPenyewa = FirebaseDatabase.getInstance().getReference("Users/Penyewa");
        btnTariKSaldo = (Button)findViewById(R.id.btnTarikSaldoATM);
        txtNoRek = (EditText)findViewById(R.id.txtNoRek);
        txtNominal = (EditText)findViewById(R.id.txtNominal);
//        noRek = txtNoRek.getText().toString().trim();
//        nominal = Integer.parseInt(txtNominal.getText().toString());
        Spinner spin = (Spinner)findViewById(R.id.atmSpinner);
        spin.setOnItemSelectedListener(this);
        custom_spinner_adapter adapter = new custom_spinner_adapter(this,imgBank,namaBank);
        spin.setAdapter(adapter);

        Intent intent = getIntent();
        saldo = intent.getIntExtra("JLHSALDO",0);
        refTarikSaldo = FirebaseDatabase.getInstance().getReference("TarikSaldo");
        btnTariKSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarik_saldo();
            }
        });
    }

    private void tarik_saldo() {
        if (!jenisBank.equals("Pilih Bank")){
            if (TextUtils.isEmpty(txtNoRek.getText().toString()) || TextUtils.isEmpty(txtNominal.getText().toString())){
                txtNoRek.setError("Isi No Rekening");
                txtNominal.setError("Isi Nominal");
            }else{
                if (Integer.parseInt(txtNominal.getText().toString()) > saldo){
                    Toast.makeText(this,"Nominal yang anda masukkan melebihi saldo anda",Toast.LENGTH_LONG).show();
                }else {
                    if (Integer.parseInt(txtNominal.getText().toString()) > 1){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String idPenyewa = user.getUid();
                        String idPenarikan = refTarikSaldo.push().getKey();
                        String email = user.getEmail();
                        Date date = new Date();
                        tarik_saldo tarikSaldo = new tarik_saldo(idPenarikan,email,txtNoRek.getText().toString(), jenisBank,DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM, Locale.ENGLISH).format(date),"Menunggu Konfirmasi",Integer.parseInt(txtNominal.getText().toString()));
                        refTarikSaldo.child(idPenyewa).child(idPenarikan).setValue(tarikSaldo);

                        laporan_saldo_dompet_digital_model laporanSaldoDompetDigitalModel = new laporan_saldo_dompet_digital_model("Penarikan Saldo","Menunggu Konfirmasi", idPenarikan,idPenyewa,Integer.parseInt(txtNominal.getText().toString()));
                        refLaporanDompetDigitalPenyewa.child(idPenyewa).child(idPenarikan).setValue(laporanSaldoDompetDigitalModel);

                        refPenyewa.child(email.substring(0,email.indexOf("."))).child("saldo").setValue(saldo-Integer.parseInt(txtNominal.getText().toString()));
                        Intent intent = new Intent(this,home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Toast.makeText(this,"Isi Nominal lebih dari 0",Toast.LENGTH_LONG).show();
                    }
                }

            }
        }else {
            Toast.makeText(this,"Pilih Bank",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        jenisBank = namaBank[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
