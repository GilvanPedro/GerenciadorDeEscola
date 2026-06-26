package br.com.dao;

import br.com.model.entity.Disciplina;
import br.com.model.enums.NivelEnsino;
import br.com.util.GarantirRepositorio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {
    private final String ARQUIVO = "GerenciadorDeEscola/arquivos/disciplinas.txt";
    private final String IDENTIFICADOR = "GerenciadorDeEscola/arquivos/ultimo_identificador_disciplina.txt";

    private GarantirRepositorio garantirRepositorio = new GarantirRepositorio();

    // Listar as disciplinas
    public List<Disciplina> listarDisciplinas() {
        List<Disciplina> disciplinas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                Disciplina disciplina = new Disciplina(
                        Integer.parseInt(dados[0]),    // id
                        dados[1],                      // disciplina
                        converterEnum(dados[2])        // nivelEnsino
                );

                disciplinas.add(disciplina);
            }

        } catch (IOException e) {
            System.out.println("Arquivo ainda não existe.");
        }

        return disciplinas;
    }

    // Salva as disciplinas no arquivo
    public void salvar(List<Disciplina> disciplinas){
        garantirRepositorio.criarDiretorio(ARQUIVO);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))){
            for(Disciplina d : disciplinas){
                bw.write(d.getId() + ";" + d.getDisciplina() + ";" + d.getNivelEnsino());
                bw.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Remover uma disciplina pelo id
    public void excluir(int id){
        List<Disciplina> disciplinas = listarDisciplinas();

        disciplinas.removeIf(d -> d.getId() == id);
        salvar(disciplinas);
    }

    // Editar as informações de uma disciplina e cadastra-la novamente
    public void editar(Disciplina disciplinaAtualizada){
        List<Disciplina> disciplinas = listarDisciplinas();

        for(int i = 0; i < disciplinas.size(); i++){
            if(disciplinas.get(i).getId() == disciplinaAtualizada.getId()){
                disciplinas.set(i, disciplinaAtualizada);
                break;
            }
        }

        salvar(disciplinas);
    }

    // Caminho de onde tá salvando o id
    public String caminhoUltimoId(){
        return IDENTIFICADOR;
    }

    // Converter de volta para os Enums
    public NivelEnsino converterEnum(String texto) {
        return NivelEnsino.valueOf(texto.trim().toUpperCase());
    }
}
