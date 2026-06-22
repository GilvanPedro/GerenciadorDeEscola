package br.com.util;

import br.com.dao.AlunoDAO;
import br.com.model.entity.Aluno;

import java.util.List;

public class BuscarAluno {
    private static final AlunoDAO alunoDAO = new AlunoDAO();

    // Vai buscar o aluno pela matrícula, com 2 blocos de buscas sequenciais, um para o ano, e outro para o resto da matrícula
    public static Aluno buscarPorMatricula(int matricula) {
        List<Aluno> alunos = alunoDAO.listar();

        String matriculaStr = String.valueOf(matricula);
        int anoBuscado = Integer.parseInt(matriculaStr.substring(0, 2));

        // 1ª busca binária: encontra os limites do bloco do ano
        int[] limites = encontrarLimitesDoAno(alunos, anoBuscado);

        if (limites == null) {
            return null; // ano não existe na lista
        }

        // 2ª busca binária: busca a matrícula dentro do bloco do ano
        return buscarNoBlocoDoAno(alunos, matricula, limites[0], limites[1]);
    }

    // Vai encontrar o ano da matrícula
    private static int[] encontrarLimitesDoAno(List<Aluno> alunos, int anoBuscado) {
        int inicio = 0;
        int fim = alunos.size() - 1;
        int primeiroDoAno = -1;
        int ultimoDoAno = -1;

        // Acha o primeiro aluno do ano
        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int anoMeio = getAno(alunos.get(meio).getMatricula());

            if (anoMeio == anoBuscado) {
                primeiroDoAno = meio;
                fim = meio - 1; // continua buscando à esquerda

            } else if (anoMeio < anoBuscado) {
                inicio = meio + 1;

            } else {
                fim = meio - 1;
            }
        }

        if (primeiroDoAno == -1) {
            return null; // ano não encontrado
        }

        // Acha o último aluno do ano
        inicio = primeiroDoAno;
        fim = alunos.size() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int anoMeio = getAno(alunos.get(meio).getMatricula());

            if (anoMeio == anoBuscado) {
                ultimoDoAno = meio;
                inicio = meio + 1; // continua buscando à direita

            } else {
                fim = meio - 1;
            }
        }

        return new int[]{primeiroDoAno, ultimoDoAno};
    }

    // Vai buscar dentro do bloco do ano
    private static Aluno buscarNoBlocoDoAno(List<Aluno> alunos, int matricula, int inicio, int fim) {
        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int matriculaMeio = alunos.get(meio).getMatricula();

            if (matriculaMeio == matricula) {
                return alunos.get(meio);

            } else if (matriculaMeio < matricula) {
                inicio = meio + 1;

            } else {
                fim = meio - 1;
            }
        }

        return null;
    }

    // Vai pegar o ano da matrícula
    private static int getAno(int matricula) {
        return Integer.parseInt(String.valueOf(matricula).substring(0, 2));
    }
}
