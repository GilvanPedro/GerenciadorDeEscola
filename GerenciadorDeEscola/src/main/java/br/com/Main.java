package br.com;

import br.com.controller.AlunoController;
import br.com.model.enums.SituacaoAluno;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        AlunoController controller = new AlunoController();

        controller.adicionar(
                "Marcos Henrrique",
                "71182759050",
                "20/06/2008",
                "3º Ano",
                1,
                SituacaoAluno.ATIVO,
                List.of(1, 2)
        );

        controller.listar();
    }
}