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

import com.example.appperguntasrespostas.model.Jogador;

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
    //Estou fazendo a verificação caso aperte no botão de iniciar a partida, estou
    // tratando no set do model tbm
        btnIniciarPartida.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();

            if (nome.isEmpty()) {
                // Depois pesquise algum jeito de mostrar o erro na tela
                System.out.println("Nome está vazio");
                return;
            }

            Jogador jogador = new Jogador(nome);

            Intent intent = new Intent(this, PerguntasActivity.class);
            intent.putExtra("nome_jogador", jogador.getNome());
            startActivity(intent);
        });
    }
}