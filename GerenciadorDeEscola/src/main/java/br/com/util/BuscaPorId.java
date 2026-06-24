package br.com.util;

import br.com.model.Identificador;

import java.util.List;

public class BuscaPorId {

    public static <T extends Identificador> T buscarPorId(List<T> lista, int id) {
        int inicio = 0;
        int fim = lista.size() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            T elemento = lista.get(meio);

            if (elemento.getId() == id) {
                return elemento;
            }

            if (elemento.getId() < id) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return null;
    }
}