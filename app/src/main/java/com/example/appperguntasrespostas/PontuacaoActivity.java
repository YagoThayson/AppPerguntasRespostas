package com.example.appperguntasrespostas;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.TextView;

public class PontuacaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.tela_pontuacao);
        Button btnVoltarParaInicio = findViewById(R.id.buttonVoltarParaInicio);
        Button btnjogarNovamente = findViewById(R.id.buttonJogarNovamente);

        String nomeJogador = getIntent().getStringExtra("nomeJogador");
        int acertos = getIntent().getIntExtra("Acertos", 10);
        int total = getIntent().getIntExtra("TotalPerguntas", 10);

        // TextView txtNome = findViewById(R.id.txtNome);
        TextView Txtacertos = findViewById(R.id.acertos);
        TextView taxaAcertos = findViewById(R.id.taxa);
        TextView pontuacao = findViewById(R.id.pontuacao);

        //gambiarra ai para setText em String
        pontuacao.setText("" + (acertos * 100));
        Txtacertos.setText("" + acertos);
        taxaAcertos.setText(acertos + "/" + (total));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVoltarParaInicio.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        btnjogarNovamente.setOnClickListener(v -> {
            Intent intent = new Intent(this, OpcoesActivity.class);
            intent.putExtra("nomeJogador", nomeJogador);
            startActivity(intent);
        });
    }
}


