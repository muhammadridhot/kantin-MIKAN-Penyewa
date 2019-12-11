package com.example.ridho.mikan_penyewa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ridho.mikan_penyewa.Model.Penyewa;
import com.example.ridho.mikan_penyewa.Model.stan_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class register extends AppCompatActivity implements AdapterView.OnItemSelectedListener{



    Button btnPengajuan;
    private DatabaseReference refPenyewa,refStan;
    private FirebaseAuth authPenyewa;
    EditText txtEmail,txtNama,txtPass;
    RadioGroup rdbGroup;
    RadioButton rdbButton;
    String stan;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ComboBox Memakai Spinner
        final Spinner spinner = (Spinner)findViewById(R.id.no_stan);

        data = new ArrayList<>();
        spinner.setOnItemSelectedListener(this);
        refStan = FirebaseDatabase.getInstance().getReference("Stan");

        refPenyewa = FirebaseDatabase.getInstance().getReference("Users/Penyewa");
        authPenyewa = FirebaseAuth.getInstance();

        rdbGroup = (RadioGroup)findViewById(R.id.rdbGroup);

        rdbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final int rdbId = rdbGroup.getCheckedRadioButtonId();
                rdbButton = findViewById(rdbId);
                if (rdbButton.getText().equals("Kantin A")){
                    Toast.makeText(register.this,"Tidak dapat melakukan pengajuan sewa stan pada kantin A untuk saat ini",Toast.LENGTH_LONG).show();
                    spinner.setEnabled(false);
                    txtEmail.setEnabled(false);
                    txtNama.setEnabled(false);
                    txtPass.setEnabled(false);
                    btnPengajuan.setEnabled(false);
                    spinner.setAdapter(null);
                }else if(rdbButton.getText().equals("Kantin B")){
                    spinner.setEnabled(true);
                    txtEmail.setEnabled(true);
                    txtNama.setEnabled(true);
                    txtPass.setEnabled(true);
                    btnPengajuan.setEnabled(true);
                    refStan.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            data.clear();
                            for (DataSnapshot snapshot:dataSnapshot.child("KantinB").getChildren()){
                                stan_model stanModel = snapshot.getValue(stan_model.class);
                                if (!stanModel.getStatus()){
                                    data.add(stanModel.getNama());
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(register.this,android.R.layout.simple_spinner_item,data);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        btnPengajuan = (Button)findViewById(R.id.btnRegister);
        btnPengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_pengajuan();
            }
        });

        txtEmail = (EditText)findViewById(R.id.txtEmailRegister);
        txtNama = (EditText)findViewById(R.id.txtnamaRegister);
        txtPass = (EditText)findViewById(R.id.txtPassRegister);
    }
    //ComboBox
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        stan = parent.getItemAtPosition(position).toString();
        Log.d("Pilih : ",stan);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void click_pengajuan() {

        final int rdbId = rdbGroup.getCheckedRadioButtonId();
        rdbButton = findViewById(rdbId);
        Log.d("Checked :", rdbButton.getText().toString());

        final String email = txtEmail.getText().toString().trim();
        final String pass = txtPass.getText().toString();
        final String nama = txtNama.getText().toString();
        final String image = "https://firebasestorage.googleapis.com/v0/b/kantin-online-mikroskil-9ee2c.appspot.com/o/logo_mikan.jpg?alt=media&token=2a1c3c85-1972-4d3a-8902-61a7575f4203";

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(nama)){
            txtEmail.setError("Required");
            txtPass.setError("Required");
            txtNama.setError("Required");
        }else{
            final String emailReg = email.substring(0,(email.indexOf(".")));

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Please wait......");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();

            authPenyewa.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = authPenyewa.getCurrentUser();
                        if (firebaseUser != null){
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(Uri.parse(image))
                                    .setDisplayName(nama).build();

                            firebaseUser.updateProfile(profileChangeRequest);

                            String userId = firebaseUser.getUid();
                            Penyewa penyewa = new Penyewa(userId,email,pass,nama,rdbButton.getText().toString(),stan,image);
                            refPenyewa.child(emailReg).setValue(penyewa).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(rdbButton.getText().equals("Kantin B")){
                                        refStan.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot:dataSnapshot.child("KantinB").getChildren()){
                                                    if (snapshot.child("nama").getValue().equals(stan)){
                                                        HashMap<String,Object> test = new HashMap<>();
                                                        test.put("status",true);
                                                        refStan.child("KantinB").child(snapshot.getKey()).updateChildren(test);
                                                        Log.d("StatusAnda",snapshot.getKey() + stan);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    Toast.makeText(register.this,"Register berhasil",Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(register.this, login.class));
                                    finish();
                                    pd.dismiss();
                                }
                            });
                        }
                    }else{
                        Toast.makeText(register.this,"Register gagal,email sudah terdaftar",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });
        }
    }

}
