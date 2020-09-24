package com.example.googlestore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import Prevalent.Prevalent;

public class addressEdit extends AppCompatActivity {

    private EditText newAdd;
    private TextView name, ph;
    private ImageView avat;
    private FloatingActionButton save;
    private DatabaseReference Ref;
    private String c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);

        save = findViewById(R.id.save);
        avat = findViewById(R.id.accountProfile);
        name = findViewById(R.id.name);
        ph = findViewById(R.id.phno);
        newAdd = findViewById(R.id.new_address);

        Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        Picasso.get().load(Prevalent.CurrentOnlineUser.getPhoto()).into(avat);
        name.setText(Prevalent.CurrentOnlineUser.getName());
        ph.setText(Prevalent.CurrentOnlineUser.getPhone());
        newAdd.setText(Prevalent.CurrentOnlineUser.getAddress());

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String a = String.valueOf(newAdd.getText());
                Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Prevalent.CurrentOnlineUser.setAddress(a);
                        HashMap<String , Object> userdataMap = new HashMap<>();
                        userdataMap.put("Address" , a);
                        Ref.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(addressEdit.this, FinalCartActivity.class);
                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}
