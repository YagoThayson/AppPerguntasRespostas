
package com.example.appperguntasrespostas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

// Imports para fazer os spinners de buscar as categorias.
import android.widget.Spinner;
import java.util.ArrayList;
import android.widget.ArrayAdapter;

import android.util.Log;

import com.example.appperguntasrespostas.model.FragmentGenerico;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

public class OpcoesActivity extends AppCompatActivity {
    private Button btnJogar;
    private String nomeJogador;
    private Spinner spinnerCategoria;
    private Spinner spinnerDificuldade;
    private Spinner spinnerQtdPerguntas;
    private ArrayList<Integer> idsCategorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nomeJogador = getIntent().getStringExtra("nomeJogador");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_opcoes);

        btnJogar = findViewById(R.id.Jogar);
        spinnerCategoria = findViewById(R.id.spinner1);
        spinnerDificuldade = findViewById(R.id.spinner2);
        spinnerQtdPerguntas = findViewById(R.id.spinner3);

        configurarDificuldade();
        configurarQtdPerguntas();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        carregarCategorias();
        configurarDificuldade();
        configurarQtdPerguntas();

        btnJogar.setOnClickListener(v -> {
            int qtdPerguntas = (Integer) spinnerQtdPerguntas.getSelectedItem();
            System.out.println(spinnerQtdPerguntas.getSelectedItem());

            int posicao = spinnerCategoria.getSelectedItemPosition();
            String categoriaNome = spinnerCategoria.getSelectedItem().toString();
            int categoriaId = idsCategorias.get(posicao);

            String dificuldade = spinnerDificuldade.getSelectedItem().toString();
            // int tempoPorQuestao = 30; // vai ser padrão né? se sim vou deixar só lá na perguntas activity msm

            Intent intent = new Intent(this, PerguntasActivity.class);
            intent.putExtra("nomeJogador", nomeJogador);
            intent.putExtra("categoriaId", categoriaId);
            intent.putExtra("categoriaNome", categoriaNome);
            intent.putExtra("dificuldade", dificuldade);
            intent.putExtra("qtdPerguntas", qtdPerguntas);

            startActivity(intent);
        });
    }

    private void carregarCategorias() {
        // Para executar em segundo plano, o ANDROID n deixa fazer na thread principal pq é uma operação de rede
        new Thread(() -> {
            try {
                // Cria a URL e faz a conversão para a gente poder fazer requisições HTTP
                URL url = new URL("https://opentdb.com/api_category.php");
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                Scanner scanner = new Scanner(conexao.getInputStream());
                String resposta = scanner.useDelimiter("\\A").next();

                JSONObject json = new JSONObject(resposta);
                JSONArray categorias = json.getJSONArray("trivia_categories");
                ArrayList<String> nomes = new ArrayList<>();
                idsCategorias.clear();
                for (int i = 0; i < categorias.length(); i++) {
                    JSONObject categoria = categorias.getJSONObject(i);
                    nomes.add(categoria.getString("name"));
                    idsCategorias.add(categoria.getInt("id"));
                }

                scanner.close();
                conexao.disconnect();
                // Estava dando erro, pesquisei: Já que o android n deixa executar na thread principal, e temos que mostrar nela.
                // O runOnUiThread() permite executar esse trecho na thread responsável pela interface.
                runOnUiThread(() -> {
                    // O spinner não recebe um ArrayList parece, esse ADAPTER serve para apresentar eles na tela
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            nomes
                    );

                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    spinnerCategoria.setAdapter(adapter);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnJogar.setVisibility(View.INVISIBLE);
                    btnJogar.setEnabled(false);
                    chamarFragment();
                });
                Log.e("OpcoesActivity", "Erro ao carregar categorias", e); // Depois ver possiveis tratamentos, talvez entrar no modo OFF
            }
        }).start();
    }

    private void configurarDificuldade(){
        String[] dificuldades = {"easy", "medium", "hard" };

        ArrayAdapter<String> adapterDificuldade = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dificuldades
        );

        adapterDificuldade.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerDificuldade.setAdapter(adapterDificuldade);
    }
    private void configurarQtdPerguntas(){
        // Esse arrayAdapter trabalha com objetos e não com tipos primitivos.
        Integer[] qtdPerguntas = {1,5, 10, 15, 20,25, 30, 35, 40, }; // Adicionei o 1 só para o teste ser mais rápido TIRAR DEPOIS

        ArrayAdapter<Integer> adapterQtdPerguntas = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                qtdPerguntas
        );

        adapterQtdPerguntas.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerQtdPerguntas.setAdapter(adapterQtdPerguntas);
    }
    public void chamarFragment(){
        FragmentOfline fragment = FragmentOfline.newInstance(nomeJogador);

        findViewById(R.id.container_offline).setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_offline, fragment)
                .commit();
    }
    public void fecharfragment(){
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.container_offline);

        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();

        }

        findViewById(R.id.container_offline)
                .setVisibility(View.GONE);
    }
}
