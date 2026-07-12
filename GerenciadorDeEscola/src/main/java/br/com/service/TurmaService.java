package br.com.service;

import br.com.dao.AlunoDAO;
import br.com.dao.TurmaDAO;
import br.com.model.entity.Aluno;
import br.com.model.entity.Turma;
import br.com.util.GerarId;

import java.time.LocalDate;
import java.util.List;

public class TurmaService {
    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    // Adicionar a turma e fazer as verificações
    public void adicionarTurma(Turma turma){
        List<Turma> turmas = turmaDAO.listarTurmas();

        // Validar se a série foi informada
        if(turma.getSerie() == null){
            throw new IllegalArgumentException(
                    "Erro: Você precisa informar uma série"
            );
        }

        // Validar se o turno foi informado
        if(turma.getTurno() == null){
            throw new IllegalArgumentException(
                    "Erro: Você precisa informar um turno"
            );
        }

        // Validar se o nível de ensino foi informado
        if(turma.getNivelEnsino() == null){
            throw new IllegalArgumentException(
                    "Erro: Você precisa informar um nível de ensino"
            );
        }

        // Validar o ano letivo
        if(turma.getAnoLetivo() <= 0){
            throw new IllegalArgumentException(
                    "Erro: O ano letivo informado é inválido"
            );
        }

        // Verificar se o ano letivo não é do futuro
        if(turma.getAnoLetivo() > LocalDate.now().getYear()){
            throw new IllegalArgumentException(
                    "Erro: O ano letivo informado não pode ser no futuro! Ano: " + LocalDate.now().getYear()
            );
        }

        // Verificar se já existe uma turma igual (mesma série, turno, nível de ensino e ano letivo)
        boolean turmaDuplicada = turmas.stream().anyMatch(t ->
                t.getSerie() == turma.getSerie() &&
                        t.getTurno() == turma.getTurno() &&
                        t.getNivelEnsino() == turma.getNivelEnsino() &&
                        t.getAnoLetivo() == turma.getAnoLetivo()
        );

        if(turmaDuplicada){
            throw new IllegalArgumentException(
                    "Erro: Já existe uma turma cadastrada com essa série, turno, nível de ensino e ano letivo"
            );
        }

        // Verificar se cada aluno informado realmente existe (a turma pode começar vazia;
        // alunos são vinculados principalmente pelo cadastro/edição do Aluno)
        List<Aluno> alunos = alunoDAO.listarAluno();
        for(int alunoId : turma.getAlunosId()){
            boolean alunoExiste = alunos.stream().anyMatch(a -> a.getMatricula() == alunoId);

            if(!alunoExiste){
                throw new IllegalArgumentException(
                        "Erro: Aluno com matrícula " + alunoId + " não encontrado."
                );
            }
        }

        // Gerar e definir o id
        int id = GerarId.gerarNovoId(turmaDAO.caminhoUltimoId());
        turma.setId(id);

        // Salvar
        turmas.add(turma);
        turmaDAO.salvar(turmas);
    }

    // Listar as turmas
    public List<Turma> listarTurmas(){
        return turmaDAO.listarTurmas();
    }

    // Editar uma turma e salvar ela
    public void editarTurma(Turma turmaEditada){
        List<Turma> turmas = turmaDAO.listarTurmas();

        boolean existe = turmas.stream().anyMatch(t -> t.getId() == turmaEditada.getId());

        if(!existe){
            throw new IllegalArgumentException(
                    "Erro: Turma com id: " + turmaEditada.getId() + " não encontrada"
            );
        }

        // Validar se a série foi informada
        if(turmaEditada.getSerie() == null){
            throw new IllegalArgumentException(
                    "Erro: Você precisa informar uma série"
            );
        }

        // Validar se o turno foi informado
        if(turmaEditada.getTurno() == null){
            throw new IllegalArgumentException(
                    "Erro: Você precisa informar um turno"
            );
        }

        // Validar se o nível de ensino foi informado
        if(turmaEditada.getNivelEnsino() == null){
            throw new IllegalArgumentException(
                    "Erro: Você precisa informar um nível de ensino"
            );
        }

        // Validar o ano letivo
        if(turmaEditada.getAnoLetivo() <= 0){
            throw new IllegalArgumentException(
                    "Erro: O ano letivo informado é inválido"
            );
        }

        // Verificar se o ano letivo não é do futuro
        if(turmaEditada.getAnoLetivo() > LocalDate.now().getYear()){
            throw new IllegalArgumentException(
                    "Erro: O ano letivo informado não pode ser no futuro! Ano: " + LocalDate.now().getYear()
            );
        }

        // Validar se há pelo menos um aluno vinculado
        if(turmaEditada.getAlunosId() == null){
            turmaEditada.setAlunosId(new java.util.ArrayList<>());
        }

        // Verificar se cada aluno informado realmente existe
        List<Aluno> alunos = alunoDAO.listarAluno();
        for(int alunoId : turmaEditada.getAlunosId()){
            boolean alunoExiste = alunos.stream().anyMatch(a -> a.getMatricula() == alunoId);

            if(!alunoExiste){
                throw new IllegalArgumentException(
                        "Erro: Aluno com matrícula " + alunoId + " não encontrado."
                );
            }
        }

        turmaDAO.editar(turmaEditada);
    }

    // Excluir uma turma
    public void excluirTurma(int id){
        List<Turma> turmas = turmaDAO.listarTurmas();

        boolean existe = turmas.stream().anyMatch(t -> t.getId() == id);

        if(!existe){
            throw new IllegalArgumentException(
                    "Erro: Turma com id: " + id + " não encontrada"
            );
        }

        turmaDAO.excluir(id);
    }

    // Adiciona um aluno à lista de alunos de uma turma (idempotente).
    // Usado tanto pelo cadastro/edição de Aluno (fluxo principal) quanto pela
    // ação rápida "Adicionar à turma" na aba de Turmas.
    public void adicionarAlunoNaTurma(int turmaId, int alunoMatricula){
        List<Turma> turmas = turmaDAO.listarTurmas();
        Turma turma = turmas.stream().filter(t -> t.getId() == turmaId).findFirst().orElse(null);

        if(turma == null){
            throw new IllegalArgumentException(
                    "Erro: Turma com id " + turmaId + " não encontrada."
            );
        }

        List<Integer> alunosId = new java.util.ArrayList<>(
                turma.getAlunosId() == null ? new java.util.ArrayList<>() : turma.getAlunosId()
        );

        if(!alunosId.contains(alunoMatricula)){
            alunosId.add(alunoMatricula);
            turma.setAlunosId(alunosId);
            turmaDAO.editar(turma);
        }
    }

    // Remove um aluno da lista de alunos de uma turma (idempotente).
    // Não falha caso a turma já não exista mais (ex.: durante exclusão em cascata).
    public void removerAlunoDaTurma(int turmaId, int alunoMatricula){
        List<Turma> turmas = turmaDAO.listarTurmas();
        Turma turma = turmas.stream().filter(t -> t.getId() == turmaId).findFirst().orElse(null);

        if(turma == null){
            return;
        }

        List<Integer> alunosId = new java.util.ArrayList<>(
                turma.getAlunosId() == null ? new java.util.ArrayList<>() : turma.getAlunosId()
        );

        if(alunosId.remove(Integer.valueOf(alunoMatricula))){
            turma.setAlunosId(alunosId);
            turmaDAO.editar(turma);
        }
    }
}