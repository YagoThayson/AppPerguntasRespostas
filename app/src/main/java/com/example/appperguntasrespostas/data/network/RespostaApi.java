package com.example.appperguntasrespostas.data.network;

import com.example.appperguntasrespostas.model.Pergunta;


// Essa classe serve só para pegar os dados da OpenTriviaClient, guardar os dados no objeto e entregar pelos GETTERS.

import java.util.List;

public class RespostaApi {
    private final int responseCode;
    private final List<Pergunta> perguntas;

    public RespostaApi(int responseCode, List<Pergunta> perguntas) {
        this.responseCode = responseCode;
        this.perguntas = perguntas;
    }

    public int getResponseCode() { return responseCode; }
    public List<Pergunta> getPerguntas() { return perguntas; }
}