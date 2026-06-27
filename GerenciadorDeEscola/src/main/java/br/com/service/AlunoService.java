package br.com.service;

import br.com.controller.ResponsavelController;
import br.com.dao.AlunoDAO;
import br.com.dao.ResponsavelDAO;
import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;
import br.com.util.BuscaPorId;
import br.com.util.ValidarCpf;

import java.util.List;

public class AlunoService {

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    private final ResponsavelService responsavelService = new ResponsavelService();

    // Verificar as informações e depois salvar no arquivo
    public void adicionarAluno(Aluno aluno) {

        List<Aluno> alunos = alunoDAO.listarAluno();

        // Validar se a matrícula está repetida
        for (Aluno a : alunos) {
            if (a.getMatricula() == aluno.getMatricula()) {
                throw new IllegalArgumentException(
                        "Erro: A matrícula já está cadastrada, tente novamente, se o erro persistir, entre em contato com algum desenvolvedor"
                );
            }
        }

        // Validar o Cpf
        if (!ValidarCpf.validarCpf(aluno.getCpf())) {
            throw new IllegalArgumentException(
                    "Erro: O CPF é inválido"
            );
        }

        // Verificar por Cpf já cadastrado
        if (ValidarCpf.validarDuplicidadeCPF(alunos, aluno.getCpf())) {
            throw new IllegalArgumentException(
                    "Erro: O CPF já está cadastrado"
            );
        }

        // Validar se o aluno tem responsáveis cadastrados
        if (aluno.getResponsavelId() == null || aluno.getResponsavelId().isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: aluno deve ter ao menos um responsável."
            );
        }

        // Verificar se cada responsável informado realmente existe
        List<Responsavel> responsaveis = responsavelDAO.listarResponsaveis();
        for (int responsavelId : aluno.getResponsavelId()) {
            Responsavel encontrado = BuscaPorId.buscarPorId(responsaveis, responsavelId);
            if (encontrado == null) {
                throw new IllegalArgumentException(
                        "Erro: Responsável com id " + responsavelId + " não encontrado."
                );
            }
        }

        // Salvamento dos Alunos
        alunos.add(aluno);
        alunoDAO.salvar(alunos);
        alunoDAO.salvarUltimaMatricula(aluno.getMatricula());

        // Vincula o aluno a cada responsável informado
        for (int responsavelId : aluno.getResponsavelId()) {
            responsavelService.adicionarAlunoAoResponsavel(responsavelId, aluno.getMatricula());
        }
    }

    // Listar os Alunos
    public List<Aluno> listarAlunos() {
        return alunoDAO.listarAluno();
    }

    // Editar o aluno
    public void editarAluno(Aluno alunoAtualizado) {
        if (alunoAtualizado.getNome() == null || alunoAtualizado.getNome().isBlank()) {
            throw new IllegalArgumentException(
                    "Erro: nome do aluno não pode ser vazio."
            );
        }

        if (alunoAtualizado.getSerie() == null || alunoAtualizado.getSerie().isBlank()) {
            throw new IllegalArgumentException(
                    "Erro: série não pode ser vazia."
            );
        }

        if (alunoAtualizado.getResponsavelId() == null || alunoAtualizado.getResponsavelId().isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: aluno deve ter ao menos um responsável."
            );
        }

        // Verificar se cada responsável informado realmente existe
        List<Responsavel> responsaveis = responsavelDAO.listarResponsaveis();
        for (int responsavelId : alunoAtualizado.getResponsavelId()) {
            Responsavel encontrado = BuscaPorId.buscarPorId(responsaveis, responsavelId);
            if (encontrado == null) {
                throw new IllegalArgumentException(
                        "Erro: Responsável com id " + responsavelId + " não encontrado."
                );
            }
        }

        alunoDAO.editar(alunoAtualizado);
    }

    // Exclui o aluno se ele existir
    public void excluirAluno(int matricula) {
        List<Aluno> alunos = alunoDAO.listarAluno();

        boolean existe = alunos.stream()
                .anyMatch(a -> a.getMatricula() == matricula);

        if (!existe) {
            throw new IllegalArgumentException(
                    "Erro: aluno com matrícula: " + matricula + " não encontrado."
            );
        }

        alunoDAO.excluir(matricula);
    }
}