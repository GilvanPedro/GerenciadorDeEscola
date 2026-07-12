package br.com.model.dto;

import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;

import java.util.List;

public class ResponsavelExibicao {
    private final Responsavel responsavel;
    private final List<Aluno> alunos;

    public ResponsavelExibicao(Responsavel responsavel, List<Aluno> alunos) {
        this.responsavel = responsavel;
        this.alunos = alunos;
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }
}