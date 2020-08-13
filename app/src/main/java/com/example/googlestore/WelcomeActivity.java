package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;
import maes.tech.intentanim.CustomIntent;

public class WelcomeActivity extends AppCompatActivity {

    private FloatingActionButton nextPage;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);
//        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        String AdminPhoneKey = Paper.book().read(Prevalent.AdminPhoneKey);
        String AdminPasswordKey = Paper.book().read(Prevalent.AdminPasswordKey);
//        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        nextPage = (FloatingActionButton) findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, UserLogin.class);
                startActivity(intent);
                CustomIntent.customType(WelcomeActivity.this, "left-to-right");
            }
        });

        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
//                loadingBar.setTitle("Please Wait");
                loadingBar.setMessage("We are directly logging you in");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                AllowAccess(UserPhoneKey ,UserPasswordKey);

            }
        }
        if (AdminPhoneKey != "" && AdminPasswordKey != ""){
            if (!TextUtils.isEmpty(AdminPhoneKey) && !TextUtils.isEmpty(AdminPasswordKey)){
//                loadingBar.setTitle("Please Wait");
                loadingBar.setMessage("We are directly logging you in");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                AllowAccessForAdmin(AdminPhoneKey ,AdminPasswordKey);

            }
        }
    }

    private void AllowAccessForAdmin(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Admin").child(phone).exists()){
                    Users usersData = dataSnapshot.child("Admin").child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            loadingBar.dismiss();
                            Toast.makeText(WelcomeActivity.this,  "Welcome Back Google Dealer", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(WelcomeActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(WelcomeActivity.this,  "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(WelcomeActivity.this, phone + " doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()){
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            loadingBar.dismiss();
                            Prevalent.CurrentOnlineUser = usersData;
                            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(WelcomeActivity.this,  "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(WelcomeActivity.this, phone + " doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
