package br.com.controller;

import br.com.model.entity.Nota;
import br.com.service.NotaService;

import java.util.List;

public class NotaController {
    private final NotaService notaService = new NotaService();

    // Lança uma nota para um aluno, numa disciplina, num bimestre
    public void lancarNota(int alunoMatricula, int disciplinaId, int bimestre, double valor) {
        Nota nota = new Nota(alunoMatricula, disciplinaId, bimestre, valor);
        notaService.adicionarNota(nota);
    }

    // Lista as notas lançadas, prontas para exibição
    public List<Nota> listarNotas() {
        return notaService.listarNotas();
    }

    // Edita uma nota já lançada
    public void editarNota(int id, int alunoMatricula, int disciplinaId, int bimestre, double valor) {
        Nota notaAtualizada = new Nota(id, alunoMatricula, disciplinaId, bimestre, valor);
        notaService.editarNota(notaAtualizada);
    }

    // Exclui uma nota lançada
    public void excluirNota(int id) {
        notaService.excluirNota(id);
    }
}