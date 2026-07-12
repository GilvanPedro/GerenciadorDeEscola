package br.com.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Lista de seleção múltipla baseada em checkboxes: cada item tem uma caixinha
 * de marcação própria, então basta clicar em qualquer item para marcá-lo ou
 * desmarcá-lo, sem precisar segurar Ctrl/Shift (que é o comportamento padrão,
 * pouco óbvio, de um JList com seleção múltipla).
 */
public class ListaComCheckbox extends JList<ItemSelecao> {

    private final Set<Integer> marcados = new LinkedHashSet<>();

    public ListaComCheckbox() {
        setModel(new DefaultListModel<>());
        setFont(Tema.FONTE_BASE);
        setBackground(Tema.BRANCO);
        setCellRenderer(new RendererCheckbox());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int indice = locationToIndex(e.getPoint());
                if (indice < 0) return;

                ItemSelecao item = getModel().getElementAt(indice);
                if (marcados.contains(item.getId())) {
                    marcados.remove(item.getId());
                } else {
                    marcados.add(item.getId());
                }
                repaint();
            }
        });
    }

    /** Substitui os itens exibidos na lista, mantendo as marcações atuais (por id). */
    public void definirItens(List<ItemSelecao> itens) {
        DefaultListModel<ItemSelecao> modelo = new DefaultListModel<>();
        for (ItemSelecao item : itens) {
            modelo.addElement(item);
        }
        setModel(modelo);
    }

    /** Marca exatamente os ids informados (desmarca o restante). */
    public void marcar(List<Integer> ids) {
        marcados.clear();
        if (ids != null) {
            marcados.addAll(ids);
        }
        repaint();
    }

    /** Desmarca todos os itens. */
    public void desmarcarTodos() {
        marcados.clear();
        repaint();
    }

    /** Retorna os ids atualmente marcados. */
    public List<Integer> obterMarcados() {
        return new ArrayList<>(marcados);
    }

    private class RendererCheckbox extends JCheckBox implements ListCellRenderer<ItemSelecao> {
        RendererCheckbox() {
            setOpaque(true);
            setFont(Tema.FONTE_BASE);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ItemSelecao> list, ItemSelecao value,
                                                       int index, boolean isSelected, boolean cellHasFocus) {
            setText(value == null ? "" : value.toString());
            setSelected(value != null && marcados.contains(value.getId()));
            setBackground(isSelected ? Tema.ACCENT_CLARO : Tema.BRANCO);
            setForeground(Tema.TEXTO_PRINCIPAL);
            setEnabled(list.isEnabled());
            return this;
        }
    }
}
