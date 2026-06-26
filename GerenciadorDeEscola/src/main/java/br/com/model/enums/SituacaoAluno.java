package br.com.model.enums;

public enum SituacaoAluno {
    ATIVO("Ativo"),
    TRANSFERIDO("Transferido"),
    TRANCADO("Trancado");

    private String descricao;

    SituacaoAluno(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}