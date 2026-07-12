package br.com.service;

import br.com.dao.DisciplinaDAO;
import br.com.dao.NotaDAO;
import br.com.dao.VinculoDAO;
import br.com.model.dto.LinhaBoletim;
import br.com.model.entity.Aluno;
import br.com.model.entity.Disciplina;
import br.com.model.entity.Nota;
import br.com.model.entity.Vinculo;
import br.com.model.enums.SituacaoBoletim;
import br.com.util.BuscaPorId;
import br.com.util.BuscarAluno;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BoletimService {
    private final VinculoDAO vinculoDAO = new VinculoDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final NotaDAO notaDAO = new NotaDAO();

    // Nota mínima para ser aprovado direto
    private static final double MEDIA_APROVACAO = 6.0;
    // Abaixo disso é reprovado direto, entre esse valor e a média de aprovação é recuperação
    private static final double MEDIA_MINIMA_RECUPERACAO = 4.0;

    // Gera o boletim completo de um aluno, com todas as disciplinas da turma dele
    public List<LinhaBoletim> gerarBoletim(int matricula) {

        Aluno aluno = BuscarAluno.buscarPorMatricula(matricula);

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Erro: Aluno com matrícula " + matricula + " não encontrado."
            );
        }

        // Pega todos os vínculos da turma do aluno, pra saber quais disciplinas ele deve cursar
        List<Vinculo> vinculosDaTurma = vinculoDAO.listarVinculos().stream()
                .filter(v -> v.getTurmaId() == aluno.getTurmaId())
                .toList();

        if (vinculosDaTurma.isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: A turma do aluno ainda não possui disciplinas vinculadas."
            );
        }

        // Junta as disciplinas de todos os vínculos, sem repetir
        Set<Integer> disciplinaIds = new LinkedHashSet<>();
        for (Vinculo v : vinculosDaTurma) {
            disciplinaIds.addAll(v.getDisciplinaId());
        }

        List<Disciplina> todasDisciplinas = disciplinaDAO.listarDisciplinas();
        List<Nota> notasDoAluno = notaDAO.listarNotas().stream()
                .filter(n -> n.getAlunoMatricula() == matricula)
                .toList();

        List<LinhaBoletim> boletim = new ArrayList<>();

        for (int disciplinaId : disciplinaIds) {
            Disciplina disciplina = BuscaPorId.buscarPorId(todasDisciplinas, disciplinaId);
            String nomeDisciplina = disciplina != null ? disciplina.getDisciplina() : "Disciplina não encontrada (id: " + disciplinaId + ")";

            Double nota1 = buscarNotaDoBimestre(notasDoAluno, disciplinaId, 1);
            Double nota2 = buscarNotaDoBimestre(notasDoAluno, disciplinaId, 2);
            Double nota3 = buscarNotaDoBimestre(notasDoAluno, disciplinaId, 3);
            Double nota4 = buscarNotaDoBimestre(notasDoAluno, disciplinaId, 4);

            Double mediaFinal = null;
            SituacaoBoletim situacao = SituacaoBoletim.EM_ANDAMENTO;

            if (nota1 != null && nota2 != null && nota3 != null && nota4 != null) {
                mediaFinal = Math.round(((nota1 + nota2 + nota3 + nota4) / 4) * 100.0) / 100.0;
                situacao = classificar(mediaFinal);
            }

            boletim.add(new LinhaBoletim(disciplinaId, nomeDisciplina, nota1, nota2, nota3, nota4, mediaFinal, situacao));
        }

        return boletim;
    }

    // Busca a nota de um bimestre específico, dentro das notas já filtradas do aluno
    private Double buscarNotaDoBimestre(List<Nota> notasDoAluno, int disciplinaId, int bimestre) {
        return notasDoAluno.stream()
                .filter(n -> n.getDisciplinaId() == disciplinaId && n.getBimestre() == bimestre)
                .map(Nota::getValor)
                .findFirst()
                .orElse(null);
    }

    // Classifica a média final em uma situação
    private SituacaoBoletim classificar(double mediaFinal) {
        if (mediaFinal >= MEDIA_APROVACAO) {
            return SituacaoBoletim.APROVADO;
        } else if (mediaFinal >= MEDIA_MINIMA_RECUPERACAO) {
            return SituacaoBoletim.RECUPERACAO;
        } else {
            return SituacaoBoletim.REPROVADO;
        }
    }
}