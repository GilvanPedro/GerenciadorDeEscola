package br.com.model.dto;

import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;

import java.util.List;

public class AlunoExibicao {
    private final Aluno aluno;
    private final List<Responsavel> responsaveis;

    public AlunoExibicao(Aluno aluno, List<Responsavel> responsaveis) {
        this.aluno = aluno;
        this.responsaveis = responsaveis;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public List<Responsavel> getResponsaveis() {
        return responsaveis;
    }
}