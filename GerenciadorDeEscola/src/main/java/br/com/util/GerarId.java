package br.com.util;

import java.io.*;

public class GerarId {

    public static int gerarNovoId(String caminhoArquivo) {
        int ultimoId = lerUltimoId(caminhoArquivo);
        int novoId = ultimoId + 1;
        salvarId(caminhoArquivo, novoId);
        return novoId;
    }

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

    private static void salvarId(String caminhoArquivo, int novoId) {
        garantirDiretorio(caminhoArquivo); // <-- adiciona aqui
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            bw.write(String.valueOf(novoId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void garantirDiretorio(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        File diretorio = arquivo.getParentFile();
        if (diretorio != null && !diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
}