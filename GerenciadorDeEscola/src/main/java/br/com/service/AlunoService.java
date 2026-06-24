package br.com.service;

import br.com.dao.AlunoDAO;
import br.com.model.entity.Aluno;
import br.com.util.ValidarCpf;

import java.util.List;

public class AlunoService {

    private final AlunoDAO alunoDAO = new AlunoDAO();

    public void adicionar(Aluno aluno) {

        List<Aluno> alunos = alunoDAO.listarAluno();

        // Validar se a matrícula está repetida
        for(Aluno a : alunos){
            if(a.getMatricula() == aluno.getMatricula()){
                throw new IllegalArgumentException(
                        "A matrícula já está cadastrada, tente novamente, se o erro persistir, entre em contato com algum desenvolvedor"
                );
            }
        }

        // Validar o Cpf
        if(!ValidarCpf.validarCpf(aluno.getCpf())){
            throw new IllegalArgumentException(
                    "O CPF é inválido"
            );
        }

        // Verificar por Cpf já cadastrado
        if(ValidarCpf.validarDuplicidadeCPF(alunos, aluno.getCpf())){
            throw new IllegalArgumentException(
                    "O CPF já está cadastrado"
            );
        }

        // Validar se o aluno tem responsáveis cadastrados
        if (aluno.getResponsavelId() == null || aluno.getResponsavelId().isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: aluno deve ter ao menos um responsável."
            );
        }

        // Salvamento dos Alunos
        alunos.add(aluno);
        alunoDAO.salvar(alunos);
        alunoDAO.salvarUltimaMatricula(aluno.getMatricula());
    }

    // Listar os Alunos
    public List<Aluno> listar() {
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