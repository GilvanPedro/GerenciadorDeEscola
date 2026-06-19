package br.com.model.entity;

import br.com.model.enums.NivelEnsino;
import br.com.model.enums.Series;
import br.com.model.enums.Turnos;

public class Turma {
    private int id;
    private Series serie;
    private Turnos turno;
    private NivelEnsino nivelEnsino;
    private int anoLetivo;

    public Turma(int id, Series serie, Turnos turno, NivelEnsino nivelEnsino, int anoLetivo) {
        this.id = id;
        this.serie = serie;
        this.turno = turno;
        this.nivelEnsino = nivelEnsino;
        this.anoLetivo = anoLetivo;
    }

    public int getId() {
        return id;
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
}
