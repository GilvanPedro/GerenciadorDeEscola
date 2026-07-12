package br.com.model.dto;

import br.com.model.entity.Aluno;

import java.util.List;

public class BoletimExibicao {
    private final Aluno aluno;
    private final List<LinhaBoletim> linhas;

    public BoletimExibicao(Aluno aluno, List<LinhaBoletim> linhas) {
        this.aluno = aluno;
        this.linhas = linhas;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public List<LinhaBoletim> getLinhas() {
        return linhas;
    }
}