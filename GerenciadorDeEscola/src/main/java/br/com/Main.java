package br.com;

import br.com.controller.ProfessorController;
import br.com.dao.ProfessorDAO;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ProfessorController professorController = new ProfessorController();
        ProfessorDAO dao = new ProfessorDAO();

        professorController.editarProfessor(
                3,
                "Carlos Eduardo",
                "12/04/1998",
                List.of(7),
                List.of(),
                "(62) 99876-5432",
                "Rua das Palmeiras, 123, Setor Bueno, Goiânia - GO",
                "carlos.mendes@gmail.com"
        );

        System.out.println("\n======= Lista de Professores =======");
        professorController.listar();
    }
}