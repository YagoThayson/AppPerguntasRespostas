package com.example.appperguntasrespostas;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentAjuda2 extends Fragment {

    private Button botao_ok;

    public FragmentAjuda2() {
        // Required empty public constructor
    }
    public static FragmentAjuda2 newInstance() {
        FragmentAjuda2 fragment = new FragmentAjuda2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajuda2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        botao_ok.findViewById(R.id.botao_generico);

        botao_ok.setOnClickListener(v -> {

            getParentFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });


    }
}