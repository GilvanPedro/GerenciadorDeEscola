package br.com.util;

import java.io.*;

public class GerarId {
    public static int gerarNovoId(String CAMINHO){
        int ultimoId = lerUltimoId(CAMINHO);
        int novoId = ultimoId + 1;
        salvarId(CAMINHO, novoId);

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
}
