package br.com.util;

import java.io.File;

public class GarantirRepositorio {

    // Garante que o diretório existe antes de salvar
    public void criarDiretorio(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        File diretorio = arquivo.getParentFile();
        if (diretorio != null && !diretorio.exists()) {
            diretorio.mkdirs(); // cria todas as pastas necessárias
        }
    }
}
