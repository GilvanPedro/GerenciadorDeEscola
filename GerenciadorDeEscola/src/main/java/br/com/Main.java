package br.com;

import br.com.controller.AlunoController;
import br.com.controller.ResponsavelController;

public class Main {
    public static void main(String[] args) {

        AlunoController alunoController = new AlunoController();
        ResponsavelController responsavelController = new ResponsavelController();

        responsavelController.adicionarAlunoAoResponsavel(5, 26007);
        responsavelController.adicionarAlunoAoResponsavel(7, 26007);

        System.out.println("\n===== LISTANDO RESPONSÁVEIS =====");

        try {
            responsavelController.listarResponsaveis();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n===== LISTANDO ALUNOS =====");

        try {
            alunoController.listarAlunos();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}