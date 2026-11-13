package com.example.appperguntasrespostas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import android.os.CountDownTimer;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

// Bibliotecas para CRIAR O JSON das partidas e JSON de perguntas
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.json.JSONArray;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.appperguntasrespostas.data.network.OpenTriviaClient;
import com.example.appperguntasrespostas.model.ConfiguracaoPartida;
import com.example.appperguntasrespostas.model.FragmentGenerico;
import com.example.appperguntasrespostas.model.Pergunta;
// import com.example.appperguntasrespostas.model.ConfiguracaoPartida;
import android.content.Intent;
import com.example.appperguntasrespostas.PontuacaoActivity; // acho q nem precisa pq tá no mesmo packpage. Pesquisa dps

import java.util.List;
import java.util.Scanner;

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

    private String nomeJogador;
    private ConfiguracaoPartida config;
    private boolean ajudaUsada1 = false;
    private boolean ajudaUsada2 = false;
    private boolean ajudaUsada3 = false;
    private static final int[] IDS_CARDS ={ R.id.Resposta1, R.id.Resposta2, R.id.Resposta3, R.id.Resposta4 };
    private ImageView xAjuda1, xAjuda2, xAjuda3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        nomeJogador = getIntent().getStringExtra("nomeJogador");
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

        findViewById(R.id.ajuda1).setOnClickListener(v -> ajuda1Metade());
        findViewById(R.id.ajuda2).setOnClickListener(v -> ajuda2RespostaCorreta());
        findViewById(R.id.ajuda3).setOnClickListener(v -> ajuda3PularPergunta());

        barraEsquerda = findViewById(R.id.barraEsquerda);
        barraDireita = findViewById(R.id.barraDireita);
        txtTempo = findViewById(R.id.txtTempo);


        quadroDePerguntas.setText("Carregando..."); // DEPOIS SUBSTITUIR ISSO AQUI POR UMA TELA DE LOADING SIMPLES EIN


        int qtd_perguntas = getIntent().getIntExtra("qtdPerguntas", 10); // Depois criar metodos que receberão respostas da tela de configuração de partida. LEMBRE DO MAX DE 50
        String categoriaNome = getIntent().getStringExtra("categoriaNome");
        int categoriaId = getIntent().getIntExtra("categoriaId", 9);; // Por enquanto vou deixar padrão, mas me lembrem de fazer a lógica para ele pegar o id, comparando o nome da categoria escolhida
        String dificuldade = getIntent().getStringExtra("dificuldade");
        int tempoPorQuestao = 30;

        config = new ConfiguracaoPartida(categoriaId, categoriaNome, dificuldade, qtd_perguntas, tempoPorQuestao);

        ConfiguracaoPartida config = new ConfiguracaoPartida(categoriaId, categoriaNome, dificuldade, qtd_perguntas, tempoPorQuestao);
        new OpenTriviaClient().buscarPerguntas(config, new OpenTriviaClient.Callback() {

            @Override
            public void onSucesso(List<Pergunta> perguntas) {
                iniciarPartida(perguntas);
            }
            @Override
            public void onErro(String mensagem) {
                Log.e("TRIVIA", "Entrou no onErro!");
                // Log.e("TRIVIA", "Erro ao carregar perguntas na API");
                carregarPerguntasLocais();

            }
        });
    }
    private void iniciarPartida(List<Pergunta> perguntas){
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
                        processarResposta(correta,escolhida);
                    });
                }
            }

    private void mostrarPergunta() {
        // Depois adicionar um IF para não entrar sempre nesse laço, mesmo que não seja necessario
        for (int i = 0; i < IDS_CARDS.length; i++) {
            View card = findViewById(IDS_CARDS[i]);
            card.setEnabled(true);
            card.setVisibility(View.VISIBLE);
        }

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
        findViewById(R.id.container_tempo_esgotado).setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_tempo_esgotado, new TempoEsgotado())
                .commit();
    }

    public void continuarAposTempoEsgotado() {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.container_tempo_esgotado);

        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();

        }

        findViewById(R.id.container_tempo_esgotado)
                .setVisibility(View.GONE);

        processarResposta(false, "Tempo esgotado");
    }

    private void processarResposta(boolean correta, String escolhida) {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (correta) {
            acertos++;
        }

        Log.d("TRIVIA", escolhida + " -> " + (correta ? "CORRETA" : "INCORRETA") + " | acertos: " + acertos);

        indiceAtual++;

        if (indiceAtual < listaPerguntas.size()) {

            mostrarPergunta();

        } else {
            // Log.d("TRIVIA", resultado.toString(4))
            // Aqui você coloca o bloco que já possui
            salvarJson();
        }
    }

    private void ajuda1Metade() {
        if(ajudaUsada1){
            return;
        }
        ajudaUsada1 = true;

        List<Integer> posicoesErradas = new ArrayList<>();

        for (int i = 0; i < txtRespostas.length; i++) {
            // Como esse textview não é uma String propriamente dita, só fucniona se eu pegar o texto e converter para String.
            if(!(txtRespostas[i].getText().toString().equals(perguntaAtual.getRespostaCorreta()))){
                posicoesErradas.add(i);
            }
        }
        Collections.shuffle(posicoesErradas);

        for(int i = 0; i < 2; i++){
            findViewById(IDS_CARDS[posicoesErradas.get(i)]).setEnabled(false);
            findViewById(IDS_CARDS[posicoesErradas.get(i)]).setVisibility(View.INVISIBLE);
            //resolver o problema que a alternativa fica desabilitada e invisivel a partida inteira
            Log.d("TRIVIA", "Ajuda 1: Elimina a posição " + posicoesErradas.get(i));
            // TODO: DEPOIS substituir esse log pela logica do frontend
        }

        findViewById(R.id.xAjuda1).setVisibility(View.VISIBLE);

    }
    private void ajuda2RespostaCorreta() {
        if(ajudaUsada2){
            return;
        }
        ajudaUsada2 = true;
        String correta = perguntaAtual.getRespostaCorreta();

        for (int i = 0; i < txtRespostas.length; i++) {
            if (txtRespostas[i].getText().toString().equals(correta)) {
                // TODO: DEPOIS destacar o card da posição i, que vai ser o card da posição correta.
                Log.d("TRIVIA", "Ajuda 2: a correta está na posição " + i);
                String teste = "a opção correta está na posição "+ (int)(i+1);
                chamarFragment(teste);
            }
            findViewById(R.id.xAjuda2).setVisibility(View.VISIBLE);
        }
    }
    private void ajuda3PularPergunta() {
        if(ajudaUsada3){
            return;
        }
        ajudaUsada3 = true;

    }

    private void carregarPerguntasLocais() {
        try {
            // Vamos mover para a pasta RAW pq existem um metodo para acessar ela de forma facinha
            InputStream arquivo = getResources().openRawResource(R.raw.perguntas);
            // Tava dando erro, antes dessa linha, ele só lia até o primeiro espaço em branco
            Scanner scanner = new Scanner(arquivo).useDelimiter("\\Z");

            // Ai a gente armazena esse SCAN em uma variavel para depois transformar em um JSONObject
            String conteudo = "";
            if (scanner.hasNext()) {
                conteudo = scanner.next();
            }
            scanner.close();

            //Pega a string conteudo e transforma em um objeto do tipo JSONObject
            JSONObject json = new JSONObject(conteudo);
            JSONArray resultados = json.getJSONArray("results");

            //  Como não dá para usar direto o respostasJson pq respostas incorretas é uma lists e não um JSONArray, criamos outra lista
            listaPerguntas = new ArrayList<>();
            for (int i = 0; i < resultados.length(); i++) {
                JSONObject item = resultados.getJSONObject(i);

                JSONArray respostasJson = item.getJSONArray("incorrect_answers");
                List<String> incorretas = new ArrayList<>();

                for (int j = 0; j < respostasJson.length(); j++) {
                    incorretas.add(respostasJson.getString(j));
                }

                // depois de percorrer as respostas eu crio o objeto pergunta
                listaPerguntas.add(new Pergunta(
                        item.getString("category"),
                        item.getString("difficulty"),
                        item.getString("question"),
                        item.getString("correct_answer"),
                        incorretas
                ));
            }

            indiceAtual = 0; // só para começar pela primeira pergunta
            Collections.shuffle(listaPerguntas);
            // Metodo massa DEMAIS esse SUBLIST, "ANOTAR ELE PARA PROJETOS FUTUROS"
            listaPerguntas = new ArrayList<>(listaPerguntas.subList(0, 10));

            iniciarPartida(listaPerguntas);

        } catch (Exception e) {
            Log.e("TRIVIA", "Erro ao carregar perguntas locais", e);
        }
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

            findViewById(R.id.container_tempo_esgotado)
                    .setVisibility(View.GONE);
        }
        private void salvarJson(){
            try {
                // Esse metodo put só funciona dentro de um try, catch, então vou lançar uma exception generica msm
                // Log.d("TRIVIA", "Fim da partida: o jogador: " +  nomeJogador + " fez " + acertos + "/" + listaPerguntas.size());
                JSONObject resultado = new JSONObject();
                resultado.put("jogador", nomeJogador);
                resultado.put("dificuldade", config.getDificuldade());
                resultado.put("acertos", acertos);
                resultado.put("totalPerguntas", listaPerguntas.size());

                // Escrever e salvar arquivo JSON para ter persistência de DADOS
                FileOutputStream arquivo = openFileOutput("resultados.json", MODE_APPEND); // Esse mode_append signigica que eu quero adicionar algo, PRIVATE seria sobrescrever por exemplo.
                arquivo.write((resultado.toString() + "\n").getBytes()); // Esse getBytes converto uma string em um vetor de bytes, e depois escreve esses bytes no arquivo JSON, se fosse TXT era mais fácil escrever
                arquivo.close();// Mas seria mais dificil de ler, então deixa assim msm.
                Log.d("TRIVIA", "Resultado salvo com sucesso");
                // Para acessar esse arquivo o caminho no android studio é: VIEW -> Tool Windows -> Device Explorer - data -> data -> com.example.appperguntasrespostas -> files e Vai estar lá

                Intent intent = new Intent(this, PontuacaoActivity.class);
                intent.putExtra("nomeJogador", nomeJogador);
                intent.putExtra("dificuldade", config.getDificuldade());
                intent.putExtra("Acertos", acertos);
                intent.putExtra("TotalPerguntas", listaPerguntas.size());
                startActivity(intent);

            }catch (Exception e ){
                Log.e("TRIVIA", "Erro ao criar JSON", e);
            }
        }
    }
