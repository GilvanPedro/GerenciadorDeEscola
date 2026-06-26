package br.com.controller;

import br.com.model.entity.Aluno;
import br.com.model.enums.SituacaoAluno;
import br.com.service.AlunoService;
import br.com.util.BuscarAluno;
import br.com.util.GeradorMatricula;

import java.util.List;

public class AlunoController {
    private final AlunoService alunoService = new AlunoService();
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

        System.out.println("Aluno cadastrado com sucesso!");
    }

    // Lista os alunos salvos
    public void listarAlunos() {

        List<Aluno> alunos = alunoService.listarAlunos();

        if (alunos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: Nenhum aluno cadastrado"
            );
        }

        for (Aluno aluno : alunos) {
            System.out.println("--------------------");
            System.out.println("Matrícula: " + aluno.getMatricula());
            System.out.println("Nome: " + aluno.getNome());
            System.out.println("CPF: " + aluno.getCpf());
            System.out.println("Série: " + aluno.getSerie());
            System.out.println("Situação: " + aluno.getSituacao());
        }
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
            throw new IllegalArgumentException (
                    "Erro: Aluno com matrícula " + matricula + " não encontrado."
            );
        }

        Aluno alunoAtualizado = new Aluno(
                matricula,
                nome,
                alunoExistente.getCpf(), // CPF não pode ser alterado
                alunoExistente.getDataNascimentoFormatada(), // data não pode ser alterada
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
