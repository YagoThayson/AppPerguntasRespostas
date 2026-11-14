package com.example.appperguntasrespostas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentGenerico extends Fragment {
    private TextView txtGenerico;
    private Button botaoGenerico;



    public FragmentGenerico() {}

    public static FragmentGenerico newInstance() {
        FragmentGenerico fragment = new FragmentGenerico();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generico, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtGenerico = view.findViewById(R.id.txt_generico);
        botaoGenerico = view.findViewById(R.id.botao_generico);
        Bundle bundle = getArguments();

        String texto = "";

        if (bundle != null && bundle.containsKey("texto")) {
            texto = bundle.getString("texto");
        }

        txtGenerico.setText(texto);

        // GAMBIARRA ENORME
        botaoGenerico.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).fecharfragment();
            } else if (requireActivity() instanceof PerguntasActivity) {
                ((PerguntasActivity) requireActivity()).fecharfragment();
            }
        });
    }
}