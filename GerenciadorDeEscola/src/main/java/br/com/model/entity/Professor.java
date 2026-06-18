package br.com.model.entity;

import java.util.List;

public class Professor {
    private List<Disciplina> disciplina;
    private List<Vinculo> vinculo;

    public Professor(List<Disciplina> disciplina, List<Vinculo> vinculo) {
        this.disciplina = disciplina;
        this.vinculo = vinculo;
    }

    public List<Disciplina> getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(List<Disciplina> disciplina) {
        this.disciplina = disciplina;
    }

    public List<Vinculo> getVinculo() {
        return vinculo;
    }

    public void setVinculo(List<Vinculo> vinculo) {
        this.vinculo = vinculo;
    }
}
