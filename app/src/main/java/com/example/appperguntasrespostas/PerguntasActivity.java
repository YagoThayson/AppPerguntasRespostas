package com.example.appperguntasrespostas;

import java.util.ArrayList;
import java.util.Collections;

import android.os.CountDownTimer;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;


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
    private View barraEsquerda, barraDireita;
    private CountDownTimer timer;
    private static final long TEMPO_TOTAL = 30000;
    private TextView txtTempo;

    private boolean ajudaUsada1 = false;
    private boolean ajudaUsada2 = false;
    private boolean ajudaUsada3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String nomeJogador = getIntent().getStringExtra("nomeJogador");
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

        findViewById(R.id.ajuda1).setOnClickListener(v -> {
            if (ajudaUsada1) {
                v.setEnabled(false);
                Log.d("TRIVIA", "Ajuda já foi usada");
                return;
            }
            ajudaUsada1 = true;
            Log.d("TRIVIA", "Ajuda usada");
        });
        findViewById(R.id.ajuda2).setOnClickListener(v -> {
            if (ajudaUsada2) {
                v.setEnabled(false);
                Log.d("TRIVIA", "Ajuda já foi usada");
                return;
            }
            ajudaUsada2 = true;
            Log.d("TRIVIA", "Ajuda usada");
        });
        findViewById(R.id.ajuda3).setOnClickListener(v -> {
            if (ajudaUsada3) {
                Log.d("TRIVIA", "Ajuda já foi usada");
                v.setEnabled(false);
                return;
            }
            ajudaUsada3 = true;
            Log.d("TRIVIA", "Ajuda usada");
        });
        barraEsquerda = findViewById(R.id.barraEsquerda);
        barraDireita = findViewById(R.id.barraDireita);
        txtTempo = findViewById(R.id.txtTempo);


        quadroDePerguntas.setText("Carregando..."); // DEPOIS SUBSTITUIR ISSO AQUI POR UMA TELA DE LOADING SIMPLES EIN
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
                        boolean correta = isCorreta(escolhida);

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
        iniciarCronometro();
    }

    private boolean isCorreta(String escolhida) {
        return escolhida.equals(perguntaAtual.getRespostaCorreta());
    }

    private void iniciarCronometro() {
        if (timer != null) timer.cancel();

        // reseta as barras para o tamanho cheio
        barraEsquerda.setScaleX(1f);
        barraDireita.setScaleX(1f);

        // pivô: cada barra encolhe em direção ao relógio
        barraEsquerda.post(() -> {
            barraEsquerda.setPivotX(barraEsquerda.getWidth()); // lado direito (perto do relógio)
            barraDireita.setPivotX(0f);                        // lado esquerdo (perto do relógio)
        });

        timer = new CountDownTimer(TEMPO_TOTAL, 50) {
            @Override
            public void onTick(long millisRestantes) {
                // número: arredonda para cima para mostrar 30 no início e 1 no fim
                txtTempo.setText(String.valueOf((millisRestantes + 999) / 1000));

                // barras: de 1 (cheia) até 0 (vazia)
                float proporcao = (float) millisRestantes / TEMPO_TOTAL;
                barraEsquerda.setScaleX(proporcao);
                barraDireita.setScaleX(proporcao);
            }

            @Override
            public void onFinish() {
                txtTempo.setText("0");
                barraEsquerda.setScaleX(0f);
                barraDireita.setScaleX(0f);
                tempoEsgotado();
            }
        }.start();
    }

    private void tempoEsgotado() {
        // aqui: contar como erro e passar para a próxima pergunta
    }

}