package com.example.googlestore;


import android.content.Intent;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import Prevalent.Prevalent;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class AccountFragment extends Fragment {

    public AccountFragment() {
        // Required empty public constructor
    }

    private TextView userPhoneEditText, addressEditText, name, email;
    private FloatingActionButton Logout;
    private TextView fullNameEditText, level;
    private ImageView profilePhoto, photo;
    private DatabaseReference Count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

//        +++++++++++++++++++++++++++ Assigning ID to variables ++++++++++++++++++++++++++++++++++++
        fullNameEditText = view.findViewById(R.id.setting_full_name);
        userPhoneEditText = view.findViewById(R.id.setting_phone_no);
        addressEditText = view.findViewById(R.id.setting_address);
        Logout = view.findViewById(R.id.logout);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        profilePhoto = view.findViewById(R.id.accountProfile);
        photo = view.findViewById(R.id.photo);
        level = view.findViewById(R.id.level);
//        +++++++++++++++++++++++++++ Assigning ID to variables ++++++++++++++++++++++++++++++++++++

        Count = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());

        Count.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int count = (int) snapshot.getChildrenCount();
                if (count <= 8){
                    level.setText("Level 1");
                    photo.setBackgroundResource(R.drawable.levelone);
                }else if (count >= 9 && count <= 16){
                    level.setText("Level 2");
                    photo.setBackgroundResource(R.drawable.leveltwo);
                }else if (count >= 17 && count <= 25){
                    level.setText("Level 3");
                    photo.setBackgroundResource(R.drawable.levelthree);
                }else {
                    level.setText("Level 4");
                    photo.setBackgroundResource(R.drawable.levelfour);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fullNameEditText.setText(Prevalent.CurrentOnlineUser.getName());
        userPhoneEditText.setText(Prevalent.CurrentOnlineUser.getPhone());
        addressEditText.setText(Prevalent.CurrentOnlineUser.getAddress());

        name.setText(Prevalent.CurrentOnlineUser.getName());
        email.setText(Prevalent.CurrentOnlineUser.getPhone());
        Picasso.get().load(Prevalent.CurrentOnlineUser.getPhoto()).into(profilePhoto);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}

