package br.com.service;

import br.com.dao.DisciplinaDAO;
import br.com.model.entity.Disciplina;
import br.com.util.GerarId;

import java.util.List;

public class DisciplinaService {
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    // Adicionar uma disciplina depois de fazer as verificações
    public void adicionar(Disciplina disciplina){
        List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();

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
}
