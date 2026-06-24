package br.com.util;

import br.com.dao.ProfessorDAO;

import java.io.*;

public class GerarId {

    private static GarantirRepositorio garantirDiretorio = new GarantirRepositorio();

    // Gerar um novo Id
    public static int gerarNovoId(String caminhoArquivo) {
        int ultimoId = lerUltimoId(caminhoArquivo);
        int novoId = ultimoId + 1;
        salvarId(caminhoArquivo, novoId);
        return novoId;
    }

    // Ler o ultimo Id
    private static int lerUltimoId(String caminhoArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha = br.readLine();
            if (linha != null) {
                return Integer.parseInt(linha.trim());
            }
        } catch (IOException e) {
            return 0;
        }
        return 0;
    }

    // Salva o Id
    private static void salvarId(String caminhoArquivo, int novoId) {
        garantirDiretorio.criarDiretorio(caminhoArquivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            bw.write(String.valueOf(novoId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}