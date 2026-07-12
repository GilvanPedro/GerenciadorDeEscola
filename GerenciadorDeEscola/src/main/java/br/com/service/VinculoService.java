package br.com.service;

import br.com.dao.DisciplinaDAO;
import br.com.dao.ProfessorDAO;
import br.com.dao.TurmaDAO;
import br.com.dao.VinculoDAO;
import br.com.model.entity.Disciplina;
import br.com.model.entity.Professor;
import br.com.model.entity.Turma;
import br.com.model.entity.Vinculo;
import br.com.util.BuscaPorId;
import br.com.util.GerarId;

import java.util.List;

public class VinculoService {
    private final VinculoDAO vinculoDAO = new VinculoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    // Verificações e salvamento do vínculo
    public void adicionarVinculo(Vinculo vinculo) {

        // Verifica se o professor existe
        Professor professor = BuscaPorId.buscarPorId(professorDAO.listarProfessor(), vinculo.getProfessorId());
        if (professor == null) {
            throw new IllegalArgumentException(
                    "Erro: Professor com id: " + vinculo.getProfessorId() + " não encontrado."
            );
        }

        // Verifica se a turma existe
        Turma turma = BuscaPorId.buscarPorId(turmaDAO.listarTurmas(), vinculo.getTurmaId());
        if (turma == null) {
            throw new IllegalArgumentException(
                    "Erro: Turma com id: " + vinculo.getTurmaId() + " não encontrada."
            );
        }

        // O vínculo precisa ter no mínimo 1 disciplina
        if (vinculo.getDisciplinaId() == null || vinculo.getDisciplinaId().isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: Vínculo não pode estar sem disciplinas."
            );
        }

        // Verifica se cada disciplina informada realmente existe
        List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();
        for (int disciplinaId : vinculo.getDisciplinaId()) {
            Disciplina encontrada = BuscaPorId.buscarPorId(disciplinas, disciplinaId);
            if (encontrada == null) {
                throw new IllegalArgumentException(
                        "Erro: Disciplina com id " + disciplinaId + " não encontrada."
                );
            }
        }

        // Verifica se o dia da semana foi informado
        if (vinculo.getDiaSemana() == null) {
            throw new IllegalArgumentException(
                    "Erro: É necessário informar o dia da semana."
            );
        }

        // Verifica se o período da aula foi informado
        if (vinculo.getPeriodoAula() == null) {
            throw new IllegalArgumentException(
                    "Erro: É necessário informar o período da aula."
            );
        }

        List<Vinculo> vinculos = vinculoDAO.listarVinculos();

        // Verifica se o professor já tem um vínculo no mesmo dia/período
        boolean professorOcupado = vinculos.stream()
                .anyMatch(v -> v.getProfessorId() == vinculo.getProfessorId()
                        && v.getDiaSemana() == vinculo.getDiaSemana()
                        && v.getPeriodoAula() == vinculo.getPeriodoAula());

        if (professorOcupado) {
            throw new IllegalArgumentException(
                    "Erro: O professor já possui um vínculo neste dia e período."
            );
        }

        // Verifica se a turma já tem um vínculo no mesmo dia/período
        boolean turmaOcupada = vinculos.stream()
                .anyMatch(v -> v.getTurmaId() == vinculo.getTurmaId()
                        && v.getDiaSemana() == vinculo.getDiaSemana()
                        && v.getPeriodoAula() == vinculo.getPeriodoAula());

        if (turmaOcupada) {
            throw new IllegalArgumentException(
                    "Erro: A turma já possui um vínculo neste dia e período."
            );
        }

        // Gera o id só após passar em todas as verificações
        int id = GerarId.gerarNovoId(vinculoDAO.caminhoUltimoId());
        vinculo.setId(id);

        // Salva
        vinculos.add(vinculo);
        vinculoDAO.salvar(vinculos);
    }

    // Listar os vínculos
    public List<Vinculo> listarVinculos() {
        return vinculoDAO.listarVinculos();
    }

    // Verificações da edição do vínculo e salvamento dele
    public void editarVinculo(Vinculo vinculoAtualizado) {

        if (vinculoAtualizado.getDisciplinaId() == null || vinculoAtualizado.getDisciplinaId().isEmpty()) {
            throw new IllegalArgumentException(
                    "Erro: Vínculo não pode estar sem disciplinas."
            );
        }

        List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();
        for (int disciplinaId : vinculoAtualizado.getDisciplinaId()) {
            Disciplina encontrada = BuscaPorId.buscarPorId(disciplinas, disciplinaId);
            if (encontrada == null) {
                throw new IllegalArgumentException(
                        "Erro: Disciplina com id " + disciplinaId + " não encontrada."
                );
            }
        }

        if (vinculoAtualizado.getDiaSemana() == null) {
            throw new IllegalArgumentException(
                    "Erro: É necessário informar o dia da semana."
            );
        }

        if (vinculoAtualizado.getPeriodoAula() == null) {
            throw new IllegalArgumentException(
                    "Erro: É necessário informar o período da aula."
            );
        }

        // Verifica conflito de horário com outros vínculos, ignorando o próprio
        List<Vinculo> vinculos = vinculoDAO.listarVinculos();

        boolean professorOcupado = vinculos.stream()
                .anyMatch(v -> v.getId() != vinculoAtualizado.getId()
                        && v.getProfessorId() == vinculoAtualizado.getProfessorId()
                        && v.getDiaSemana() == vinculoAtualizado.getDiaSemana()
                        && v.getPeriodoAula() == vinculoAtualizado.getPeriodoAula());

        if (professorOcupado) {
            throw new IllegalArgumentException(
                    "Erro: O professor já possui um vínculo neste dia e período."
            );
        }

        boolean turmaOcupada = vinculos.stream()
                .anyMatch(v -> v.getId() != vinculoAtualizado.getId()
                        && v.getTurmaId() == vinculoAtualizado.getTurmaId()
                        && v.getDiaSemana() == vinculoAtualizado.getDiaSemana()
                        && v.getPeriodoAula() == vinculoAtualizado.getPeriodoAula());

        if (turmaOcupada) {
            throw new IllegalArgumentException(
                    "Erro: A turma já possui um vínculo neste dia e período."
            );
        }

        vinculoDAO.editar(vinculoAtualizado);
    }

    // Exclui o vínculo se ele existir
    public void excluirVinculo(int id) {
        List<Vinculo> vinculos = vinculoDAO.listarVinculos();

        boolean existe = vinculos.stream().anyMatch(v -> v.getId() == id);

        if (!existe) {
            throw new IllegalArgumentException(
                    "Erro: vínculo com id: " + id + " não encontrado."
            );
        }

        vinculoDAO.excluir(id);
    }
}