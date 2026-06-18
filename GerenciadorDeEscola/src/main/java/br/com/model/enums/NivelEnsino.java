package br.com.model.enums;

public enum NivelEnsino {
    INFALTIL("Infantil"),
    ENSINO_FUNDAMENTAL("Ensino Fundamental"),
    ENSINO_MEDIO("Ensino Médio");

    private String descricao;

    NivelEnsino(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}