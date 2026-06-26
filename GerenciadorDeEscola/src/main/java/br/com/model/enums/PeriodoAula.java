package br.com.model.enums;

public enum PeriodoAula {
    INTERVALO("Intervalo"),

    PRIMEIRA_AULA("1ª Aula"),
    SEAGUNDA_AULA("2ª Aula"),
    TERCEIRA_AULA("3ª Aula"),
    QUARTA_AULA("4ª Aula"),
    QUINTA_AULA("5ª Aula"),
    SEXTA_AULA("6ª Aula"),
    SETIMA_AULA("7ª Aula"),
    OITAVA_AULA("8ª Aula"),
    NONA_AULA("9ª Aula");

    private String descricao;

    PeriodoAula(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}