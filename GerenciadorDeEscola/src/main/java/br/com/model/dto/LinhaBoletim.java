package br.com.model.dto;

import br.com.model.enums.SituacaoBoletim;

public class LinhaBoletim {
    private int disciplinaId;
    private String disciplinaNome;
    private Double nota1;
    private Double nota2;
    private Double nota3;
    private Double nota4;
    private Double mediaFinal;
    private SituacaoBoletim situacao;

    public LinhaBoletim(int disciplinaId, String disciplinaNome, Double nota1, Double nota2, Double nota3, Double nota4, Double mediaFinal, SituacaoBoletim situacao) {
        this.disciplinaId = disciplinaId;
        this.disciplinaNome = disciplinaNome;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
        this.nota4 = nota4;
        this.mediaFinal = mediaFinal;
        this.situacao = situacao;
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public String getDisciplinaNome() {
        return disciplinaNome;
    }

    public Double getNota1() {
        return nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public Double getNota3() {
        return nota3;
    }

    public Double getNota4() {
        return nota4;
    }

    public Double getMediaFinal() {
        return mediaFinal;
    }

    public SituacaoBoletim getSituacao() {
        return situacao;
    }

    // Formata uma nota para exibição, mostrando "-" quando ainda não foi lançada
    private String formatarNota(Double nota) {
        return nota == null ? "-" : String.valueOf(nota);
    }

    @Override
    public String toString() {
        return disciplinaNome + ": " +
                "1ºBim=" + formatarNota(nota1) + " | " +
                "2ºBim=" + formatarNota(nota2) + " | " +
                "3ºBim=" + formatarNota(nota3) + " | " +
                "4ºBim=" + formatarNota(nota4) + " | " +
                "Média=" + formatarNota(mediaFinal) + " | " +
                "Situação=" + situacao.getDescricao();
    }
}