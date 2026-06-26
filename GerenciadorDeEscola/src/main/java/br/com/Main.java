package br.com;

import br.com.controller.DisciplinaController;

public class Main {

    public static void main(String[] args) {

        DisciplinaController controller = new DisciplinaController();

        System.out.println();
        System.out.println("========================================");
        System.out.println("     LISTANDO DISCIPLINAS CADASTRADAS");
        System.out.println("========================================");

        // Listagem de todas as disciplinas
        controller.listarDisciplina();
    }
}