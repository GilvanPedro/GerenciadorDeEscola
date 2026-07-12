package br.com.dao;

import br.com.model.entity.Vinculo;
import br.com.model.enums.DiaSemana;
import br.com.model.enums.PeriodoAula;
import br.com.util.GarantirRepositorio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VinculoDAO {
    private final String ARQUIVO = "GerenciadorDeEscola/arquivos/vinculos.txt";
    private final String IDENTIFICADOR = "GerenciadorDeEscola/arquivos/ultimo_identificador_vinculo.txt";

    private GarantirRepositorio garantirRepositorio = new GarantirRepositorio();

    // Listar os vínculos cadastrados
    public List<Vinculo> listarVinculos() {
        List<Vinculo> vinculos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                Vinculo vinculo = new Vinculo(
                        Integer.parseInt(dados[0]),                            // id
                        Integer.parseInt(dados[1]),                            // professorId
                        converterLista(dados[2]),                              // disciplinaId
                        Integer.parseInt(dados[3]),                            // turmaId
                        converterDiaSemana(dados[4]),                          // diaSemana
                        converterPeriodoAula(dados[5])                         // periodoAula
                );

                vinculos.add(vinculo);
            }

        } catch (IOException e) {
            System.out.println("Arquivo ainda não existe.");
        }

        return vinculos;
    }

    // Salva a lista de vínculos no arquivo
    public void salvar(List<Vinculo> vinculos) {
        garantirRepositorio.criarDiretorio(ARQUIVO);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Vinculo v : vinculos) {
                bw.write(v.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Remove um vínculo pelo id
    public void excluir(int id) {
        List<Vinculo> vinculos = listarVinculos();
        vinculos.removeIf(v -> v.getId() == id);
        salvar(vinculos);
    }

    // Edita as informações de um vínculo já cadastrado
    public void editar(Vinculo vinculoAtualizado) {
        List<Vinculo> vinculos = listarVinculos();

        for (int i = 0; i < vinculos.size(); i++) {
            if (vinculos.get(i).getId() == vinculoAtualizado.getId()) {
                vinculos.set(i, vinculoAtualizado);
                break;
            }
        }

        salvar(vinculos);
    }

    // Converte "1,2,3" ou "vazio" para List<Integer>
    private List<Integer> converterLista(String texto) {
        List<Integer> ids = new ArrayList<>();

        if (texto == null || texto.isBlank() || texto.trim().equalsIgnoreCase("vazio")) {
            return ids;
        }

        for (String parte : texto.split(",")) {
            ids.add(Integer.parseInt(parte.trim()));
        }

        return ids;
    }

    // Converte de volta para o Enum DiaSemana
    private DiaSemana converterDiaSemana(String texto) {
        return DiaSemana.valueOf(texto.trim().toUpperCase());
    }

    // Converte de volta para o Enum PeriodoAula
    private PeriodoAula converterPeriodoAula(String texto) {
        return PeriodoAula.valueOf(texto.trim().toUpperCase());
    }

    // Caminho de onde tá salvando o último id
    public String caminhoUltimoId() {
        return IDENTIFICADOR;
    }
}