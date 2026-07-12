package br.com.view;

/**
 * Implementada por todas as abas do sistema. Permite que a aba recarregue
 * seus dados (tabela e combos) sempre que for selecionada, garantindo que
 * informações cadastradas em outras abas (ex.: uma nova turma) apareçam
 * corretamente nos formulários que dependem delas.
 */
public interface AbaAtualizavel {
    void atualizarDados();
}
