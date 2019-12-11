package com.example.ridho.mikan_penyewa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ridho.mikan_penyewa.Model.menu;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class tambah_menu extends AppCompatActivity {


    private DatabaseReference refMenu;
    private StorageReference storageMenu;
    Button btnAdd,btnChoose;
    EditText txtNama,txtHarga;
    ImageView  imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;
    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        txtNama = (EditText)findViewById(R.id.txtNamaMenu);
        txtHarga = (EditText)findViewById(R.id.txtHargaMenu);
        imageView = (ImageView)findViewById(R.id.imgMenu);
        btnAdd = (Button)findViewById(R.id.btnAddMenu);
        btnChoose = (Button)findViewById(R.id.btnChooseImgMenu);

        refMenu = FirebaseDatabase.getInstance().getReference("Menu");
        storageMenu = FirebaseStorage.getInstance().getReference("MenuImage");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_Choose();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_addMenu();
            }
        });
    }

    private void click_addMenu() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String key = user.getUid();
        final String nama = txtNama.getText().toString();
        final String harga = txtHarga.getText().toString().trim();
        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(harga)){
            txtNama.setError("Required");
            txtHarga.setError("Required");
        }else{
            if (filepath != null){
                final StorageReference storageReference = storageMenu.child(nama);

                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG,25,baos);
                    byte[] data = baos.toByteArray();
                    uploadTask = storageReference.putBytes(data);
                }catch (IOException e){
                    e.printStackTrace();
                }
                final ProgressDialog pd = new ProgressDialog(this);
                pd.setMessage("Please wait...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            String idMenu = refMenu.push().getKey();
                            menu data = new menu(idMenu,nama,downloadUri.toString(),Integer.parseInt(harga));
                            refMenu.child(key).child(idMenu).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(tambah_menu.this,"Menu berhasil ditambahkan",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }
                });
            }else{
                Toast.makeText(this,"No File selected",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void click_Choose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null){
            filepath = data.getData();
            Picasso.get().load(filepath).into(imageView);
        }
    }
}
