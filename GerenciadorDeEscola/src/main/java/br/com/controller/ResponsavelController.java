package br.com.controller;

import br.com.dao.ResponsavelDAO;
import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;
import br.com.service.ResponsavelService;
import br.com.util.BuscaPorId;
import br.com.util.BuscarAluno;

import java.util.List;

public class ResponsavelController {

    private final ResponsavelService responsavelService = new ResponsavelService();
    private final ResponsavelDAO responsavelDAO = new ResponsavelDAO();

    // Listar os Responsáveis
    public void listarResponsaveis() {
        List<Responsavel> responsaveis = responsavelService.listarResponsaveis();

        if (responsaveis.isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: Nenhum responsável cadastrado"
            );
        }

        for (Responsavel responsavel : responsaveis) {
            System.out.println("====================");
            System.out.println("ID:           " + responsavel.getId());
            System.out.println("Nome:         " + responsavel.getNome());
            System.out.println("CPF:          " + responsavel.getCpf());
            System.out.println("Nascimento:   " + responsavel.getDataNascimentoFormatada());
            System.out.println("Endereço:     " + responsavel.getEndereco());
            System.out.println("Telefone:     " + responsavel.getTelefone());

            List<Integer> matriculas = responsavel.getAlunosId();
            if (matriculas == null || matriculas.isEmpty()) {
                System.out.println("---- Alunos: Nenhum aluno vinculado");
            } else {
                System.out.println("---- Alunos ----");
                for (int i = 0; i < matriculas.size(); i++) {
                    Aluno aluno = BuscarAluno.buscarPorMatricula(matriculas.get(i));
                    if (aluno != null) {
                        System.out.println("  " + (i + 1) + ". " + aluno.getNome());
                        System.out.println("     Matrícula:  " + aluno.getMatricula());
                        System.out.println("     CPF:        " + aluno.getCpf());
                        System.out.println("     Nascimento: " + aluno.getDataNascimentoFormatada());
                        System.out.println("     Série:      " + aluno.getSerie());
                        System.out.println("     Situação:   " + aluno.getSituacao());
                    } else {
                        System.out.println("  " + (i + 1) + ". Aluno não encontrado (matrícula: " + matriculas.get(i) + ")");
                    }
                }
            }
        }
        System.out.println("====================");
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