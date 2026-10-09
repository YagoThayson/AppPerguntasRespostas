package com.example.appperguntasrespostas.model;

// Partida.java
public class Partida {
    private long id;
    private long jogadorId;
    private long dataHora;        // System.currentTimeMillis()
    private String categoria;
    private String dificuldade;
    private int totalQuestoes;
    private int acertos;
    private int pontuacao;

    public Partida(long jogadorId, long dataHora, String categoria, String dificuldade,
                   int totalQuestoes, int acertos, int pontuacao) {
        this.jogadorId = jogadorId;
        this.dataHora = dataHora;
        this.categoria = categoria;
        this.dificuldade = dificuldade;
        this.totalQuestoes = totalQuestoes;
        this.acertos = acertos;
        this.pontuacao = pontuacao;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getJogadorId() {
        return jogadorId;
    }
    public long getDataHora() {
        return dataHora;
    }
    public String getCategoria() {
        return categoria;
    }
    public String getDificuldade() {
        return dificuldade;
    }
    public int getTotalQuestoes() {
        return totalQuestoes;
    }
    public int getAcertos() {
        return acertos;
    }
    public int getPontuacao() {
        return pontuacao;
    }
}
