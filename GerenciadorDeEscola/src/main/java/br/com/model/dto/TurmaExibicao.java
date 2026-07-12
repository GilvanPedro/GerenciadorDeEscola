package br.com.model.dto;

import br.com.model.entity.Aluno;
import br.com.model.entity.Turma;

import java.util.List;

public class TurmaExibicao {
    private final Turma turma;
    private final List<Aluno> alunos;

    public TurmaExibicao(Turma turma, List<Aluno> alunos) {
        this.turma = turma;
        this.alunos = alunos;
    }

    public Turma getTurma() {
        return turma;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }
}