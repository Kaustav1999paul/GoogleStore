package com.example.googlestore;


import android.content.Intent;

import android.os.Bundle;


import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private TextView fullNameEditText;
    private CircleImageView profilePhoto;
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
//        +++++++++++++++++++++++++++ Assigning ID to variables ++++++++++++++++++++++++++++++++++++


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

