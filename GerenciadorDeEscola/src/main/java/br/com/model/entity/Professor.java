package br.com.model.entity;

import java.util.List;

public class Professor extends Pessoa{
    private int id;
    private List<Integer> disciplinaId;
    private List<Integer> vinculoId;
    private String telefone;
    private String endereco;
    private String email;

    public Professor(String nome, String cpf, String dataNascimentoTexto, int id, List<Integer> disciplinaId, List<Integer> vinculoId, String telefone, String endereco, String email) {
        super(nome, cpf, dataNascimentoTexto);
        this.id = id;
        this.disciplinaId = disciplinaId;
        this.vinculoId = vinculoId;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
    }

    public Professor(String nome, String cpf, String dataNascimentoTexto, List<Integer> disciplinaId, List<Integer> vinculoId, String telefone, String endereco, String email) {
        super(nome, cpf, dataNascimentoTexto);
        this.disciplinaId = disciplinaId;
        this.vinculoId = vinculoId;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        String disciplinas = disciplinaId.stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));

        String vinculos = vinculoId.stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));

        return getNome() + ";" + getCpf() + ";" + getDataNascimentoFormatada() + ";"
                + id + ";" + disciplinas + ";" + vinculos + ";"
                + telefone + ";" + endereco + ";" + email;
    }
}
