package br.com.service;

import br.com.dao.AlunoDAO;
import br.com.dao.ResponsavelDAO;
import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;
import br.com.util.BuscaPorId;
import br.com.util.BuscarAluno;
import br.com.util.GerarId;
import br.com.util.ValidarCpf;

import java.util.ArrayList;
import java.util.List;

public class ResponsavelService {

    private final ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    // Verificar e salvar o Responsável (alunos não são obrigatórios no cadastro inicial)
    public void adicionarResponsavel(Responsavel responsavel) {
        List<Responsavel> responsaveis = responsavelDAO.listarResponsaveis();

        // Verifica se o cpf é válido
        if (!ValidarCpf.validarCpf(responsavel.getCpf())) {
            throw new IllegalArgumentException(
                    "Erro: CPF inválido"
            );
        }

        // Verificar duplicidade de cpf
        if (ValidarCpf.validarDuplicidadeCPF(responsaveis, responsavel.getCpf())) {
            throw new IllegalArgumentException(
                    "Erro: O CPF já está cadastrado"
            );
        }

        // Verificar se os alunos informados existem (caso já informe algum no cadastro)
        if (responsavel.getAlunosId() != null && !responsavel.getAlunosId().isEmpty()) {
            for (int alunoId : responsavel.getAlunosId()) {
                Aluno encontrado = BuscarAluno.buscarPorMatricula(alunoId);
                if (encontrado == null) {
                    throw new IllegalArgumentException(
                            "Erro: Aluno com matrícula " + alunoId + " não encontrado."
                    );
                }
            }
        }

        int id = GerarId.gerarNovoId(responsavelDAO.caminhoUltimoId());
        responsavel.setId(id);

        // Salvamento do Responsável
        responsaveis.add(responsavel);
        responsavelDAO.salvar(responsaveis);

    }

    // Vincula um aluno já existente a um responsável já existente
    public void adicionarAlunoAoResponsavel(int responsavelId, int matriculaAluno) {
        List<Responsavel> responsaveis = responsavelDAO.listarResponsaveis();

        // Verifica se o responsável existe
        Responsavel responsavel = BuscaPorId.buscarPorId(responsaveis, responsavelId);
        if (responsavel == null) {
            throw new IllegalArgumentException(
                    "Erro: Responsável com id " + responsavelId + " não encontrado."
            );
        }

        // Verifica se o aluno existe
        Aluno aluno = BuscarAluno.buscarPorMatricula(matriculaAluno);
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Erro: Aluno com matrícula " + matriculaAluno + " não encontrado."
            );
        }

        // Verifica se o aluno já está vinculado
        if (responsavel.getAlunosId().contains(matriculaAluno)) {
            throw new IllegalArgumentException(
                    "Erro: Aluno com matrícula " + matriculaAluno + " já está vinculado a este responsável."
            );
        }

        // Adiciona o aluno e salva
        List<Integer> alunosAtualizados = new ArrayList<>(responsavel.getAlunosId());
        alunosAtualizados.add(matriculaAluno);
        responsavel.setAlunosId(alunosAtualizados);

        responsavelDAO.editar(responsavel);
    }

    // Listar os Responsáveis
    public List<Responsavel> listarResponsaveis() {
        return responsavelDAO.listarResponsaveis();
    }

    // Editar o responsável
    public void editarResponsavel(Responsavel responsavelAtualizado) {
        if (responsavelAtualizado.getNome() == null || responsavelAtualizado.getNome().isBlank()) {
            throw new IllegalArgumentException(
                    "Erro: nome do responsável não pode ser vazio."
            );
        }

        if (responsavelAtualizado.getTelefone() == null || responsavelAtualizado.getTelefone().isBlank()) {
            throw new IllegalArgumentException(
                    "Erro: telefone não pode ser vazio."
            );
        }

        if (responsavelAtualizado.getEndereco() == null || responsavelAtualizado.getEndereco().isBlank()) {
            throw new IllegalArgumentException(
                    "Erro: endereço não pode ser vazio."
            );
        }

        // Verificar se os alunos informados existem
        if (responsavelAtualizado.getAlunosId() != null && !responsavelAtualizado.getAlunosId().isEmpty()) {
            for (int alunoId : responsavelAtualizado.getAlunosId()) {
                Aluno encontrado = BuscarAluno.buscarPorMatricula(alunoId);
                if (encontrado == null) {
                    throw new IllegalArgumentException(
                            "Erro: Aluno com matrícula " + alunoId + " não encontrado."
                    );
                }
            }
        }

        responsavelDAO.editar(responsavelAtualizado);
    }

    // Exclui o responsável se ele existir
    public void excluirResponsavel(int id) {
        List<Responsavel> responsaveis = responsavelDAO.listarResponsaveis();

        boolean existe = responsaveis.stream()
                .anyMatch(r -> r.getId() == id);

        if (!existe) {
            throw new IllegalArgumentException(
                    "Erro: responsável com id " + id + " não encontrado."
            );
        }

        responsavelDAO.excluir(id);
    }
}