package br.com;

import br.com.model.entity.Pessoa;

public class Main {

    public static void main(String[] args) {

        Pessoa pessoa = new Pessoa(
                "Gilvan Pedro",
                "123.456.789-00",
                "17/06/2006"
        );

        System.out.println("Pessoa cadastrada com sucesso!");
        System.out.println("Nome: " + pessoa.getNome());
        System.out.println("CPF: " + pessoa.getCpf());
        System.out.println("Data de Nascimento: " + pessoa.getDataNascimentoFormatada());
    }
}