package com.example.appperguntasrespostas.model;

// Se tiver mais alguma configuração que a gente quiser adicionar mandem mensagem no grupo PFVR
public class ConfiguracaoPartida {
    private int categoriaId;      // id da categoria no Open Trivia DB (ex.: 9 = General Knowledge)
    private String categoriaNome;
    private String dificuldade;   // "easy", "medium" ou "hard"
    private int quantidade;       // a API aceita de 1 a 50 em uma requesição
    private int tempoPorQuestao;  // em segundos, ESSE TEMPO VAI SER PADRÃO???

    public ConfiguracaoPartida(int categoriaId, String categoriaNome, String dificuldade, int quantidade, int tempoPorQuestao) {
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
        this.dificuldade = dificuldade;
        this.quantidade = quantidade;
        this.tempoPorQuestao = tempoPorQuestao;
    }

    public int getCategoriaId() {
        return categoriaId;
    }
    public String getCategoriaNome() {
        return categoriaNome;
    }
    public String getDificuldade() {
        return dificuldade;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public int getTempoPorQuestao() {
        return tempoPorQuestao;
    }
}
