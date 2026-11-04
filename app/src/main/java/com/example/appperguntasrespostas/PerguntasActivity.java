package com.example.appperguntasrespostas;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appperguntasrespostas.data.network.OpenTriviaClient;
import com.example.appperguntasrespostas.model.ConfiguracaoPartida;
import com.example.appperguntasrespostas.model.Pergunta;

import java.util.List;

public class PerguntasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perguntas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtResultado = findViewById(R.id.txtResultado);
        txtResultado.setText("Carregando...");
        int qtd_perguntas = 50;

        ConfiguracaoPartida config = new ConfiguracaoPartida(9, "General Knowledge", "easy", qtd_perguntas, 20);
        new OpenTriviaClient().buscarPerguntas(config, new OpenTriviaClient.Callback() {
            @Override
            public void onSucesso(List<Pergunta> perguntas) {
                StringBuilder sb = new StringBuilder();
                for (Pergunta p : perguntas) {
                    sb.append(p.getCategoria()).append(" | ").append(p.getDificuldade()).append("\n");
                    sb.append(p.getEnunciado()).append("\n");
                    sb.append("Correta: ").append(p.getRespostaCorreta()).append("\n");
                    sb.append("Incorretas: ").append(p.getRespostasIncorretas()).append("\n\n");
                }
                txtResultado.setText(sb.toString());
            }

            @Override
            public void onErro(String mensagem) {
                txtResultado.setText(mensagem);
            }
        });
    }
}