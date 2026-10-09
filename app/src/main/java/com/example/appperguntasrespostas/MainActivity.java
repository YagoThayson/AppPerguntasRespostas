package com.example.appperguntasrespostas;

import android.os.Bundle;
import android.util.Log;

import com.example.appperguntasrespostas.data.network.OpenTriviaClient;
import com.example.appperguntasrespostas.model.ConfiguracaoPartida;
import com.example.appperguntasrespostas.model.Pergunta;

import java.util.List;

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
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ConfiguracaoPartida config = new ConfiguracaoPartida(9, "General Knowledge", "easy", 5, 20);
        new OpenTriviaClient().buscarPerguntas(config, new OpenTriviaClient.Callback() {
            @Override
            public void onSucesso(List<Pergunta> perguntas) {
                for (Pergunta p : perguntas) Log.d("TRIVIA", p.getEnunciado());
            }

            @Override
            public void onErro(String mensagem) {
                Log.e("TRIVIA", mensagem);
            }
        });
    }
}