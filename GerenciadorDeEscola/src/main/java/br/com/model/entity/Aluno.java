package br.com.model.entity;

import java.util.List;

public class Aluno extends Pessoa{
    private int matricula;
    private String serie;
    private Turma turma;
    private String situacao;
    private List<Responsavel> responsavel;


    public Aluno(String nome, String cpf, String dataNascimentoTexto, String serie) {
        super(nome, cpf, dataNascimentoTexto);
        this.matricula = matricula;
        this.serie = serie;
    }

    public int getMatricula() {
        return matricula;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }
}
