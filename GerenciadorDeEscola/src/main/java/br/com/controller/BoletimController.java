package br.com.controller;

import br.com.model.dto.BoletimExibicao;
import br.com.model.dto.LinhaBoletim;
import br.com.model.entity.Aluno;
import br.com.service.BoletimService;
import br.com.util.BuscarAluno;

import java.util.List;

public class BoletimController {
    private final BoletimService boletimService = new BoletimService();

    // Monta o boletim do aluno pronto para exibição
    public BoletimExibicao gerarExibicaoBoletim(int matricula) {
        List<LinhaBoletim> linhas = boletimService.gerarBoletim(matricula);
        Aluno aluno = BuscarAluno.buscarPorMatricula(matricula);

        return new BoletimExibicao(aluno, linhas);
    }
}