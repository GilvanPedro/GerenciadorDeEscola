package br.com.controller;

import br.com.dao.ResponsavelDAO;
import br.com.model.dto.AlunoExibicao;
import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;
import br.com.model.enums.SituacaoAluno;
import br.com.service.AlunoService;
import br.com.util.BuscaPorId;
import br.com.util.BuscarAluno;
import br.com.util.GeradorMatricula;

import java.util.ArrayList;
import java.util.List;

public class AlunoController {
    private final AlunoService alunoService = new AlunoService();
    private final ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    private GeradorMatricula geradorMatricula = new GeradorMatricula();

    // Adiciona um aluno, enviando para o service para que as verificações sejam feitas antes que ele seja salvo
    public void adicionarAluno(
            String nome,
            String cpf,
            String dataNascimento,
            String serie,
            int turmaId,
            SituacaoAluno situacao,
            List<Integer> responsaveis
    ) {
        int matricula = geradorMatricula.gerarMatricula();

        Aluno aluno = new Aluno(
                matricula,
                nome,
                cpf,
                dataNascimento,
                serie,
                turmaId,
                situacao,
                responsaveis
        );

        alunoService.adicionarAluno(aluno);
    }

    // Monta a lista de alunos pronta para exibição, já com os responsáveis resolvidos
    public List<AlunoExibicao> listarAlunos() {
        List<Aluno> alunos = alunoService.listarAlunos();

        List<Responsavel> todosResponsaveis = responsavelDAO.listarResponsaveis();
        List<AlunoExibicao> exibicoes = new ArrayList<>();

        for (Aluno aluno : alunos) {
            List<Responsavel> responsaveisDoAluno = new ArrayList<>();

            for (int responsavelId : aluno.getResponsavelId()) {
                Responsavel r = BuscaPorId.buscarPorId(todosResponsaveis, responsavelId);
                if (r != null) {
                    responsaveisDoAluno.add(r);
                }
            }

            exibicoes.add(new AlunoExibicao(aluno, responsaveisDoAluno));
        }

        return exibicoes;
    }

    // Editar um aluno já cadastrado
    public void editarAluno(
            int matricula,
            String nome,
            String serie,
            int turmaId,
            SituacaoAluno situacao,
            List<Integer> responsaveisId
    ) {
        Aluno alunoExistente = BuscarAluno.buscarPorMatricula(matricula);

        if (alunoExistente == null) {
            throw new IllegalArgumentException(
                    "Erro: Aluno com matrícula " + matricula + " não encontrado."
            );
        }

        Aluno alunoAtualizado = new Aluno(
                matricula,
                nome,
                alunoExistente.getCpf(),
                alunoExistente.getDataNascimentoFormatada(),
                serie,
                turmaId,
                situacao,
                responsaveisId
        );

        alunoService.editarAluno(alunoAtualizado);
    }

    // Manda para o service para excluir um aluno pelo número de matrícula
    public void excluirAluno(int matricula) {
        alunoService.excluirAluno(matricula);
    }
}