package br.com.model.entity;

import br.com.model.Identificador;

import java.util.List;

public class Responsavel extends Pessoa implements Identificador {
    private int id;
    private String endereco;
    private String telefone;
    private List<Integer> alunosId;

    public Responsavel(int id, String nome, String cpf, String dataNascimentoTexto, String endereco, String telefone, List<Integer> alunosId) {
        super(nome, cpf, dataNascimentoTexto);
        this.id = id;
        this.endereco = endereco;
        this.telefone = telefone;
        this.alunosId = alunosId;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Integer> getAlunosId() {
        return alunosId;
    }

    public void setAlunosId(List<Integer> alunosId) {
        this.alunosId = alunosId;
    }
}
