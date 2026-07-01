package br.com.controller;

import br.com.dao.AlunoDAO;
import br.com.dao.TurmaDAO;
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

    // Listar turmas
    public void listarTurma(){
        List<Turma> turmas = turmaDAO.listarTurmas();

        if(turmas.isEmpty()){
            throw new IllegalArgumentException(
                    "Erro: Nenhuma turma cadastrada"
            );
        }

        List<Aluno> todosAlunos = alunoDAO.listarAluno();

        for(Turma t : turmas){
            System.out.println("-------------------");
            System.out.println("ID:              " + t.getId());
            System.out.println("Série:           " + t.getSerie().getDescricao());
            System.out.println("Turno:           " + t.getTurno().getDescricao());
            System.out.println("Nível de Ensino: " + t.getNivelEnsino().getDescricao());
            System.out.println("Ano Letivo:      " + t.getAnoLetivo());

            System.out.println("---- Alunos ----");
            if(t.getAlunosId().isEmpty()){
                System.out.println("  Nenhum aluno matriculado");
            } else {
                int i = 1;
                for(int alunoId : t.getAlunosId()){
                    Aluno aluno = todosAlunos.stream()
                            .filter(a -> a.getMatricula() == alunoId)
                            .findFirst()
                            .orElse(null);

                    if(aluno != null){
                        System.out.println("  " + i + ". " + aluno.getNome());
                        System.out.println("     Matrícula:  " + aluno.getMatricula());
                        System.out.println("     CPF:        " + aluno.getCpf());
                        System.out.println("     Nascimento: " + aluno.getDataNascimentoFormatada());
                        System.out.println("     Situação:   " + aluno.getSituacao());
                    } else {
                        System.out.println("  " + i + ". Aluno não encontrado (matrícula: " + alunoId + ")");
                    }
                    i++;
                }
            }
        }
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
}