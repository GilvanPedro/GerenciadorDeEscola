package br.com.model.entity;

import java.util.List;

public class Professor {
    private List<Disciplina> disciplina;
    private List<Vinculo> vinculo;
    private String telefone;
    private String endereco;
    private String email;

    public Professor(List<Disciplina> disciplina, List<Vinculo> vinculo, String telefone, String endereco, String email) {
        this.disciplina = disciplina;
        this.vinculo = vinculo;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
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
