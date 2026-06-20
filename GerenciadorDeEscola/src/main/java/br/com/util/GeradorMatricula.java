package br.com.util;

import br.com.dao.AlunoDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeradorMatricula {
    AlunoDAO alunoDAO = new AlunoDAO();

    LocalDate hoje = LocalDate.now();
    String anoCurto = hoje.format(DateTimeFormatter.ofPattern("yy"));

    public int gerarMatricula() {

        String anoCurto = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
        int ultimaMatricula = alunoDAO.obterUltimaMatricula();

        // Pega só os 3 últimos dígitos (sequencial) da última matrícula e soma 1
        int proximoSequencial = (ultimaMatricula % 1000) + 1;

        // Garante no mínimo 3 algarismos (1 -> "001", 23 -> "023", 456 -> "456")
        String sequencialFormatado = String.format("%03d", proximoSequencial);
        String matricula = anoCurto + sequencialFormatado;

        return Integer.parseInt(matricula);
    }
}
