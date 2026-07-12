package br.com.model.entity;

import br.com.model.Identificador;

public class Nota implements Identificador {
    private int id;
    private int alunoMatricula;
    private int disciplinaId;
    private int bimestre;
    private double valor;

    public Nota(int id, int alunoMatricula, int disciplinaId, int bimestre, double valor) {
        this.id = id;
        this.alunoMatricula = alunoMatricula;
        this.disciplinaId = disciplinaId;
        this.bimestre = bimestre;
        this.valor = valor;
    }

    // Construtor sem id, usado antes de gerar o id no service
    public Nota(int alunoMatricula, int disciplinaId, int bimestre, double valor) {
        this.alunoMatricula = alunoMatricula;
        this.disciplinaId = disciplinaId;
        this.bimestre = bimestre;
        this.valor = valor;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlunoMatricula() {
        return alunoMatricula;
    }

    public void setAlunoMatricula(int alunoMatricula) {
        this.alunoMatricula = alunoMatricula;
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return id + ";" +
                alunoMatricula + ";" +
                disciplinaId + ";" +
                bimestre + ";" +
                valor;
    }
}