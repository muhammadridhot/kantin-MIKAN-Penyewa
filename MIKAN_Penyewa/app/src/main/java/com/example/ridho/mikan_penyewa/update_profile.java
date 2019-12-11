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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class update_profile extends AppCompatActivity {

    EditText nama;
    CircleImageView imageView;
    Button btnChoose,btnUpdate;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;
    private UploadTask uploadTask;
    private StorageReference storageReference;
    private DatabaseReference refPenyewa;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        nama = (EditText)findViewById(R.id.txtusernameupdate);
        imageView = (CircleImageView)findViewById(R.id.profile_image_update);
        btnChoose = (Button)findViewById(R.id.update_img);
        btnUpdate = (Button)findViewById(R.id.btnUpdate_profile);

        storageReference = FirebaseStorage.getInstance().getReference("Upload-Image");
        refPenyewa = FirebaseDatabase.getInstance().getReference("Users/Penyewa");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            nama.setText(user.getDisplayName());
            if (user.getPhotoUrl() == null) {
                Picasso.get().load(R.drawable.logo_mikan).into(imageView);
            } else {
                Picasso.get().load(user.getPhotoUrl()).into(imageView);
            }
        }


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_UpdateProfile();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_ChooseImage();
            }
        });
    }



    private void click_ChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null) {
            filepath = data.getData();
            Picasso.get().load(filepath).into(imageView);
        }
    }

    private void click_UpdateProfile() {
        if (filepath != null){
            final StorageReference refUrl = storageReference.child(user.getEmail());
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                ByteArrayOutputStream baos =  new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG,25,baos);
                byte[] data = baos.toByteArray();
                uploadTask = refUrl.putBytes(data);
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

                    return refUrl.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
//                        if (downloadUri != null){
//                            String down = downloadUri.toString();
//                        }

                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nama.getText().toString())
                                .setPhotoUri(Uri.parse(downloadUri.toString()))
                                .build();

                        user.updateProfile(profileChangeRequest);
                        refPenyewa.child(user.getEmail().substring(0, user.getEmail().indexOf("."))).child("nama").setValue(nama.getText().toString());
                        refPenyewa.child(user.getEmail().substring(0, user.getEmail().indexOf("."))).child("image").setValue(downloadUri.toString());
                        pd.dismiss();
                        Toast.makeText(update_profile.this, "Update Profile Success", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(update_profile.this,"Silahkan pilih image untuk update",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
