package com.example.appperguntasrespostas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TempoEsgotado#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tempo_esgotado, container, false);
    }
}