package br.com;

import br.com.controller.ProfessorController;
import br.com.dao.ProfessorDAO;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ProfessorController professorController = new ProfessorController();
        ProfessorDAO dao = new ProfessorDAO();

        System.out.println("\n======= Lista de Professores =======");
        professorController.listar();
    }
}