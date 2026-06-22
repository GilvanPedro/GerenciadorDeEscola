package br.com.dao;

import br.com.model.entity.Aluno;
import br.com.model.enums.SituacaoAluno;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private final String ARQUIVO = "GerenciadorDeEscola/arquivos/alunos.txt";
    private final String MATRICULA = "GerenciadorDeEscola/arquivos/ultima_matricula.txt";

    // Vai listar o aluno
    public List<Aluno> listar(){
        List<Aluno> alunos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {

            String linha;

            while ((linha = br.readLine()) != null) {

                String[] dados = linha.split(";");

                Aluno aluno = new Aluno(
                        Integer.parseInt(dados[0]),
                        dados[1],
                        dados[2],
                        dados[3],
                        dados[4],
                        Integer.parseInt(dados[5]),
                        SituacaoAluno.valueOf(dados[6]),
                        converterResponsaveis(dados[7])
                );

                alunos.add(aluno);
            }

        } catch (IOException e) {
            System.out.println("Arquivo ainda não existe.");
        }

        return alunos;
    }

    // Salva o aluno no arquivo
    public void salvar(List<Aluno> pessoas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {

            for (Aluno a : pessoas) {
                bw.write(a.toString());
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Vai salvar o valor da última matrícula
    public void salvarUltimaMatricula(int matricula) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MATRICULA))) {

            bw.write(String.valueOf(matricula));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Vai obter o valor da última matrícula cadastrada
    public int obterUltimaMatricula() {

        try (BufferedReader br = new BufferedReader(new FileReader(MATRICULA))) {

            String linha = br.readLine();

            if (linha != null) {
                return Integer.parseInt(linha);
            }

        } catch (IOException e) {
            return 0;
        }
        return 0;
    }

    // Remove um aluno pelo número de matrícula
    public void excluir(int matricula) {
        List<Aluno> alunos = listar();

        alunos.removeIf(a -> a.getMatricula() == matricula);
        salvar(alunos);
    }

    // Edita as informações de um aluno já cadastrado
    public void editar(Aluno alunoAtualizado) {
        List<Aluno> alunos = listar();

        for (int i = 0; i < alunos.size(); i++) {
            if (alunos.get(i).getMatricula() == alunoAtualizado.getMatricula()) {
                alunos.set(i, alunoAtualizado);
                break;
            }
        }

        salvar(alunos);
    }

    private List<Integer> converterResponsaveis(String texto) {

        List<Integer> ids = new ArrayList<>();
        String[] partes = texto.split(",");

        for (String id : partes) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }
}
