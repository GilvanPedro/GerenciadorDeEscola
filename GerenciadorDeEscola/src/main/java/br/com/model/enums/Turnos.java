package br.com.model.enums;

public enum Turnos {
    MATUTINO("Matutino"),
    VESPERTINO("Vespertino");

    private String descricao;

    Turnos(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}