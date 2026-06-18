package br.com.model.entity;

import br.com.model.enums.NivelEnsino;

public class Disciplina {
    private String disciplina;
    private NivelEnsino nivelEnsino;

    public Disciplina(String disciplina, NivelEnsino nivelEnsino) {
        this.disciplina = disciplina;
        this.nivelEnsino = nivelEnsino;
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
