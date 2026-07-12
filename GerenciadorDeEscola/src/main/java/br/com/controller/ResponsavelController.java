package br.com.controller;

import br.com.dao.ResponsavelDAO;
import br.com.model.dto.ResponsavelExibicao;
import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;
import br.com.service.ResponsavelService;
import br.com.util.BuscaPorId;
import br.com.util.BuscarAluno;

import java.util.ArrayList;
import java.util.List;

public class ResponsavelController {

    private final ResponsavelService responsavelService = new ResponsavelService();
    private final ResponsavelDAO responsavelDAO = new ResponsavelDAO();

    // Monta a lista de responsáveis pronta para exibição, já com os alunos vinculados resolvidos
    public List<ResponsavelExibicao> listarResponsaveis() {
        List<Responsavel> responsaveis = responsavelService.listarResponsaveis();

        List<ResponsavelExibicao> exibicoes = new ArrayList<>();

        for (Responsavel responsavel : responsaveis) {
            List<Aluno> alunosDoResponsavel = new ArrayList<>();

            List<Integer> matriculas = responsavel.getAlunosId();
            if (matriculas != null) {
                for (int matricula : matriculas) {
                    Aluno aluno = BuscarAluno.buscarPorMatricula(matricula);
                    if (aluno != null) {
                        alunosDoResponsavel.add(aluno);
                    }
                }
            }

            exibicoes.add(new ResponsavelExibicao(responsavel, alunosDoResponsavel));
        }

        return exibicoes;
    }

    // Adicionar um responsável e enviar para as verificações no service
    public void adicionarResponsavel(
            String nome,
            String cpf,
            String dataNascimento,
            String endereco,
            String telefone,
            List<Integer> alunosId
    ) {
        Responsavel responsavel = new Responsavel(
                nome,
                cpf,
                dataNascimento,
                endereco,
                telefone,
                alunosId
        );

        responsavelService.adicionarResponsavel(responsavel);
    }

    // Editar o responsável e enviar para as verificações no service
    public void editarResponsavel(
            int id,
            String nome,
            String endereco,
            String telefone,
            List<Integer> alunosId
    ) {
        Responsavel responsavelExistente = BuscaPorId.buscarPorId(responsavelDAO.listarResponsaveis(), id);

        if (responsavelExistente == null) {
            throw new IllegalArgumentException(
                    "Erro: Responsável com o id: " + id + " não encontrado."
            );
        }

        Responsavel responsavelAtualizado = new Responsavel(
                id,
                nome,
                responsavelExistente.getCpf(),
                responsavelExistente.getDataNascimentoFormatada(),
                endereco,
                telefone,
                alunosId
        );

        responsavelService.editarResponsavel(responsavelAtualizado);
    }

    // Vincula um aluno já cadastrado a um responsável já cadastrado
    public void adicionarAlunoAoResponsavel(int responsavelId, int matriculaAluno) {
        responsavelService.adicionarAlunoAoResponsavel(responsavelId, matriculaAluno);
    }

    // Manda para o service excluir o responsável
    public void excluirResponsavel(int id) {
        responsavelService.excluirResponsavel(id);
    }
}