package com.example.appperguntasrespostas.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appperguntasrespostas.R;

public class TelaPerguntas extends AppCompatActivity{
    private TextView txtTempo, txtPergunta;
    private View barraEsquerda, barraDireita;
    private ImageButton ajuda1, ajuda2, ajuda3, ajuda4;
    private CardView resposta1, resposta2, resposta3, resposta4;

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
        ajuda4 = findViewById(R.id.ajuda4);
        resposta1 = findViewById(R.id.Resposta1);
        resposta2 = findViewById(R.id.Resposta2);
        resposta3 = findViewById(R.id.Resposta3);
        resposta4 = findViewById(R.id.Resposta4);

    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
