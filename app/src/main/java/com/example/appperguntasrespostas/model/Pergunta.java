package com.example.appperguntasrespostas.model;// Pergunta.java
import java.util.List;

public class Pergunta {
    private long id;
    private String categoria;
    private String dificuldade;
    private String enunciado;
    private String respostaCorreta;
    private List<String> respostasIncorretas;

    public Pergunta(String categoria, String dificuldade, String enunciado,
                    String respostaCorreta, List<String> respostasIncorretas) {
        this.categoria = categoria;
        this.dificuldade = dificuldade;
        this.enunciado = enunciado;
        this.respostaCorreta = respostaCorreta;
        this.respostasIncorretas = respostasIncorretas;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCategoria() {
        return categoria;
    }
    public String getDificuldade() {
        return dificuldade;
    }
    public String getEnunciado() {
        return enunciado;
    }
    public String getRespostaCorreta() {

        return respostaCorreta;
    }
    public List<String> getRespostasIncorretas() {
        return respostasIncorretas;
    }
}