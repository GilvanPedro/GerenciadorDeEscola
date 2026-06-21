package br.com.controller;

import br.com.model.entity.Aluno;
import br.com.model.enums.SituacaoAluno;
import br.com.service.AlunoService;
import br.com.util.GeradorMatricula;

import java.util.List;

public class AlunoController {
    private final AlunoService alunoService = new AlunoService();
    private GeradorMatricula geradorMatricula = new GeradorMatricula();

    // Adiciona um aluno, enviando para o service para que as verificações sejam feitas antes que ele seja salvo
    public void adicionar(
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

        alunoService.adicionar(aluno);

        System.out.println("Aluno cadastrado com sucesso!");
    }

    // Lista os alunos salvos
    public void listar() {

        List<Aluno> alunos = alunoService.listar();

        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
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
            List<Integer> responsaveisId
    ){

        // CRIAR UM VERIFICADOR PARA VER SE A MATRÍCULA EXISTE

        // CRIAR UM MÉTODO PARA ENCONTRAR OS ALUNOS NOS ARQUIVOS SALVOS
    }
}
