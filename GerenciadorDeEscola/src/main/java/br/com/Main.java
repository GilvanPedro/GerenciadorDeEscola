package br.com;

import br.com.controller.AlunoController;
import br.com.controller.ProfessorController;
import br.com.dao.ProfessorDAO;
import br.com.model.enums.SituacaoAluno;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ProfessorController professorController = new ProfessorController();
        ProfessorDAO dao = new ProfessorDAO();

//        professorController.adicionarProfessor(
//                "Carlos Eduardo Mendes",
//                "52998224725",
//                "12/04/1994",
//                List.of(1, 3, 5),
//                List.of(1),
//                "(62) 99876-5432",
//                "Rua das Palmeiras, 123, Setor Bueno, Goiânia - GO",
//                "carlos.mendes@escola.com.br"
//        );

        professorController.editarProfessor(
                3,
                "Carlos Eduardo",
                "12/04/1998",
                List.of(1, 3, 9),
                List.of(1, 7),
                "(62) 99876-5432",
                "Rua das Palmeiras, 123, Setor Bueno, Goiânia - GO",
                "carlos.mendes@gmail.com"
        );

        System.out.println("\n======= Lista de Professores =======");
        professorController.listar();
    }
}