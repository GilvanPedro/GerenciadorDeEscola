package br.com.dao;

import br.com.model.entity.Professor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {
    private final String ARQUIVO = "GerenciadorEscola/arquivos/professores.txt";
    private final String IDENTIFICADOR = "GerenciadorEscola/arquivos/ultimo_identificador_professor.txt";

    // Listar os professores cadastrados
    public List<Professor> listarProfessor() {
        List<Professor> professores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                Professor professor = new Professor(
                        dados[0],                      // nome
                        dados[1],                      // cpf
                        dados[2],                      // dataNascimentoTexto
                        Integer.parseInt(dados[3]),    // id
                        converterLista(dados[4]),      // disciplinaId
                        converterLista(dados[5]),      // vinculoId
                        dados[6],                      // telefone
                        dados[7],                      // endereco
                        dados[8]                       // email
                );

                professores.add(professor);
            }

        } catch (IOException e) {
            System.out.println("Arquivo ainda não existe.");
        }

        return professores;
    }

    // Salva a lista de professores no arquivo
    public void salvar(List<Professor> professores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {

            for (Professor p : professores) {
                bw.write(p.toString());
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Obtém o último identificador salvo
    public int obterUltimoIdentificador() {
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

    // Remove um professor pelo id
    public void excluir(int id) {
        List<Professor> professores = listarProfessor();
        professores.removeIf(p -> p.getId() == id);
        salvar(professores);
    }

    // Edita as informações de um professor já cadastrado
    public void editar(Professor professorAtualizado) {
        List<Professor> professores = listarProfessor();

        for (int i = 0; i < professores.size(); i++) {
            if (professores.get(i).getId() == professorAtualizado.getId()) {
                professores.set(i, professorAtualizado);
                break;
            }
        }

        salvar(professores);
    }

    // Salvar o ultimo id
    public void salvarUltimoId() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(IDENTIFICADOR))) {
            bw.write(String.valueOf(obterUltimoIdentificador() + 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Converte "1,2,3" → List<Integer> (equivalente ao converterResponsaveis do Aluno)
    private List<Integer> converterLista(String texto) {
        List<Integer> ids = new ArrayList<>();
        for (String parte : texto.split(",")) {
            ids.add(Integer.parseInt(parte.trim()));
        }
        return ids;
    }

    // Retorna o caminho de onde tá salvando o ultimo identificador
    public String caminhoUltimoIdentificador(){
        return IDENTIFICADOR;
    }
}