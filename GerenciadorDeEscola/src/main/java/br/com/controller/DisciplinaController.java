package br.com.controller;

import br.com.dao.DisciplinaDAO;
import br.com.model.entity.Disciplina;
import br.com.model.enums.NivelEnsino;
import br.com.service.DisciplinaService;
import br.com.util.BuscaPorId;

import java.util.List;

public class DisciplinaController {

    private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private DisciplinaService disciplinaService = new DisciplinaService();

    // Listar disciplina
    public void listarDisciplina(){
        List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();

        if(disciplinas.isEmpty()){
            throw new IllegalArgumentException(
                    "Erro: Nenhuma disciplina cadastrada"
            );
        }

        for(Disciplina d : disciplinas){
            System.out.println("-------------------");
            System.out.println("ID:              " + d.getId());
            System.out.println("Disciplina:      " + d.getDisciplina());
            System.out.println("Nível de Ensino: " + d.getNivelEnsino().getDescricao());
        }
    }

    // Adicionar Disciplina
    public void adicionarDisciplina(
            String disciplina,
            NivelEnsino nivelEnsino
    ){

        Disciplina novaDisciplina = new Disciplina(
                disciplina,
                nivelEnsino
        );

        disciplinaService.adicionarDisciplina(novaDisciplina);
    }

    // Editar disciplina já cadastrada
    public void editarDisciplina(
            int id,
            String disciplina,
            NivelEnsino nivelEnsino
    ){
        Disciplina disciplinaExistente = BuscaPorId.buscarPorId(disciplinaDAO.listarDisciplinas(), id);

        // Verifica se a disciplina existe antes de enviar para outras verificações
        if(disciplinaExistente == null){
            throw new IllegalArgumentException(
                    "Erro: Disciplina com o id: " + id + "não encontrada"
            );
        }

        Disciplina disciplinaAtualizada = new Disciplina(
                id,
                disciplina,
                nivelEnsino
        );
    }

    // Manda para o service excluir a disciplina
    public void excluirDisciplina(int id){
        disciplinaService.excluirDisciplina(id);
    }
}
