package com.example.appperguntasrespostas;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// IMPORTS para o content provider/resolver e
// Para os texts de carregar historico
import android.view.View;
import android.widget.TextView;



public class HistoricoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.historico);
        // Uri uri = Uri.parse("content://com.example.appperguntasrespostas.provider/historico");

        // O getContent retorna o objeto, e o query consulta o contentProvider
        // Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Button btnVoltar = findViewById(R.id.botao_voltar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        carregarHistorico();
    }

    private void carregarHistorico(){
        Uri uri = Uri.parse("content://com.example.appperguntasrespostas.provider/historico");

        Cursor cursor = getContentResolver().query(uri,null, null, null, null);

        if(cursor == null){
            return;
        }
        View[] cards = {
                findViewById(R.id.opcao1),
                findViewById(R.id.opcao2),
                findViewById(R.id.opcao3),
                findViewById(R.id.opcao4),
                findViewById(R.id.opcao5),
        };
        TextView[] jogadores = {
                findViewById(R.id.txtJogador1),
                findViewById(R.id.txtJogador2),
                findViewById(R.id.txtJogador3),
                findViewById(R.id.txtJogador4),
                findViewById(R.id.txtJogador5),
        };
        TextView[] dificuldades = {
                findViewById(R.id.txtDificuldade1),
                findViewById(R.id.txtDificuldade2),
                findViewById(R.id.txtDificuldade3),
                findViewById(R.id.txtDificuldade4),
                findViewById(R.id.txtDificuldade5),
        };
        TextView[] pontos = {
                findViewById(R.id.txtPontos1),
                findViewById(R.id.txtPontos2),
                findViewById(R.id.txtPontos3),
                findViewById(R.id.txtPontos4),
                findViewById(R.id.txtPontos5),
        };

        // Para não mostrar os cards
         for(int i = 0; i < cards.length; i++){
            cards[i].setVisibility(View.INVISIBLE);
         }

        if(cursor.moveToLast()){
            for(int i = 0; i < 5; i++){
                jogadores[i].setText(cursor.getString(0));
                dificuldades[i].setText(cursor.getString(3)); // quarta coluna do provider (Acho que é o 3)
                pontos[i].setText(String.valueOf(cursor.getInt(1)* 100));


                cards[i].setVisibility(View.VISIBLE);

                if (!cursor.moveToPrevious()) break;
            }
        }
        cursor.close();
    }

}