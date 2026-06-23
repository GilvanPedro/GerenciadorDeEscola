package br.com;

import br.com.controller.AlunoController;
import br.com.controller.ProfessorController;
import br.com.dao.ProfessorDAO;
import br.com.model.enums.SituacaoAluno;
import br.com.service.ProfessorService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        AlunoController controller = new AlunoController();

        controller.editarAluno(
                26002,
                "João Silva",        // nome
                "9º Ano",           // série
                2,                  // turmaId
                SituacaoAluno.ATIVO, // situação
                List.of(1, 2)       // ids dos responsáveis
        );

        controller.excluirAluno(26002);

        controller.listar();


        ProfessorController professorController = new ProfessorController();
        ProfessorDAO dao = new ProfessorDAO();



        professorController.listar();
    }
}