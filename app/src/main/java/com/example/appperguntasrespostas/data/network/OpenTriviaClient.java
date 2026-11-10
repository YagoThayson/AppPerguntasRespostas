package com.example.appperguntasrespostas.data.network;

import android.os.Handler;
import android.os.Looper;

import androidx.core.text.HtmlCompat;

import com.example.appperguntasrespostas.model.ConfiguracaoPartida;
import com.example.appperguntasrespostas.model.Pergunta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OpenTriviaClient {

    private static final String BASE_URL = "https://opentdb.com/api.php";
    private static final int TIMEOUT_MS = 10000;

    // Quando a requisição terminar, ele vai dar um desses 2 estados, precisar reescrever ele na activity que vai receber a requisição
    public interface Callback {
        void onSucesso(List<Pergunta> perguntas);
        void onErro(String mensagem);
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // Roda em background e devolve o resultado na UI Thread
    public void buscarPerguntas(ConfiguracaoPartida config, Callback callback) {
        executor.execute(() -> {
            try {
                RespostaApi resposta = requisitar(config);
                mainHandler.post(() -> callback.onSucesso(resposta.getPerguntas()));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onErro("Ocorreu um erro."));
            } // Depois pegar todas as possiveis excessoes e colocar mais catch
        });
    }

    private RespostaApi requisitar(ConfiguracaoPartida config)
            throws Exception {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(montarUrl(config)).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);

            int http = conn.getResponseCode();
            if (http == 429) {
                throw new Exception("Muitas requisições. Aguarde alguns segundos e tente de novo.");
            }
            if (http != HttpURLConnection.HTTP_OK) {
                throw new Exception("Servidor indisponível (código " + http + ").");
            }
            return interpretar(lerCorpo(conn.getInputStream()));
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String montarUrl(ConfiguracaoPartida config) {
        String url = BASE_URL;

        url += "?amount=" + config.getQuantidade();
        url += "&type=multiple";

        if (config.getCategoriaId() > 0) {
            url += "&category=" + config.getCategoriaId();
        }

        if (config.getDificuldade() != null &&
                !config.getDificuldade().isEmpty()) {
            url += "&difficulty=" + config.getDificuldade();
        }

        return url;
    }

    private String lerCorpo(InputStream in) throws IOException {
        String conteudo = "";
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                conteudo += linha;
            }
        }
        return conteudo;
    }

    private RespostaApi interpretar(String json) throws JSONException, Exception {
        JSONObject raiz = new JSONObject(json);
        int code = raiz.getInt("response_code");
        // Só para saber qual foi o erro, quiserem olhar as responses tá aqui https://opentdb.com/api_config.php
        switch (code) {
            case 0: break;
            case 1: throw new Exception("Não há questões suficientes para essa configuração.");
            case 2: throw new Exception("Parâmetros inválidos na requisição.");
            case 3: throw new Exception("Token não encontrado");
            case 4: throw new Exception("Problema com o token da sessão.");
            case 5: throw new Exception("Muitas requisições. Aguarde alguns segundos.");
            default: throw new Exception("Erro desconhecido da API (código " + code + ").");
        }

        JSONArray results = raiz.getJSONArray("results");
        List<Pergunta> perguntas = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject item = results.getJSONObject(i);

            List<String> incorretas = new ArrayList<>();
            JSONArray arr = item.getJSONArray("incorrect_answers");
            for (int j = 0; j < arr.length(); j++) {
                incorretas.add(decodificar(arr.getString(j)));
            }

            perguntas.add(new Pergunta(
                    decodificar(item.getString("category")),
                    item.getString("difficulty"),
                    decodificar(item.getString("question")),
                    decodificar(item.getString("correct_answer")),
                    incorretas));
        }
        return new RespostaApi(code, perguntas);
    }

    // Converte HTML entities (&quot;,) em texto normal
    private String decodificar(String texto) {
        return HtmlCompat.fromHtml(texto, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
    }
}