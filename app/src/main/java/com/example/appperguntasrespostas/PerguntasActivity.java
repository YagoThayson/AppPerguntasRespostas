package com.example.appperguntasrespostas;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;
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

    private Pergunta perguntaAtual;
    private int acertos = 0;
    private boolean respondida = false;
    private List<Pergunta> listaPerguntas;
    private int indiceAtual = 0;
    private TextView quadroDePerguntas;
    private TextView[] txtRespostas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String nomeJogador = getIntent().getStringExtra("nome_jogador");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_de_perguntas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quadroDePerguntas = findViewById(R.id.quadroDePerguntas);
        txtRespostas = new TextView[] {
                findViewById(R.id.txtResposta1),
                findViewById(R.id.txtResposta2),
                findViewById(R.id.txtResposta3),
                findViewById(R.id.txtResposta4)
        };
        quadroDePerguntas.setText("Carregando...");
        int qtd_perguntas = 50; // Depois criar metodos que receberão respostas da tela de configuração de partida. LEMBRE DO MAX DE 50
        String categoriaNome = "General Knowledge";
        int categoriaid = 9;
        String dificuldade = "easy";
        int tempoPorQuestao = 30;

        ConfiguracaoPartida config = new ConfiguracaoPartida(categoriaid, categoriaNome, dificuldade, qtd_perguntas, tempoPorQuestao);
        new OpenTriviaClient().buscarPerguntas(config, new OpenTriviaClient.Callback() {

            @Override
            public void onSucesso(List<Pergunta> perguntas) {
                if (perguntas.isEmpty()) {
                    quadroDePerguntas.setText("Nenhuma pergunta encontrada.");
                    return;
                }

                listaPerguntas = perguntas;
                indiceAtual = 0;
                mostrarPergunta();

                int[] idsCards = { R.id.Resposta1, R.id.Resposta2, R.id.Resposta3, R.id.Resposta4 };
                for (int i = 0; i < idsCards.length; i++) {
                    final int pos = i;
                    findViewById(idsCards[pos]).setOnClickListener(v -> {
                        if (respondida) {
                            return;
                        }
                        respondida = true;

                        String escolhida = txtRespostas[pos].getText().toString();
                        boolean correta = estaCorreta(escolhida);

                        if (correta) {
                            acertos++;
                        }

                        Log.d("TRIVIA", escolhida + " -> " + (correta ? "CORRETA" : "INCORRETA") + " | acertos: " + acertos);

                        indiceAtual++;
                        if (indiceAtual < listaPerguntas.size()) {
                            mostrarPergunta();
                        } else {
                            Log.d("TRIVIA", "Fim da partida: o jogador: " +  nomeJogador + " fez " + acertos + "/" + listaPerguntas.size());
                        }
                    });
                }
            }

            @Override
            public void onErro(String mensagem) {
                quadroDePerguntas.setText(mensagem);
            }
        });
    }

    private void mostrarPergunta() {
        Pergunta p = listaPerguntas.get(indiceAtual);
        perguntaAtual = p;
        respondida = false;
        quadroDePerguntas.setText(p.getEnunciado());

        List<String> alternativas = new ArrayList<>(p.getRespostasIncorretas());
        alternativas.add(p.getRespostaCorreta());
        Collections.shuffle(alternativas);

        for (int i = 0; i < txtRespostas.length && i < alternativas.size(); i++) {
            txtRespostas[i].setText(alternativas.get(i));
        }
    }

    private boolean estaCorreta(String escolhida) {
        return perguntaAtual != null && escolhida.equals(perguntaAtual.getRespostaCorreta());
    }
}