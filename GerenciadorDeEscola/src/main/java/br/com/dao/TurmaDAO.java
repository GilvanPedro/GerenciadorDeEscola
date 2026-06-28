package br.com.dao;

import br.com.model.entity.Turma;
import br.com.model.enums.NivelEnsino;
import br.com.model.enums.Series;
import br.com.model.enums.Turnos;
import br.com.util.GarantirRepositorio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {
    private final String ARQUIVO = "GerenciadorDeEscola/arquivos/turmas.txt";
    private final String IDENTIFICADOR = "GerenciadorDeEscola/arquivos/ultimo_identificador_turma.txt";

    private final GarantirRepositorio garantirRepositorio = new GarantirRepositorio();

    // Listar as turmas
    public List<Turma> listarTurmas(){
        List<Turma> turmas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                Turma turma = new Turma(
                        Integer.parseInt(dados[0]),         // id
                        converterParaSerie(dados[1]),       // Serie
                        converterParaTurnos(dados[2]),      // Turno
                        converterParaNivelEnsino(dados[3]), // Nível de Ensino
                        Integer.parseInt(dados[4]),         // Ano Letivo
                        conveterAlunos(dados[5])            // Alunos

                );

                turmas.add(turma);
            }

        } catch (IOException e) {
            System.out.println("Arquivo ainda não existe.");
        }

        return turmas;
    }

    // Salvar a lista de turmas
    public void salvar(List<Turma> turmas) {
        garantirRepositorio.criarDiretorio(ARQUIVO);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Turma t : turmas) {
                bw.write(t.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Converter para Enum Series
    public Series converterParaSerie(String texto){
        return Series.valueOf(texto.trim().toUpperCase());
    }

    // Converter para Enum Turnos
    public Turnos converterParaTurnos(String texto){
        return Turnos.valueOf(texto.trim().toUpperCase());
    }

    // Converter para Enum Nivel de ensino
    public NivelEnsino converterParaNivelEnsino(String texto) {
        return NivelEnsino.valueOf(texto.trim().toUpperCase());
    }

    // Converter o texto de volta para a lista dos ids do aluno
    private List<Integer> conveterAlunos(String texto) {

        List<Integer> ids = new ArrayList<>();
        String[] partes = texto.split(",");

        for (String id : partes) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }
}
