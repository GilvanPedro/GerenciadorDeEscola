package br.com.view;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de formulário reutilizável, baseado em GridBagLayout, que permite
 * adicionar linhas "rótulo + campo" de forma simples e com espaçamento
 * consistente em todas as abas do sistema.
 */
public class PainelFormulario extends JPanel {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private int linhaAtual = 0;

    public PainelFormulario() {
        setLayout(new GridBagLayout());
        setBackground(Tema.BRANCO);
        setOpaque(true);
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
    }

    /** Adiciona uma linha simples com rótulo à esquerda e campo à direita (campo ocupa o espaço restante). */
    public void adicionarLinha(String rotuloTexto, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = linhaAtual;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(Tema.rotulo(rotuloTexto), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(campo, gbc);

        linhaAtual++;
    }

    /**
     * Adiciona uma linha em que o campo é alto (ex.: uma JScrollPane com uma JList
     * de seleção múltipla), permitindo altura preferencial customizada.
     */
    public void adicionarLinhaAlta(String rotuloTexto, JComponent campo, int altura) {
        gbc.gridx = 0;
        gbc.gridy = linhaAtual;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(Tema.rotulo(rotuloTexto), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        campo.setPreferredSize(new Dimension(campo.getPreferredSize().width, altura));
        add(campo, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        linhaAtual++;
    }

    /** Adiciona uma linha ocupando as duas colunas (ex.: uma faixa de botões). */
    public void adicionarLinhaCompleta(JComponent componente) {
        gbc.gridx = 0;
        gbc.gridy = linhaAtual;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(componente, gbc);
        gbc.gridwidth = 1;
        linhaAtual++;
    }

    public int proximaLinha() {
        return linhaAtual;
    }
}
