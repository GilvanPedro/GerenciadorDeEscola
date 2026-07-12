package br.com.view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

/**
 * Fábrica de componentes de formulário comuns, usados em várias abas:
 * campo de CPF com máscara, listas de seleção múltipla e combos de enum.
 */
public final class Campos {

    private Campos() {}

    /** Cria um campo de texto para CPF com máscara "###.###.###-##". */
    public static JFormattedTextField campoCpf() {
        try {
            MaskFormatter mascara = new MaskFormatter("###.###.###-##");
            mascara.setPlaceholderCharacter('_');
            JFormattedTextField campo = new JFormattedTextField(mascara);
            campo.setFont(Tema.FONTE_BASE);
            campo.setColumns(14);
            return campo;
        } catch (ParseException e) {
            // Não deve ocorrer, a máscara é fixa e válida
            JFormattedTextField campo = new JFormattedTextField();
            campo.setColumns(14);
            return campo;
        }
    }

    /** Cria um campo de texto para datas no formato "dd/MM/yyyy". */
    public static JFormattedTextField campoData() {
        try {
            MaskFormatter mascara = new MaskFormatter("##/##/####");
            mascara.setPlaceholderCharacter('_');
            JFormattedTextField campo = new JFormattedTextField(mascara);
            campo.setFont(Tema.FONTE_BASE);
            campo.setColumns(10);
            return campo;
        } catch (ParseException e) {
            JFormattedTextField campo = new JFormattedTextField();
            campo.setColumns(10);
            return campo;
        }
    }

    /** Extrai apenas os dígitos de um texto de CPF (removendo pontos e traço da máscara). */
    public static String cpfSemMascara(String cpfComMascara) {
        return cpfComMascara == null ? "" : cpfComMascara.replaceAll("\\D", "");
    }

    /** Cria um JTextField padronizado. */
    public static JTextField campoTexto(int colunas) {
        JTextField campo = new JTextField(colunas);
        campo.setFont(Tema.FONTE_BASE);
        return campo;
    }

    /** Cria um campo de texto desabilitado, usado para IDs/matrículas gerados automaticamente. */
    public static JTextField campoSomenteLeitura() {
        JTextField campo = campoTexto(10);
        campo.setEditable(false);
        campo.setBackground(Tema.CINZA_CLARO);
        campo.setForeground(Tema.TEXTO_SECUNDARIO);
        return campo;
    }

    /** Cria uma JList de seleção múltipla, envolvida em uma JScrollPane, pronta para uso. */
    public static JList<ItemSelecao> listaMultipla() {
        JList<ItemSelecao> lista = new JList<>();
        lista.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lista.setFont(Tema.FONTE_BASE);
        lista.setBackground(Tema.BRANCO);
        lista.setSelectionBackground(Tema.ACCENT_CLARO);
        lista.setSelectionForeground(Tema.TEXTO_PRINCIPAL);
        return lista;
    }

    /**
     * Cria uma lista de seleção múltipla baseada em checkbox: basta clicar em
     * cada item para marcá-lo/desmarcá-lo, sem precisar de Ctrl/Shift.
     */
    public static ListaComCheckbox listaComCheckbox() {
        return new ListaComCheckbox();
    }

    public static JScrollPane comRolagem(JComponent componente) {
        JScrollPane scroll = new JScrollPane(componente);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.CINZA_MEDIO));
        return scroll;
    }

    /** Cria um combo box para um array de valores de enum, usando o renderer de descrição. */
    public static <E> JComboBox<E> comboEnum(E[] valores) {
        JComboBox<E> combo = new JComboBox<>(valores);
        combo.setFont(Tema.FONTE_BASE);
        combo.setRenderer(Tema.rendererDescricaoEnum());
        return combo;
    }

    public static JComboBox<ItemSelecao> comboItens() {
        JComboBox<ItemSelecao> combo = new JComboBox<>();
        combo.setFont(Tema.FONTE_BASE);
        return combo;
    }
}
