package com.example.appperguntasrespostas;

import java.util.ArrayList;
import java.util.Collections;

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
        setContentView(R.layout.tela_de_perguntas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView quadroDePerguntas = findViewById(R.id.quadroDePerguntas);
        TextView[] txtRespostas = {
                findViewById(R.id.txtResposta1),
                findViewById(R.id.txtResposta2),
                findViewById(R.id.txtResposta3),
                findViewById(R.id.txtResposta4)
        };
        quadroDePerguntas.setText("Carregando...");
        int qtd_perguntas = 50; // Depois criar metodos que receberão respotas da tela de configuração de partida. LEMBRE DO MAX DE 50

        ConfiguracaoPartida config = new ConfiguracaoPartida(9, "General Knowledge", "easy", qtd_perguntas, 20);
        new OpenTriviaClient().buscarPerguntas(config, new OpenTriviaClient.Callback() {

            @Override
            public void onSucesso(List<Pergunta> perguntas) {
                if (perguntas.isEmpty()) {
                    quadroDePerguntas.setText("Nenhuma pergunta encontrada.");
                    return;
                }

                Pergunta p = perguntas.get(0);
                quadroDePerguntas.setText(p.getEnunciado());

                List<String> alternativas = new ArrayList<>(p.getRespostasIncorretas());
                alternativas.add(p.getRespostaCorreta());
                Collections.shuffle(alternativas);

                for (int i = 0; i < txtRespostas.length && i < alternativas.size(); i++) {
                    txtRespostas[i].setText(alternativas.get(i));
                }
            }

            @Override
            public void onErro(String mensagem) {
                quadroDePerguntas.setText(mensagem);
            }
        });
    }
}