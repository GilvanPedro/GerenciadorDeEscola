package br.com.controller;

import br.com.dao.AlunoDAO;
import br.com.dao.TurmaDAO;
import br.com.model.dto.TurmaExibicao;
import br.com.model.entity.Aluno;
import br.com.model.entity.Turma;
import br.com.model.enums.NivelEnsino;
import br.com.model.enums.Series;
import br.com.model.enums.Turnos;
import br.com.service.TurmaService;
import br.com.util.BuscaPorId;

import java.util.ArrayList;
import java.util.List;

public class TurmaController {

    private TurmaDAO turmaDAO = new TurmaDAO();
    private TurmaService turmaService = new TurmaService();
    private AlunoDAO alunoDAO = new AlunoDAO();

    // Monta a lista de turmas pronta para exibição, já com os alunos matriculados resolvidos
    public List<TurmaExibicao> listarTurma(){
        List<Turma> turmas = turmaDAO.listarTurmas();

        List<Aluno> todosAlunos = alunoDAO.listarAluno();
        List<TurmaExibicao> exibicoes = new ArrayList<>();

        for(Turma t : turmas){
            List<Aluno> alunosDaTurma = new ArrayList<>();

            for(int alunoId : t.getAlunosId()){
                Aluno aluno = todosAlunos.stream()
                        .filter(a -> a.getMatricula() == alunoId)
                        .findFirst()
                        .orElse(null);

                if(aluno != null){
                    alunosDaTurma.add(aluno);
                }
            }

            exibicoes.add(new TurmaExibicao(t, alunosDaTurma));
        }

        return exibicoes;
    }

    // Adicionar turma
    public void adicionarTurma(
            Series serie,
            Turnos turno,
            NivelEnsino nivelEnsino,
            int anoLetivo,
            List<Integer> alunosId
    ){
        Turma novaTurma = new Turma(
                0,
                serie,
                turno,
                nivelEnsino,
                anoLetivo,
                alunosId == null ? new ArrayList<>() : alunosId
        );

        turmaService.adicionarTurma(novaTurma);
    }

    // Editar turma já cadastrada
    public void editarTurma(
            int id,
            Series serie,
            Turnos turno,
            NivelEnsino nivelEnsino,
            int anoLetivo,
            List<Integer> alunosId
    ){
        Turma turmaExistente = BuscaPorId.buscarPorId(turmaDAO.listarTurmas(), id);

        // Verifica se a turma existe antes de enviar para outras verificações
        if(turmaExistente == null){
            throw new IllegalArgumentException(
                    "Erro: Turma com o id: " + id + " não encontrada"
            );
        }

        Turma turmaAtualizada = new Turma(
                id,
                serie,
                turno,
                nivelEnsino,
                anoLetivo,
                alunosId == null ? turmaExistente.getAlunosId() : alunosId
        );

        turmaService.editarTurma(turmaAtualizada);
    }

    // Manda para o service excluir a turma
    public void excluirTurma(int id){
        turmaService.excluirTurma(id);
    }

    // Adiciona um aluno já existente à turma (ação rápida na aba de Turmas)
    public void adicionarAlunoNaTurma(int turmaId, int alunoMatricula){
        turmaService.adicionarAlunoNaTurma(turmaId, alunoMatricula);
    }

    // Remove um aluno da turma (ação rápida na aba de Turmas)
    public void removerAlunoDaTurma(int turmaId, int alunoMatricula){
        turmaService.removerAlunoDaTurma(turmaId, alunoMatricula);
    }
}