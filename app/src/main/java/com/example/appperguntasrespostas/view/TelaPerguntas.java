package com.example.appperguntasrespostas.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appperguntasrespostas.R;

public class TelaPerguntas extends AppCompatActivity {
    private TextView txtTempo, txtPergunta;
    private View barraEsquerda, barraDireita;
    private ImageButton ajuda1, ajuda2, ajuda3;
    private boolean ajuda1Usada = false;
    private boolean ajuda2Usada = false;
    private boolean ajuda3Usada = false;
    private CardView resposta1, resposta2, resposta3, resposta4;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_de_perguntas);
        txtTempo = findViewById(R.id.txtTempo);
        txtPergunta = findViewById(R.id.quadroDePerguntas);
        barraEsquerda = findViewById(R.id.barraEsquerda);
        barraDireita = findViewById(R.id.barraDireita);
        ajuda1 = findViewById(R.id.ajuda1);
        ajuda2 = findViewById(R.id.ajuda2);
        ajuda3 = findViewById(R.id.ajuda3);
        resposta1 = findViewById(R.id.Resposta1);
        resposta2 = findViewById(R.id.Resposta2);
        resposta3 = findViewById(R.id.Resposta3);
        resposta4 = findViewById(R.id.Resposta4);

        ajuda1.setOnClickListener(v -> {
            if (ajuda1Usada) return;
            ajuda1Usada = true;
            usarAjuda(ajuda1);
        });

        ajuda2.setOnClickListener(v -> {
            if (ajuda2Usada) return;
            ajuda2Usada = true;
            // ação da ajuda 2 aqui: fazer em um metodo para ficar mais facil a organização
            usarAjuda(ajuda2);
        });

        ajuda3.setOnClickListener(v -> {
            if (ajuda3Usada) return;
            ajuda3Usada = true;
            // ação da ajuda 3 aqui: fazer em um metodo para ficar mais facil a organização
            usarAjuda(ajuda3);
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void iniciarCronometro() {
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisRestantes) {
                txtTempo.setText(String.valueOf(millisRestantes / 1000));
            }

            @Override
            public void onFinish() {
                txtTempo.setText("0");
                // contar como erro e ir para a próxima
            }
        }.start();
    }
    // metodo para deixar o botao com o aspecto de inclicavel, ajustar o setAlpha para deixar mais ou menos visivel
    //0 invisivel, 1 visivel
    private void usarAjuda(ImageButton botao) {
        botao.setEnabled(false);
        botao.setAlpha(0.1f);
    }

    private void resetarAjuda(ImageButton botao) {
        botao.setEnabled(true);
        botao.setAlpha(1f);
    }
}
