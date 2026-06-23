package br.com.controller;

import br.com.dao.ProfessorDAO;
import br.com.model.entity.Aluno;
import br.com.model.entity.Professor;
import br.com.service.ProfessorService;
import br.com.util.GerarId;

import java.util.List;

public class ProfessorController {
    private ProfessorService professorService = new ProfessorService();

    private ProfessorDAO professorDAO = new ProfessorDAO();

    // Listar Professores
    public void listar() {
        List<Professor> professores = professorService.listarProfessores();

        if (professores.isEmpty()) {
            System.out.println("Nenhum professor cadastrado.");
            return;
        }

        for (Professor professor : professores) {
            System.out.println("--------------------");
            System.out.println("ID:           " + professor.getId());
            System.out.println("Nome:         " + professor.getNome());
            System.out.println("CPF:          " + professor.getCpf());
            System.out.println("Nascimento:   " + professor.getDataNascimentoFormatada());
            System.out.println("Disciplinas:  " + professor.getDisciplina());
            System.out.println("Vínculos:     " + professor.getVinculo());
            System.out.println("Telefone:     " + professor.getTelefone());
            System.out.println("Endereço:     " + professor.getEndereco());
            System.out.println("Email:        " + professor.getEmail());
        }
    }

    // Mandando o professor para o service para as verificações finais antes de adicionar
    public void adicionarProfessor(
        String nome,
        String cpf,
        String dataNascimento,
        List<Integer> disciplinasId,
        List<Integer> vinculosId,
        String telefone,
        String endereco,
        String email
    ){
        Professor professor = new Professor(
                nome,
                cpf,
                dataNascimento,
                disciplinasId,
                vinculosId,
                telefone,
                endereco,
                email
        );

        professorService.adicionar(professor);
    }
}
