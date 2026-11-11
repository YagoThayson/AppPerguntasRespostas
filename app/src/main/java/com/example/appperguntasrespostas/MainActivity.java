package com.example.appperguntasrespostas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.appperguntasrespostas.model.FragmentGenerico;
import com.example.appperguntasrespostas.model.Pergunta;

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
        Button btnHistorico = findViewById(R.id.Historico);
    // Estou fazendo a verificação quando aperta no botão, senti que criar uma classe jogador era desnecessari
        // então resolvi tornar jogador um atributo da partida, acho que fica mais simples
        btnIniciarPartida.setOnClickListener(v -> {
            String nomeJogador = edtNome.getText().toString().trim();

            if (nomeJogador.isEmpty()) {
                // Depois pesquise algum jeito de mostrar o erro na tela
                String texto = "Nome está vazio";

                chamarFragment(texto);
                return;
            }

            Intent intent = new Intent(this, OpcoesActivity.class);
            intent.putExtra("nomeJogador", nomeJogador);
            startActivity(intent);
        });

        btnHistorico.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoricoActivity.class);
            startActivity(intent);
        });
    }

    public void chamarFragment(String texto){
        FragmentGenerico fragment = FragmentGenerico.newInstance();


        Bundle bundle = new Bundle();
        bundle.putString("texto",texto);
        fragment.setArguments(bundle);

        findViewById(R.id.container_fragment_generico).setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment_generico, fragment)
                .commit();
    }

    public void fecharfragment(){
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.container_fragment_generico);

        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();

        }

        findViewById(R.id.container_fragment_generico)
                .setVisibility(View.GONE);
    }
}