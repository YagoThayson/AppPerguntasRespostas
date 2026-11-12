package com.example.appperguntasrespostas;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class FragmentOfline extends Fragment {
    private TextView txt_offline;
    private Button botao_voltar;
    private Button botao_jogar_offline;

    public FragmentOfline() {}

    public static FragmentOfline newInstance() {
        FragmentOfline fragment = new FragmentOfline();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ofline, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        txt_offline = view.findViewById(R.id.txt_off);

        botao_jogar_offline = view.findViewById(R.id.jogarOff);

        botao_voltar.setOnClickListener(v -> {

            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        });



    }
}