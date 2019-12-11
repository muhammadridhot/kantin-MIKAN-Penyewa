package com.example.ridho.mikan_penyewa;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private  static int splashScreen = 1000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null) {
                    startActivity(new Intent(MainActivity.this, home.class));
                    finish();
                }else{
                    startActivity(new Intent(MainActivity.this, login.class));
                    finish();
                }
            }
        },splashScreen);
    }
}
