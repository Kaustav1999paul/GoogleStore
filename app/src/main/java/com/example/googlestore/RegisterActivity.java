package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputPhoneNo , InputPassword, InputName, InputAddress;
    private TextView AlreadyAUser;
    private ProgressDialog loadingBar;
    private static final int abc = 1;
    private Uri imageUri;
    private ImageView selectimage;
    private StorageReference storageReference;
    private String downloadUrl;


    private void Addphoto() {
        Intent gallerIntent = new Intent();
        gallerIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallerIntent.setType("image/");
        startActivityForResult(gallerIntent, abc);

        loadingBar.setTitle("Please Wait");
        loadingBar.setMessage("We are Setting your avatar");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == abc && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            selectimage.setImageURI(imageUri);

            final StorageReference filePath = storageReference.child(imageUri.getLastPathSegment()+ ".jpg");
            final UploadTask uploadTask = filePath.putFile(imageUri);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            downloadUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                downloadUrl = task.getResult().toString();
                                loadingBar.dismiss();
                            }else {
                                String m = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error: "+ m, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//  ++++++++++++++++++++++++++++++++++++  Assigning the value  +++++++++++++++++++++++++++++++++++++
        AlreadyAUser = (TextView) findViewById(R.id.alreadyAUser);
        CreateAccountButton = findViewById(R.id.RegisterButton);
        InputName = (EditText) findViewById(R.id.Name);
        InputPhoneNo = (EditText) findViewById(R.id.Phone);
        InputPassword = (EditText) findViewById(R.id.Password);
        loadingBar = new ProgressDialog(this);
        InputAddress = (EditText) findViewById(R.id.address);
        selectimage = findViewById(R.id.accountProf);

        storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages");
//  ++++++++++++++++++++++++++++++++++++  Assigning the value  +++++++++++++++++++++++++++++++++++++


        AlreadyAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Please Wait");
                loadingBar.setMessage("We are creating your account");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                final String name = InputName.getText().toString();
                final String phone = InputPhoneNo.getText().toString();
                final String password = InputPassword.getText().toString();
                final String address = InputAddress.getText().toString();
                Validate(name, phone , password, address, downloadUrl);
            }
        });
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addphoto();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    private void Validate(String name, String phone, String password, String address, String downloadUrl) {
        if (TextUtils.isEmpty(name)){
            Alerter.create(this)
                    .setTitle("Required")
                    .setText("Name field is empty")
                    .setIcon(R.drawable.call)
                    .setDuration(3000)
                    .enableProgress(true)
                    .setProgressColorRes(R.color.pro)
                    .setBackgroundColorRes(R.color.login)
                    .show();
            loadingBar.dismiss();
        }else if (TextUtils.isEmpty(phone)){
            Alerter.create(this)
                    .setTitle("Required")
                    .setText("Phone field is empty")
                    .setIcon(R.drawable.call)
                    .setDuration(3000)
                    .enableProgress(true)
                    .setProgressColorRes(R.color.pro)
                    .setBackgroundColorRes(R.color.login)
                    .show();
            loadingBar.dismiss();
        }else if (TextUtils.isEmpty(password)){
            Alerter.create(this)
                    .setTitle("Required")
                    .setText("Password field is empty")
                    .setIcon(R.drawable.lock)
                    .setDuration(3000)
                    .enableProgress(true)
                    .setProgressColorRes(R.color.pro)
                    .setBackgroundColorRes(R.color.login)
                    .show();
            loadingBar.dismiss();
        }else if (TextUtils.isEmpty(address)){
            Alerter.create(this)
                    .setTitle("Required")
                    .setText("Address field is empty")
                    .setIcon(R.drawable.ic_location)
                    .setDuration(3000)
                    .enableProgress(true)
                    .setProgressColorRes(R.color.pro)
                    .setBackgroundColorRes(R.color.login)
                    .show();
            loadingBar.dismiss();
        }else if (TextUtils.isEmpty(downloadUrl)){
            Alerter.create(this)
                    .setTitle("Required")
                    .setText("Avatar not selected")
                    .setIcon(R.drawable.ic_account)
                    .setDuration(3000)
                    .enableProgress(true)
                    .setProgressColorRes(R.color.pro)
                    .setBackgroundColorRes(R.color.login)
                    .show();
            loadingBar.dismiss();
        }else {
            ValidatePhoneNo(name, phone , password, address, downloadUrl);
        }
    }

    private void ValidatePhoneNo(final String name, final String phone, final String password, final String address, final String downloadUrl) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String , Object> userdataMap = new HashMap<>();
                    userdataMap.put("Phone" , phone);
                    userdataMap.put("Name" , name);
                    userdataMap.put("Password" , password);
                    userdataMap.put("Address" , address);
                    userdataMap.put("Photo", downloadUrl);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){
                              Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                              loadingBar.dismiss();
                              Intent intent = new Intent(RegisterActivity.this, UserLogin.class);
                              startActivity(intent);
                          }else {
                              loadingBar.dismiss();
                              Toast.makeText(RegisterActivity.this, "Error: Network Error in Database", Toast.LENGTH_SHORT).show();
                          }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
