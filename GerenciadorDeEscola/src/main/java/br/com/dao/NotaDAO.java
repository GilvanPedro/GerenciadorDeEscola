package br.com.dao;

import br.com.model.entity.Nota;
import br.com.util.GarantirRepositorio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NotaDAO {
    private final String ARQUIVO = "GerenciadorDeEscola/arquivos/notas.txt";
    private final String IDENTIFICADOR = "GerenciadorDeEscola/arquivos/ultimo_identificador_nota.txt";

    private GarantirRepositorio garantirRepositorio = new GarantirRepositorio();

    // Listar as notas lançadas
    public List<Nota> listarNotas() {
        List<Nota> notas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                Nota nota = new Nota(
                        Integer.parseInt(dados[0]),     // id
                        Integer.parseInt(dados[1]),     // alunoMatricula
                        Integer.parseInt(dados[2]),     // disciplinaId
                        Integer.parseInt(dados[3]),     // bimestre
                        Double.parseDouble(dados[4])    // valor
                );

                notas.add(nota);
            }

        } catch (IOException e) {
            System.out.println("Arquivo ainda não existe.");
        }

        return notas;
    }

    // Salva a lista de notas no arquivo
    public void salvar(List<Nota> notas) {
        garantirRepositorio.criarDiretorio(ARQUIVO);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Nota n : notas) {
                bw.write(n.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Remove uma nota pelo id
    public void excluir(int id) {
        List<Nota> notas = listarNotas();
        notas.removeIf(n -> n.getId() == id);
        salvar(notas);
    }

    // Edita uma nota já lançada
    public void editar(Nota notaAtualizada) {
        List<Nota> notas = listarNotas();

        for (int i = 0; i < notas.size(); i++) {
            if (notas.get(i).getId() == notaAtualizada.getId()) {
                notas.set(i, notaAtualizada);
                break;
            }
        }

        salvar(notas);
    }

    // Caminho de onde tá salvando o último id
    public String caminhoUltimoId() {
        return IDENTIFICADOR;
    }
}