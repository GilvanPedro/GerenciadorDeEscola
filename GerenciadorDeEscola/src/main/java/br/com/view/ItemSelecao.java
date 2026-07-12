package br.com.view;

import java.util.Objects;

/**
 * Representa um item exibido em um JComboBox ou JList (ex.: um aluno, turma,
 * disciplina, etc.), guardando o id real usado pela lógica de negócio e um
 * rótulo amigável para exibição ao usuário.
 */
public class ItemSelecao {
    private final int id;
    private final String rotulo;

    public ItemSelecao(int id, String rotulo) {
        this.id = id;
        this.rotulo = rotulo;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return rotulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemSelecao)) return false;
        ItemSelecao that = (ItemSelecao) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
