package br.com.model.entity;

import br.com.model.enums.DiaSemana;
import br.com.model.enums.PeriodoAula;

public class Vinculo {
    private int id;
    private Professor professor;
    private Disciplina disciplina;
    private Turma turma;
    private DiaSemana diaSemana;
    private PeriodoAula periodoAula;

    public Vinculo(int id, Professor professor, Disciplina disciplina, Turma turma, DiaSemana diaSemana, PeriodoAula periodoAula) {
        this.id = id;
        this.professor = professor;
        this.disciplina = disciplina;
        this.turma = turma;
        this.diaSemana = diaSemana;
        this.periodoAula = periodoAula;
    }

    public int getId() {
        return id;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public PeriodoAula getPeriodoAula() {
        return periodoAula;
    }

    public void setPeriodoAula(PeriodoAula periodoAula) {
        this.periodoAula = periodoAula;
    }
}
