package br.com.util;

import br.com.dao.AlunoDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeradorMatricula {
    AlunoDAO alunoDAO = new AlunoDAO();

    LocalDate hoje = LocalDate.now();
    String anoCurto = hoje.format(DateTimeFormatter.ofPattern("yy"));

    public int gerarMatricula() {

        String anoAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
        int ultimaMatricula = alunoDAO.obterUltimaMatricula();

        int proximoSequencial = 1;

        if (ultimaMatricula > 0) {

            String ultimaMatriculaStr = String.valueOf(ultimaMatricula);
            String anoUltimaMatricula = ultimaMatriculaStr.substring(0, 2);

            if (anoAtual.equals(anoUltimaMatricula)) {
                String sequencialStr = ultimaMatriculaStr.substring(2);
                proximoSequencial = Integer.parseInt(sequencialStr) + 1;
            }
        }

        String matricula = anoAtual + String.format("%03d", proximoSequencial);

        return Integer.parseInt(matricula);
    }
}