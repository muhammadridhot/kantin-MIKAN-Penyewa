package com.example.ridho.mikan_penyewa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ridho.mikan_penyewa.Model.Penyewa;
import com.example.ridho.mikan_penyewa.Model.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class login extends AppCompatActivity {

    TextView txtRegister;
    EditText txtEmail,txtPass;
    Button btnLogin;
    private FirebaseAuth authLoginPenyewa;
    private DatabaseReference refPenyewa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtRegister = (TextView)findViewById(R.id.txtregister);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,register.class));
                finish();
            }
        });

        txtEmail = (EditText)findViewById(R.id.txtMail);
        txtPass = (EditText)findViewById(R.id.txtPass);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        authLoginPenyewa = FirebaseAuth.getInstance();
        refPenyewa = FirebaseDatabase.getInstance().getReference("Users/Penyewa");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_LoginPenyewa();
            }
        });
    }

    private void click_LoginPenyewa() {
        final String email = txtEmail.getText().toString().trim();
        final String pass = txtPass.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            txtEmail.setError("Required");
            txtPass.setError("Required");
        }else{
            final String emailReg = email.substring(0,(email.indexOf(".")));
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Please wait......");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();

            Query data = refPenyewa.orderByKey().equalTo(emailReg);

            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(emailReg).exists()){
//                        Penyewa penyewa = dataSnapshot.child(emailReg).getValue(Penyewa.class);
                        authLoginPenyewa.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                        @Override
                                        public void onSuccess(InstanceIdResult instanceIdResult) {
                                            String newToken = instanceIdResult.getToken();
                                            Log.d("newToken",newToken);
                                            updateToken(newToken);
                                        }
                                    });
                                    startActivity(new Intent(login.this, home.class));
                                    finish();
                                    pd.dismiss();
                                }else{
                                    Toast.makeText(login.this,"Password salah",Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(login.this,"Email tidak terdaftar",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void updateToken(String token) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token data = new Token(token, true);//True karena Partner
        reference.child(user.getUid()).setValue(data);
    }
}
