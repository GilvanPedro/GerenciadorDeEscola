package br.com.model.entity;

import java.util.List;

public class Professor {
    private int id;
    private List<Integer> disciplinaId;
    private List<Integer> vinculoId;
    private String telefone;
    private String endereco;
    private String email;

    public Professor(int id, List<Integer> disciplina, List<Integer> vinculo, String telefone, String endereco, String email) {
        this.id = id;
        this.disciplinaId = disciplina;
        this.vinculoId = vinculo;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getDisciplina() {
        return disciplinaId;
    }

    public void setDisciplina(List<Integer> disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public List<Integer> getVinculo() {
        return vinculoId;
    }

    public void setVinculo(List<Integer> vinculoId) {
        this.vinculoId = vinculoId;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
