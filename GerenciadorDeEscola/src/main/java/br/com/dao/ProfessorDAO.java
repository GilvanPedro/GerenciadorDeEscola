package br.com.dao;

import br.com.model.entity.Professor;
import br.com.util.GarantirRepositorio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {
    private final String ARQUIVO = "GerenciadorDeEscola/arquivos/professores.txt";
    private final String IDENTIFICADOR = "GerenciadorDeEscola/arquivos/ultimo_identificador_professor.txt";

    private GarantirRepositorio garantirDiretorio = new GarantirRepositorio();

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
        garantirDiretorio.criarDiretorio(ARQUIVO);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Professor p : professores) {
                bw.write(p.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Converte "1,2,3" → List<Integer> (equivalente ao converterResponsaveis do Aluno)
    private List<Integer> converterLista(String texto) {
        List<Integer> ids = new ArrayList<>();
        if (texto == null || texto.isBlank()) return ids; // ← linha adicionada
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