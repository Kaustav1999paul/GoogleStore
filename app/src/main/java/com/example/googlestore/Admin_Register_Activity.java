package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Admin_Register_Activity extends AppCompatActivity {

    private TextView AlreadyADealer;
    private EditText AdminPhone;
    private EditText AdminName;
    private EditText AdminPassword;
    private Button CreateDealerAccount;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__register_);
//  ++++++++++++++++++++++++++++++++++++  Assigning the value  +++++++++++++++++++++++++++++++++++++
       AlreadyADealer = (TextView) findViewById(R.id.AlreadyADealer);
        AdminName = (EditText) findViewById(R.id.Name);
        AdminPhone = (EditText) findViewById(R.id.PhoneNo);
        AdminPassword = (EditText) findViewById(R.id.Password);
        loadingBar = new ProgressDialog(this);
        CreateDealerAccount = (Button) findViewById(R.id.CreateDealerAccount);
//  ++++++++++++++++++++++++++++++++++++  Assigning the value  +++++++++++++++++++++++++++++++++++++

        AlreadyADealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Register_Activity.this, Admin_LoginActivity.class);
                startActivity(intent);
            }
        });
        CreateDealerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDealerAccount();
            }
        });
    }

    private void createDealerAccount() {
        String name = AdminName.getText().toString();
        String phone = AdminPhone.getText().toString();
        String password = AdminPassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
        }else {

            loadingBar.setTitle("Please Wait");
            loadingBar.setMessage("We are creating your account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNo(name, phone , password);
        }
    }

    private void ValidatePhoneNo(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Admin").child(phone).exists())){
                    HashMap<String , Object> userdataMap = new HashMap<>();
                    userdataMap.put("Phone" , phone);
                    userdataMap.put("Name" , name);
                    userdataMap.put("Password" , password);

                    RootRef.child("Admin").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                loadingBar.dismiss();
                                Toast.makeText(Admin_Register_Activity.this, "Dealer account created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Admin_Register_Activity.this, Admin_LoginActivity.class);
                                startActivity(intent);
                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(Admin_Register_Activity.this, "Error: Network Error in Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    loadingBar.dismiss();
                    Toast.makeText(Admin_Register_Activity.this, "This " + phone + " already exists" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
