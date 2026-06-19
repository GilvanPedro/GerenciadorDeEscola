package br.com.model.entity;

import br.com.model.enums.NivelEnsino;

public class Disciplina {
    private int id;
    private String disciplina;
    private NivelEnsino nivelEnsino;

    public Disciplina(int id, String disciplina, NivelEnsino nivelEnsino) {
        this.id = id;
        this.disciplina = disciplina;
        this.nivelEnsino = nivelEnsino;
    }

    public int getId() {
        return id;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public NivelEnsino getNivelEnsino() {
        return nivelEnsino;
    }

    public void setNivelEnsino(NivelEnsino nivelEnsino) {
        this.nivelEnsino = nivelEnsino;
    }
}
