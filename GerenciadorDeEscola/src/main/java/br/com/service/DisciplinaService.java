package br.com.service;

import br.com.dao.DisciplinaDAO;
import br.com.model.entity.Disciplina;
import br.com.util.GerarId;

import java.text.Normalizer;
import java.util.List;

public class DisciplinaService {
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    // Adicionar uma disciplina depois de fazer as verificações
    public void adicionarDisciplina(Disciplina disciplina){
        List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();

        // Verificar nome duplicado
        boolean nomeIgual = disciplinas.stream()
                .anyMatch(d -> normalizar(d.getDisciplina()).equals(normalizar(disciplina.getDisciplina())));

        if(nomeIgual){
            throw new IllegalArgumentException(
                    "Erro: Já existe uma disciplina com o nome: " + disciplina.getDisciplina()
            );
        }

        // Validar se tem o nome da disciplina
        if(disciplina.getDisciplina() == null){
            throw new IllegalArgumentException(
                    "Erro: O nome da disciplina não pode estar vazio"
            );
        }

        // Ver se um nível de ensino foi adicionado
        if(disciplina.getNivelEnsino() == null){
            throw new IllegalArgumentException(
                    "Erro: Você precisa informar um nível de ensino"
            );
        }

        // Gerar e definir o id
        int id = GerarId.gerarNovoId(disciplinaDAO.caminhoUltimoId());
        disciplina.setId(id);

        // Salvar
        disciplinas.add(disciplina);
        disciplinaDAO.salvar(disciplinas);
    }

    // Listar as disciplinas
    public List<Disciplina> listarDisciplinas(){
        return disciplinaDAO.listarDisciplinas();
    }

    // Editar uma disciplina e salvar ela
    public void editarDisciplina(Disciplina disciplinaEditada){

        List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();

        // Verificar nome duplicado
        boolean nomeIgual = disciplinas.stream()
                .anyMatch(d -> normalizar(d.getDisciplina()).equals(normalizar(disciplinaEditada.getDisciplina())));

        if(nomeIgual){
            throw new IllegalArgumentException(
                    "Erro: Já existe uma disciplina com o nome: " + disciplinaEditada.getDisciplina()
            );
        }

        // Validar se tem o nome da disciplina
        if(disciplinaEditada.getDisciplina() == null){
            throw new IllegalArgumentException(
                    "Erro: O nome da disciplina não pode estar vazio"
            );
        }

        // Ver se um nível de ensino foi adicionado
        if(disciplinaEditada.getNivelEnsino() == null){
            throw new IllegalArgumentException(
                    "Erro: Você precisa informar um nível de ensino"
            );
        }

        disciplinaDAO.editar(disciplinaEditada);
    }

    // Excluir uma Disciplina
    public void excluirDisciplina(int id){
        List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();

        boolean existe = disciplinas.stream().anyMatch(d -> d.getId() == id);

        if(!existe){
            throw new IllegalArgumentException(
                    "Erro: Disciplina com id: " + id + " não encontrado."
            );
        }

        disciplinaDAO.excluir(id);
    }

    private String normalizar(String texto){
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .toLowerCase()
                .trim();
    }
}
