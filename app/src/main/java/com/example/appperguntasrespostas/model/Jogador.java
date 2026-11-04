package com.example.appperguntasrespostas.model;

// Jogador.java
public class Jogador {
    private long id;
    private String nome;

    public Jogador(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
}
