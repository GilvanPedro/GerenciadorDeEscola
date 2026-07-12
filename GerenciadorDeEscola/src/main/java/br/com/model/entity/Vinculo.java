package br.com.model.entity;

import br.com.model.Identificador;
import br.com.model.enums.DiaSemana;
import br.com.model.enums.PeriodoAula;

import java.util.List;

public class Vinculo implements Identificador {
    private int id;
    private int professorId;
    private List<Integer> disciplinaId;
    private int turmaId;
    private DiaSemana diaSemana;
    private PeriodoAula periodoAula;

    public Vinculo(int id, int professorId, List<Integer> disciplinaId, int turmaId, DiaSemana diaSemana, PeriodoAula periodoAula) {
        this.id = id;
        this.professorId = professorId;
        this.disciplinaId = disciplinaId;
        this.turmaId = turmaId;
        this.diaSemana = diaSemana;
        this.periodoAula = periodoAula;
    }

    // Construtor sem id, usado antes de gerar o id no service
    public Vinculo(int professorId, List<Integer> disciplinaId, int turmaId, DiaSemana diaSemana, PeriodoAula periodoAula) {
        this.professorId = professorId;
        this.disciplinaId = disciplinaId;
        this.turmaId = turmaId;
        this.diaSemana = diaSemana;
        this.periodoAula = periodoAula;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public List<Integer> getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(List<Integer> disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public int getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(int turmaId) {
        this.turmaId = turmaId;
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

    @Override
    public String toString() {
        String disciplinas = (disciplinaId == null || disciplinaId.isEmpty())
                ? "vazio"
                : disciplinaId.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("vazio");

        return id + ";" +
                professorId + ";" +
                disciplinas + ";" +
                turmaId + ";" +
                diaSemana + ";" +
                periodoAula;
    }
}