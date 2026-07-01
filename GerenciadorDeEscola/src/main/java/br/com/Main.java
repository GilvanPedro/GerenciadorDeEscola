package br.com;

import br.com.controller.TurmaController;
import br.com.model.enums.NivelEnsino;
import br.com.model.enums.Series;
import br.com.model.enums.Turnos;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TurmaController turmaController = new TurmaController();

        List<Integer> alunosId = new ArrayList<>();
        alunosId.add(26005);
        alunosId.add(26006);
        alunosId.add(26007);

        turmaController.adicionarTurma(
                Series.PRIMEIRA_SERIE,
                Turnos.VESPERTINO,
                NivelEnsino.ENSINO_MEDIO,
                2026,
                alunosId
        );

        System.out.println("Turma adicionada com sucesso!");

        // Lista as turmas para conferir se foi salva corretamente
        turmaController.listarTurma();
    }
}