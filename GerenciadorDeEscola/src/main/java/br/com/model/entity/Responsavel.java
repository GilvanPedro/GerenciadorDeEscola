package br.com.model.entity;

public class Responsavel extends Pessoa{
    private String endereco;
    private String telefone;

    public Responsavel(String nome, String cpf, String dataNascimentoTexto, String endereco, String telefone) {
        super(nome, cpf, dataNascimentoTexto);
        this.endereco = endereco;
        this.telefone = telefone;
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
}
