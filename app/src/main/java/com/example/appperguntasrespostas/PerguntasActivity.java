
package com.example.appperguntasrespostas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appperguntasrespostas.data.network.OpenTriviaClient;
import com.example.appperguntasrespostas.model.ConfiguracaoPartida;
import com.example.appperguntasrespostas.model.Pergunta;

public class PerguntasActivity extends AppCompatActivity {

    private Pergunta perguntaAtual;
    private int acertos = 0;
    private boolean respondida = false;
    private List<Pergunta> listaPerguntas;
    private int indiceAtual = 0;

    private String nomeJogador;
    private int tempoPorQuestao = 30;

    private TextView quadroDePerguntas;
    private TextView txtTempo;
    private TextView[] txtRespostas;

    private View barraEsquerda, barraDireita;

    private ImageButton ajuda1, ajuda2, ajuda3;

    private CardView resposta1, resposta2, resposta3, resposta4;

    private boolean ajudaUsada1 = false;
    private boolean ajudaUsada2 = false;
    private boolean ajudaUsada3 = false;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        nomeJogador = getIntent().getStringExtra("nomeJogador");

        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_de_perguntas);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.constraintLayout), (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        quadroDePerguntas = findViewById(R.id.quadroDePerguntas);
        txtTempo = findViewById(R.id.txtTempo);

        barraEsquerda = findViewById(R.id.barraEsquerda);
        barraDireita = findViewById(R.id.barraDireita);

        ajuda1 = findViewById(R.id.ajuda1);
        ajuda2 = findViewById(R.id.ajuda2);
        ajuda3 = findViewById(R.id.ajuda3);

        resposta1 = findViewById(R.id.Resposta1);
        resposta2 = findViewById(R.id.Resposta2);
        resposta3 = findViewById(R.id.Resposta3);
        resposta4 = findViewById(R.id.Resposta4);

        txtRespostas = new TextView[] {
                findViewById(R.id.txtResposta1),
                findViewById(R.id.txtResposta2),
                findViewById(R.id.txtResposta3),
                findViewById(R.id.txtResposta4)
        };

// Travar a ajuda1
        ajuda1.setOnClickListener(v -> {
            if (ajudaUsada1) {
                return;
            }

            ajudaUsada1 = true;
            v.setEnabled(false);

            Log.d("TRIVIA", "Ajuda usada");
        });
        // Travar a ajuda2

        ajuda2.setOnClickListener(v -> {
            if (ajudaUsada2) {
                return;
            }

            ajudaUsada2 = true;
            v.setEnabled(false);

            Log.d("TRIVIA", "Ajuda usada");
        });
    // Travar a ajuda3
        ajuda3.setOnClickListener(v -> {
            if (ajudaUsada3) {
                return;
            }

            ajudaUsada3 = true;
            v.setEnabled(false);

            Log.d("TRIVIA", "Ajuda usada");
        });

        quadroDePerguntas.setText("Carregando...");

        int qtd_perguntas = 50;
        String categoriaNome = "General Knowledge";
        int categoriaid = 9;
        String dificuldade = "easy";

        ConfiguracaoPartida config = new ConfiguracaoPartida(
                categoriaid,
                categoriaNome,
                dificuldade,
                qtd_perguntas,
                tempoPorQuestao
        );

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

                int[] idsCards = {
                        R.id.Resposta1,
                        R.id.Resposta2,
                        R.id.Resposta3,
                        R.id.Resposta4
                };

                for (int i = 0; i < idsCards.length; i++) {

                    final int pos = i;

                    findViewById(idsCards[pos]).setOnClickListener(v -> {

                        if (respondida) {
                            return;
                        }

                        respondida = true;

                        if (timer != null) {
                            timer.cancel();
                        }

                        String escolhida = txtRespostas[pos].getText().toString();
                        boolean correta = isCorreta(escolhida);

                        if (correta) {
                            acertos++;
                        }

                        Log.d("TRIVIA",
                                escolhida + " -> " +
                                        (correta ? "CORRETA" : "INCORRETA") +
                                        " | acertos: " + acertos
                        );

                        indiceAtual++;

                        if (indiceAtual < listaPerguntas.size()) {
                            mostrarPergunta();
                        } else {
                            Log.d("TRIVIA",
                                    "Fim da partida: o jogador: " +
                                            nomeJogador + " fez " +
                                            acertos + "/" + listaPerguntas.size()
                            );
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

        // Inicia o cronômetro para a pergunta atual
        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(tempoPorQuestao * 1000L, 1000) {

            @Override
            public void onTick(long millisRestantes) {
                txtTempo.setText(String.valueOf(millisRestantes / 1000));
            }

            @Override
            public void onFinish() {

                txtTempo.setText("0");

                if (respondida) {
                    return;
                }

                respondida = true;

                Log.d("TRIVIA", "Tempo esgotado! Resposta incorreta.");

                indiceAtual++;

                if (indiceAtual < listaPerguntas.size()) {
                    mostrarPergunta();
                } else {
                    Log.d("TRIVIA",
                            "Fim da partida: o jogador: " +
                                    nomeJogador + " fez " +
                                    acertos + "/" + listaPerguntas.size()
                    );
                }
            }

        }.start();
    }

    private boolean isCorreta(String escolhida) {
        return escolhida.equals(perguntaAtual.getRespostaCorreta());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }
}
