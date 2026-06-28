package br.com.model.entity;

import br.com.model.Identificador;
import br.com.model.enums.NivelEnsino;
import br.com.model.enums.Series;
import br.com.model.enums.Turnos;

import java.util.List;

public class Turma implements Identificador {
    private int id;
    private Series serie;
    private Turnos turno;
    private NivelEnsino nivelEnsino;
    private int anoLetivo;
    private List<Integer> alunosId;

    public Turma(int id, Series serie, Turnos turno, NivelEnsino nivelEnsino, int anoLetivo, List<Integer> alunosId) {
        this.id = id;
        this.serie = serie;
        this.turno = turno;
        this.nivelEnsino = nivelEnsino;
        this.anoLetivo = anoLetivo;
        this.alunosId = alunosId;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Series getSerie() {
        return serie;
    }

    public void setSerie(Series serie) {
        this.serie = serie;
    }

    public Turnos getTurno() {
        return turno;
    }

    public void setTurno(Turnos turno) {
        this.turno = turno;
    }

    public NivelEnsino getNivelEnsino() {
        return nivelEnsino;
    }

    public void setNivelEnsino(NivelEnsino nivelEnsino) {
        this.nivelEnsino = nivelEnsino;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public List<Integer> getAlunosId() {
        return alunosId;
    }

    public void setAlunosId(List<Integer> alunosId) {
        this.alunosId = alunosId;
    }

    // FAZER O TO STRING
}
