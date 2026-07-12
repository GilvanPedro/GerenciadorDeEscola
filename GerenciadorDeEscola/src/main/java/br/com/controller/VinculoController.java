package br.com.controller;

import br.com.dao.VinculoDAO;
import br.com.model.entity.Vinculo;
import br.com.model.enums.DiaSemana;
import br.com.model.enums.PeriodoAula;
import br.com.service.VinculoService;
import br.com.util.BuscaPorId;

import java.util.List;

public class VinculoController {

    private final VinculoService vinculoService = new VinculoService();
    private final VinculoDAO vinculoDAO = new VinculoDAO();

    // Lista os vínculos cadastrados, prontos para exibição
    public List<Vinculo> listarVinculos() {
        List<Vinculo> vinculos = vinculoService.listarVinculos();

        return vinculos;
    }

    // Adiciona um vínculo, enviando para o service para as verificações finais
    public void adicionarVinculo(
            int professorId,
            List<Integer> disciplinasId,
            int turmaId,
            DiaSemana diaSemana,
            PeriodoAula periodoAula
    ) {
        Vinculo vinculo = new Vinculo(
                professorId,
                disciplinasId,
                turmaId,
                diaSemana,
                periodoAula
        );

        vinculoService.adicionarVinculo(vinculo);
    }

    // Edita um vínculo já cadastrado
    public void editarVinculo(
            int id,
            int professorId,
            List<Integer> disciplinasId,
            int turmaId,
            DiaSemana diaSemana,
            PeriodoAula periodoAula
    ) {
        Vinculo vinculoExistente = BuscaPorId.buscarPorId(vinculoDAO.listarVinculos(), id);

        // Verifica se o vínculo existe antes de enviar para outras verificações
        if (vinculoExistente == null) {
            throw new IllegalArgumentException(
                    "Erro: Vínculo com o id: " + id + " não encontrado."
            );
        }

        Vinculo vinculoAtualizado = new Vinculo(
                id,
                professorId,
                disciplinasId,
                turmaId,
                diaSemana,
                periodoAula
        );

        vinculoService.editarVinculo(vinculoAtualizado);
    }

    // Manda para o service excluir o vínculo
    public void excluirVinculo(int id) {
        vinculoService.excluirVinculo(id);
    }
}