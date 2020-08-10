package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class UserLogin extends AppCompatActivity {

    private FloatingActionButton LoginButton;
    private EditText InputNumber , InputPassword;
    private TextView newUser ;
    private ProgressDialog loadingBar;
    private CheckBox RememberMe;
    private ImageView Admin;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);


        view = findViewById(android.R.id.content);
//  ++++++++++++++++++++++++++++++++++++  Assigning the value  +++++++++++++++++++++++++++++++++++++
        newUser = (TextView) findViewById(R.id.new_User);
        LoginButton = (FloatingActionButton) findViewById(R.id.LoginButton);
        InputNumber = (EditText) findViewById(R.id.Login_phoneNumber);
        InputPassword = (EditText) findViewById(R.id.Login_password);
        loadingBar = new ProgressDialog(this);
        RememberMe = (CheckBox) findViewById(R.id.RememberMe);
        Admin = (ImageView) findViewById(R.id.admin);
//  ++++++++++++++++++++++++++++++++++++  Assigning the value  +++++++++++++++++++++++++++++++++++++


        Paper.init(this);

        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogin.this, Admin_LoginActivity.class);
                startActivity(intent);
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogin.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Alerter.create(this)
                    .setTitle("Required")
                    .setText("Phone field is empty")
                    .setIcon(R.drawable.call)
                    .setDuration(3000)
                    .enableProgress(true)
                    .setProgressColorRes(R.color.pro)
                    .setBackgroundColorRes(R.color.login)
                    .show();
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
        }else{
//            loadingBar.setTitle("Please Wait");
            loadingBar.setMessage("We are checking your details");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {

        if (RememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }



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
//                            Toast.makeText(UserLogin.this,  "Welcome Back", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserLogin.this, HomeActivity.class);
                            startActivity(intent);
                        }else{
//                           +++++++++++++++ Wrong password ++++++++++++++
                            loadingBar.dismiss();
                            Alerter.create(UserLogin.this)
                                    .setTitle("Error")
                                    .setText("Password is incorrect")
                                    .setIcon(R.drawable.cross)
                                    .setDuration(3000)
                                    .enableProgress(true)
                                    .setProgressColorRes(R.color.pro)
                                    .setBackgroundColorRes(R.color.error)
                                    .show();
                        }
                    }
                }else {
                    loadingBar.dismiss();
                    Snackbar sb = Snackbar.make(view, phone+ " does not exist.", BaseTransientBottomBar.LENGTH_SHORT);
                    sb.show();

                    Alerter.create(UserLogin.this)
                            .setTitle(phone)
                            .setText("Phone number dose't exist!")
                            .setIcon(R.drawable.call)
                            .setDuration(3000)
                            .enableProgress(true)
                            .setProgressColorRes(R.color.pro)
                            .setBackgroundColorRes(R.color.error)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
