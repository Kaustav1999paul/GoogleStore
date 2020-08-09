package com.example.googlestore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Gmail extends Fragment {

    public Gmail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent i = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
        startActivity(i);
        return inflater.inflate(R.layout.fragment_gmail, container, false);
    }
}
