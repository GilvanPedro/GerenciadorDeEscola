package br.com.service;

import br.com.dao.AlunoDAO;
import br.com.dao.DisciplinaDAO;
import br.com.dao.NotaDAO;
import br.com.model.entity.Aluno;
import br.com.model.entity.Disciplina;
import br.com.model.entity.Nota;
import br.com.util.GerarId;

import java.util.List;

public class NotaService {
    private final NotaDAO notaDAO = new NotaDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    // Verificações e lançamento da nota
    public void adicionarNota(Nota nota) {

        // Verifica se o aluno existe
        boolean alunoExiste = alunoDAO.listarAluno().stream()
                .anyMatch(a -> a.getMatricula() == nota.getAlunoMatricula());

        if (!alunoExiste) {
            throw new IllegalArgumentException(
                    "Erro: Aluno com matrícula " + nota.getAlunoMatricula() + " não encontrado."
            );
        }

        // Verifica se a disciplina existe
        boolean disciplinaExiste = disciplinaDAO.listarDisciplinas().stream()
                .anyMatch(d -> d.getId() == nota.getDisciplinaId());

        if (!disciplinaExiste) {
            throw new IllegalArgumentException(
                    "Erro: Disciplina com id " + nota.getDisciplinaId() + " não encontrada."
            );
        }

        // Valida o bimestre
        if (nota.getBimestre() < 1 || nota.getBimestre() > 4) {
            throw new IllegalArgumentException(
                    "Erro: O bimestre deve ser entre 1 e 4."
            );
        }

        // Valida o valor da nota
        if (nota.getValor() < 0 || nota.getValor() > 10) {
            throw new IllegalArgumentException(
                    "Erro: A nota deve ser entre 0 e 10."
            );
        }

        // Não deixa lançar duas notas pro mesmo aluno+disciplina+bimestre
        boolean jaLancada = notaDAO.listarNotas().stream()
                .anyMatch(n -> n.getAlunoMatricula() == nota.getAlunoMatricula()
                        && n.getDisciplinaId() == nota.getDisciplinaId()
                        && n.getBimestre() == nota.getBimestre());

        if (jaLancada) {
            throw new IllegalArgumentException(
                    "Erro: Já existe uma nota lançada para este aluno, nesta disciplina, neste bimestre. Use editar em vez de adicionar."
            );
        }

        // Gera o id só após passar em todas as verificações
        int id = GerarId.gerarNovoId(notaDAO.caminhoUltimoId());
        nota.setId(id);

        List<Nota> notas = notaDAO.listarNotas();
        notas.add(nota);
        notaDAO.salvar(notas);
    }

    // Listar todas as notas lançadas
    public List<Nota> listarNotas() {
        return notaDAO.listarNotas();
    }

    // Verificações da edição da nota e salvamento dela
    public void editarNota(Nota notaAtualizada) {

        if (notaAtualizada.getValor() < 0 || notaAtualizada.getValor() > 10) {
            throw new IllegalArgumentException(
                    "Erro: A nota deve ser entre 0 e 10."
            );
        }

        boolean existe = notaDAO.listarNotas().stream()
                .anyMatch(n -> n.getId() == notaAtualizada.getId());

        if (!existe) {
            throw new IllegalArgumentException(
                    "Erro: Nota com id " + notaAtualizada.getId() + " não encontrada."
            );
        }

        notaDAO.editar(notaAtualizada);
    }

    // Exclui a nota se ela existir
    public void excluirNota(int id) {
        boolean existe = notaDAO.listarNotas().stream()
                .anyMatch(n -> n.getId() == id);

        if (!existe) {
            throw new IllegalArgumentException(
                    "Erro: Nota com id " + id + " não encontrada."
            );
        }

        notaDAO.excluir(id);
    }
}