package com.example.appperguntasrespostas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class TempoEsgotado extends Fragment {

    private TextView txtTempoEsgotado;
    private Button BotaoContinuar;

    public TempoEsgotado() {
        // Required empty public constructor
    }

    public static TempoEsgotado newInstance(String param1, String param2) {
        TempoEsgotado fragment = new TempoEsgotado();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tempo_esgotado, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTempoEsgotado = view.findViewById(R.id.txt_tempo_esgotado);
        BotaoContinuar = view.findViewById(R.id.botao_continuar);

        BotaoContinuar.setOnClickListener(v -> {
                ((PerguntasActivity) requireActivity())
                        .continuarAposTempoEsgotado();

        });
    }
}



