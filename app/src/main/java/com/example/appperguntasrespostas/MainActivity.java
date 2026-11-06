package com.example.appperguntasrespostas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_inicial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText edtNome = findViewById(R.id.NomeDeUsuario);
        Button btnIniciarPartida = findViewById(R.id.IniciarPartida);
    // Estou fazendo a verificação quando aperta no botão, senti que criar uma classe jogador era desnecessari
        // então resolvi tornar jogador um atributo da partida, acho que fica mais simples
        btnIniciarPartida.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();

            if (nome.isEmpty()) {
                // Depois pesquise algum jeito de mostrar o erro na tela
                System.out.println("Nome está vazio");
                return;
            }

            Intent intent = new Intent(this, PerguntasActivity.class);
            intent.putExtra("nomeJogador", nome);
            startActivity(intent);
        });
    }
}