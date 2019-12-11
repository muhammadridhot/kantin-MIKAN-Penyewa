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
import android.util.Log;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class update_menu extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Button btnUpdate,btnChooseUpdate;
    EditText txtNamaUpdate, txtHargaUpdate;
    ImageView imgViewUpdate;
    String nama,image,idMenu;
    int harga;
    private Uri filepath;
    private UploadTask uploadTask;
    private DatabaseReference refMenu;
    private StorageReference storageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu);

        refMenu = FirebaseDatabase.getInstance().getReference("Menu");
        storageMenu = FirebaseStorage.getInstance().getReference("MenuImage");

        Intent intent = getIntent();
        nama = intent.getStringExtra("NAMA");
        harga = intent.getIntExtra("HARGA",0);
        image = intent.getStringExtra("URLIMAGE");
        idMenu = intent.getStringExtra("IDMENU");

        txtNamaUpdate = (EditText)findViewById(R.id.txtNamaMenuUpdate);
        txtHargaUpdate = (EditText)findViewById(R.id.txtHargaMenuUpdate);
        imgViewUpdate = (ImageView)findViewById(R.id.imgMenuUpdate);
        btnChooseUpdate = (Button)findViewById(R.id.btnChooseImgMenuUpdate);
        btnUpdate = (Button)findViewById(R.id.btnupdateMenu);


        txtNamaUpdate.setText(nama);
        txtHargaUpdate.setText(String.valueOf(harga));
        Picasso.get().load(image).into(imgViewUpdate);

        btnChooseUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_Choose();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_update();
            }
        });
    }

    private void click_update() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String key = user.getUid();
        final String nama = txtNamaUpdate.getText().toString();
        final String harga = txtHargaUpdate.getText().toString().trim();
        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(harga)){
            txtNamaUpdate.setError("Required");
            txtHargaUpdate.setError("Required");
        }else{
            if (filepath != null ){
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
                                refMenu.child(key).child(idMenu).child("nama").setValue(txtNamaUpdate.getText().toString());
                                refMenu.child(key).child(idMenu).child("harga").setValue(Integer.parseInt(txtHargaUpdate.getText().toString()));
                            if(!image.equals(downloadUri.toString())){
                                refMenu.child(key).child(idMenu).child("image").setValue(downloadUri.toString());
                            }

                            pd.dismiss();
                            Toast.makeText(update_menu.this,"Menu berhasil diupdate",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }else {
                    refMenu.child(key).child(idMenu).child("nama").setValue(txtNamaUpdate.getText().toString());
                    refMenu.child(key).child(idMenu).child("harga").setValue(Integer.parseInt(txtHargaUpdate.getText().toString()));

                Toast.makeText(update_menu.this,"Menu berhasil diupdate",Toast.LENGTH_SHORT).show();
                finish();
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
            Picasso.get().load(filepath).into(imgViewUpdate);
        }
    }
}
