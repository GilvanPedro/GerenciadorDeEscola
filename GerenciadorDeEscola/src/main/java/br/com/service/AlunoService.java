package br.com.service;

import br.com.dao.AlunoDAO;
import br.com.model.entity.Aluno;
import br.com.util.ValidarCpf;

import java.util.List;

public class AlunoService {

    private final AlunoDAO alunoDAO = new AlunoDAO();

    public void adicionar(Aluno aluno) {

        List<Aluno> alunos = alunoDAO.listar();

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

        // Salvamento dos Alunos
        alunos.add(aluno);
        alunoDAO.salvar(alunos);
        alunoDAO.salvarUltimaMatricula(aluno.getMatricula());
    }

    // Listar os Alunos
    public List<Aluno> listar() {
        return alunoDAO.listar();
    }
}