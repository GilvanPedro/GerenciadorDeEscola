package br.com.model.enums;

public enum SituacaoBoletim {
    APROVADO("Aprovado"),
    RECUPERACAO("Recuperação"),
    REPROVADO("Reprovado"),
    EM_ANDAMENTO("Em andamento");

    private String descricao;

    SituacaoBoletim(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}