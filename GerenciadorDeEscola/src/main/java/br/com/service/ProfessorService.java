package br.com.service;

import br.com.dao.ProfessorDAO;
import br.com.model.entity.Professor;
import br.com.util.GerarId;
import br.com.util.ValidarCpf;
import br.com.util.ValidarEmail;

import java.util.List;

public class ProfessorService {
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    // Verificações e salvamento do professor
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

        // Validar email
        if(!ValidarEmail.validarEmail(professor.getEmail())){
            throw new IllegalArgumentException("O email está inválido!");
        }

        // O professor precisa ter no mínimo 1 disciplina
        if (professor.getDisciplina() == null || professor.getDisciplina().isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: Professor não pode estar sem disciplinas."
            );
        }

        // Gera o ID só após passar nas verificações
        int id = GerarId.gerarNovoId(professorDAO.caminhoUltimoIdentificador());
        professor.setId(id);

        // Adiciona e salva
        professores.add(professor);
        professorDAO.salvar(professores);
    }

    // Listar Professores
    public List<Professor> listarProfessores(){
        return professorDAO.listarProfessor();
    }

    // Verificações da edição do professor e salvamento dele
    public void editarProfessor(Professor professorAtualizado){
        if (professorAtualizado.getNome() == null || professorAtualizado.getNome().isBlank()) {
            throw new IllegalArgumentException(
                    "Erro: nome do professor não pode ser vazio."
            );
        }

        // Validar email
        if(!ValidarEmail.validarEmail(professorAtualizado.getEmail())){
            throw new IllegalArgumentException("O email está inválido!");
        }

        // O professor precisa ter no mínimo 1 disciplina
        if (professorAtualizado.getDisciplina() == null || professorAtualizado.getDisciplina().isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: Professor não pode estar sem disciplinas."
            );
        }

        professorDAO.editar(professorAtualizado);
    }

    // Exclui o professor se ele existir
    public void excluirProfessor(int id){
        List<Professor> professores = professorDAO.listarProfessor();

        boolean existe = professores.stream().anyMatch(p -> p.getId() == id);

        if(!existe){
            throw new IllegalArgumentException(
                    "Erro: professor com id: " + id + " não encontrado."
            );
        }

        professorDAO.excluir(id);
    }
}
