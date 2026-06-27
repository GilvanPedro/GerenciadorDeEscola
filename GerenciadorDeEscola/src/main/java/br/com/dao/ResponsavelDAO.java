package br.com.dao;

import br.com.model.entity.Responsavel;
import br.com.util.GarantirRepositorio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelDAO {

    private final String ARQUIVO = "GerenciadorDeEscola/arquivos/responsaveis.txt";
    private final String IDENTIFICADOR = "GerenciadorDeEscola/arquivos/ultimo_identificador_responsavel.txt";

    private GarantirRepositorio garantirRepositorio = new GarantirRepositorio();

    // Lista todos os responsáveis cadastrados
    public List<Responsavel> listarResponsaveis() {
        List<Responsavel> responsaveis = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                Responsavel responsavel = new Responsavel(
                        Integer.parseInt(dados[0]),   // id
                        dados[1],                     // nome
                        dados[2],                     // cpf
                        dados[3],                     // dataNascimento
                        dados[4],                     // endereco
                        dados[5],                     // telefone
                        converterAlunos(dados[6])     // alunosId
                );

                responsaveis.add(responsavel);
            }

        } catch (IOException e) {
            System.out.println("Arquivo ainda não existe.");
        }

        return responsaveis;
    }

    // Salva a lista de responsáveis no arquivo
    public void salvar(List<Responsavel> responsaveis) {
        garantirRepositorio.criarDiretorio(ARQUIVO);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Responsavel r : responsaveis) {
                bw.write(r.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Remove um responsável pelo id
    public void excluir(int id) {
        List<Responsavel> responsaveis = listarResponsaveis();

        responsaveis.removeIf(r -> r.getId() == id);
        salvar(responsaveis);
    }

    // Edita as informações de um responsável já cadastrado
    public void editar(Responsavel responsavelAtualizado) {
        List<Responsavel> responsaveis = listarResponsaveis();

        for (int i = 0; i < responsaveis.size(); i++) {
            if (responsaveis.get(i).getId() == responsavelAtualizado.getId()) {
                responsaveis.set(i, responsavelAtualizado);
                break;
            }
        }

        salvar(responsaveis);
    }

    // Salva o valor do último id gerado
    public void salvarUltimoId(int id) {
        garantirRepositorio.criarDiretorio(ARQUIVO);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(IDENTIFICADOR))) {
            bw.write(String.valueOf(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Obtém o valor do último id cadastrado
    public int obterUltimoId() {
        try (BufferedReader br = new BufferedReader(new FileReader(IDENTIFICADOR))) {
            String linha = br.readLine();

            if (linha != null) {
                return Integer.parseInt(linha);
            }

        } catch (IOException e) {
            return 0;
        }

        return 0;
    }

    // Retorna o caminho do arquivo de controle de id
    public String caminhoUltimoId() {
        return IDENTIFICADOR;
    }

    // Converte a string de ids para List<Integer>, tratando "vazio" e string em branco
    private List<Integer> converterAlunos(String texto) {
        List<Integer> ids = new ArrayList<>();

        if (texto == null || texto.isBlank() || texto.equals("vazio")) {
            return ids;
        }

        for (String parte : texto.split(",")) {
            String trimmed = parte.trim();
            if (!trimmed.isEmpty()) {
                ids.add(Integer.parseInt(trimmed));
            }
        }

        return ids;
    }
}