package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class Admin_LoginActivity extends AppCompatActivity {

    private TextView newDealer;
    private EditText Admin_phone , Admin_password;
    private CheckBox RememberMe;
    private Button Login;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

//  ++++++++++++++++++++++++++++++++++++  Assigning the value  +++++++++++++++++++++++++++++++++++++
        Admin_phone = (EditText) findViewById(R.id.phone);
        Admin_password = (EditText) findViewById(R.id.password);
        Login = (Button) findViewById(R.id.login);
        loadingBar = new ProgressDialog(this);
        RememberMe = (CheckBox) findViewById(R.id.rememberAdmin);
        newDealer = (TextView) findViewById(R.id.new_Dealer);
//  ++++++++++++++++++++++++++++++++++++  Assigning the value  +++++++++++++++++++++++++++++++++++++

        Paper.init(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAdmin();
            }
        });



        newDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_LoginActivity.this, Admin_Register_Activity.class);
                startActivity(intent);
            }
        });

    }

    private void LoginAdmin() {
        String phone = Admin_phone.getText().toString();
        String password = Admin_password.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Please Wait");
            loadingBar.setMessage("We are checking your details");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        if (RememberMe.isChecked()){
            Paper.book().write(Prevalent.AdminPhoneKey, phone);
            Paper.book().write(Prevalent.AdminPasswordKey, password);
        }
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
                            Toast.makeText(Admin_LoginActivity.this,  "Welcome Back Google Dealer", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Admin_LoginActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(Admin_LoginActivity.this,  "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(Admin_LoginActivity.this, phone + " doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
