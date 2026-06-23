package br.com.service;

import br.com.dao.ProfessorDAO;
import br.com.model.entity.Professor;
import br.com.util.GerarId;
import br.com.util.ValidarCpf;

import java.util.List;

public class ProfessorService {
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    public void adicionar(Professor professor) {
        List<Professor> professores = professorDAO.listarProfessor();

        // Validar cpf
        if (!ValidarCpf.validarCpf(professor.getCpf())) {
            throw new IllegalArgumentException("O CPF não é válido");
        }

        // Verificar duplicidade cpf
        if (ValidarCpf.validarDuplicidadeCPF(professores, professor.getCpf())) {
            throw new IllegalArgumentException("O CPF já está cadastrado!");
        }

        // Gera o ID só após passar nas verificações
        int id = GerarId.gerarNovoId(professorDAO.caminhoUltimoIdentificador());
        professor.setId(id);

        // Adiciona e salva
        professores.add(professor);
        professorDAO.salvar(professores);
    }

    public List<Professor> listarProfessores(){
        return professorDAO.listarProfessor();
    }
}
