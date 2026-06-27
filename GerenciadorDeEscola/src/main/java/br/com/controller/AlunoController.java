package br.com.controller;

import br.com.dao.ResponsavelDAO;
import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;
import br.com.model.enums.SituacaoAluno;
import br.com.service.AlunoService;
import br.com.util.BuscaPorId;
import br.com.util.BuscarAluno;
import br.com.util.GeradorMatricula;

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

    // Lista os alunos salvos
    public void listarAlunos() {
        List<Aluno> alunos = alunoService.listarAlunos();

        if (alunos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: Nenhum aluno cadastrado"
            );
        }

        List<Responsavel> todosResponsaveis = responsavelDAO.listarResponsaveis();

        for (Aluno aluno : alunos) {
            System.out.println("====================");
            System.out.println("Matrícula:    " + aluno.getMatricula());
            System.out.println("Nome:         " + aluno.getNome());
            System.out.println("CPF:          " + aluno.getCpf());
            System.out.println("Nascimento:   " + aluno.getDataNascimentoFormatada());
            System.out.println("Série:        " + aluno.getSerie());
            System.out.println("Situação:     " + aluno.getSituacao());

            List<Integer> ids = aluno.getResponsavelId();
            System.out.println("---- Responsáveis ----");
            for (int i = 0; i < ids.size(); i++) {
                Responsavel r = BuscaPorId.buscarPorId(todosResponsaveis, ids.get(i));
                if (r != null) {
                    System.out.println("  " + (i + 1) + ". " + r.getNome());
                    System.out.println("     CPF:        " + r.getCpf());
                    System.out.println("     Nascimento: " + r.getDataNascimentoFormatada());
                    System.out.println("     Endereço:   " + r.getEndereco());
                    System.out.println("     Telefone:   " + r.getTelefone());
                } else {
                    System.out.println("  " + (i + 1) + ". Responsável não encontrado (id: " + ids.get(i) + ")");
                }
            }
        }
        System.out.println("====================");
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