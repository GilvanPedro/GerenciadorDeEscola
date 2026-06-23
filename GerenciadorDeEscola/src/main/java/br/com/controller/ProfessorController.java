package br.com.controller;

import br.com.dao.ProfessorDAO;
import br.com.model.entity.Professor;
import br.com.util.GerarId;

import java.time.LocalDate;
import java.util.List;

public class ProfessorController {
    private ProfessorDAO professorDAO = new ProfessorDAO();

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
        int id = GerarId.gerarNovoId(professorDAO.caminhoUltimoIdentificador());

        Professor professor = new Professor(
                nome,
                cpf,
                dataNascimento,
                id,
                disciplinasId,
                vinculosId,
                telefone,
                endereco,
                email
        );

        professorService.adicionar(professor);
    }
}
