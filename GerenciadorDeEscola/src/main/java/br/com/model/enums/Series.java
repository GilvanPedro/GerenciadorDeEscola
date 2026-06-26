package br.com.model.enums;

public enum Series {
    // Infantil
    CRECHE("Creche"),
    PRE_ESCOLA("Pré-Escola"),

    // Ensino Fundamental
    PRIMEIRO_ANO("1º Ano"),
    SEGUNDO_ANO("2º Ano"),
    TERCEIRO_ANO("3º Ano"),
    QUARTO_ANO("4º Ano"),
    QUINTO_ANO("5º Ano"),
    SEXTO_ANO("6º Ano"),
    SETIMO_ANO("7º Ano"),
    OITAVO_ANO("8º Ano"),
    NONO_ANO("9º Ano"),

    // Ensino Médio
    PRIMEIRA_SERIE("1ª Série"),
    SEGUNDA_SERIE("2ª Série"),
    TERCEIRA_SERIE("3ª Série");

    private String descricao;

    Series(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}