package com.example.appperguntasrespostas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentSplash extends Fragment {

    private ImageView logo_principal;

    public FragmentSplash() {}
    public static FragmentSplash newInstance(String param1, String param2) {
        FragmentSplash fragment = new FragmentSplash();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        logo_principal.findViewById(R.id.logo_principal);
    }
}