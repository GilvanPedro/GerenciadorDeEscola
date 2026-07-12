package br.com.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.lang.reflect.Method;

/**
 * Classe central de estilo visual do sistema.
 * Concentra cores, fontes e utilitários de estilização (incluindo efeitos de hover)
 * para manter um visual minimalista e consistente em todas as abas.
 */
public final class Tema {

    private Tema() {}

    // ---------- Paleta de cores ----------
    public static final Color BRANCO = Color.WHITE;
    public static final Color CINZA_CLARO = new Color(0xF4, 0xF5, 0xF7);
    public static final Color CINZA_MEDIO = new Color(0xE3, 0xE5, 0xE9);
    public static final Color CINZA_ESCURO = new Color(0x4B, 0x50, 0x58);
    public static final Color TEXTO_PRINCIPAL = new Color(0x22, 0x25, 0x2A);
    public static final Color TEXTO_SECUNDARIO = new Color(0x6B, 0x70, 0x78);

    public static final Color ACCENT = new Color(0x2F, 0x6F, 0xED);
    public static final Color ACCENT_HOVER = new Color(0x24, 0x59, 0xC7);
    public static final Color ACCENT_CLARO = new Color(0xE9, 0xF0, 0xFE);

    public static final Color PERIGO = new Color(0xDC, 0x3A, 0x3A);
    public static final Color PERIGO_HOVER = new Color(0xB8, 0x2C, 0x2C);

    public static final Color SECUNDARIO = new Color(0xEE, 0xF0, 0xF3);
    public static final Color SECUNDARIO_HOVER = new Color(0xE0, 0xE3, 0xE8);

    // ---------- Fontes ----------
    public static final Font FONTE_BASE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONTE_ROTULO = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONTE_BOTAO = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONTE_TITULO_ABA = new Font("SansSerif", Font.BOLD, 15);
    public static final Font FONTE_TABELA_CABECALHO = new Font("SansSerif", Font.BOLD, 12);

    /** Estiliza um botão como ação primária (destaque em azul). */
    public static void botaoPrimario(JButton botao) {
        estilizarBotaoBase(botao, ACCENT, ACCENT_HOVER, BRANCO);
    }

    /** Estiliza um botão como ação secundária (cinza neutro, ex.: limpar). */
    public static void botaoSecundario(JButton botao) {
        estilizarBotaoBase(botao, SECUNDARIO, SECUNDARIO_HOVER, TEXTO_PRINCIPAL);
    }

    /** Estiliza um botão como ação destrutiva (vermelho, ex.: excluir). */
    public static void botaoPerigo(JButton botao) {
        estilizarBotaoBase(botao, PERIGO, PERIGO_HOVER, BRANCO);
    }

    private static void estilizarBotaoBase(JButton botao, Color normal, Color hover, Color texto) {
        botao.setFont(FONTE_BOTAO);
        botao.setForeground(texto);
        botao.setBackground(normal);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(new EmptyBorder(9, 18, 9, 18));

        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(normal);
            }
        });
    }

    /** Aplica um efeito de hover sutil (mudança de cor de fundo da aba) num JTabbedPane. */
    public static void aplicarHoverAbas(JTabbedPane abas) {
        abas.addMouseMotionListener(new MouseMotionAdapter() {
            private int indiceAnterior = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                int indice = abas.indexAtLocation(e.getX(), e.getY());
                if (indice != indiceAnterior) {
                    if (indiceAnterior != -1 && indiceAnterior < abas.getTabCount()) {
                        abas.setBackgroundAt(indiceAnterior, null);
                    }
                    if (indice != -1) {
                        abas.setBackgroundAt(indice, ACCENT_CLARO);
                    }
                    indiceAnterior = indice;
                }
            }
        });

        abas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                for (int i = 0; i < abas.getTabCount(); i++) {
                    abas.setBackgroundAt(i, null);
                }
            }
        });
    }

    /** Estiliza uma JTable com um visual limpo e moderno. */
    public static void estilizarTabela(JTable tabela) {
        tabela.setFont(FONTE_BASE);
        tabela.setRowHeight(28);
        tabela.setShowVerticalLines(false);
        tabela.setShowHorizontalLines(true);
        tabela.setGridColor(CINZA_MEDIO);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setSelectionBackground(ACCENT_CLARO);
        tabela.setSelectionForeground(TEXTO_PRINCIPAL);
        tabela.setFillsViewportHeight(true);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setFont(FONTE_TABELA_CABECALHO);
        cabecalho.setBackground(CINZA_CLARO);
        cabecalho.setForeground(TEXTO_SECUNDARIO);
        cabecalho.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CINZA_MEDIO));
        cabecalho.setPreferredSize(new Dimension(cabecalho.getWidth(), 32));
        tabela.setBackground(BRANCO);
    }

    /** Cria um rótulo padronizado. */
    public static JLabel rotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_ROTULO);
        label.setForeground(TEXTO_SECUNDARIO);
        return label;
    }

    /** Cria um título de seção para o formulário de cada aba. */
    public static JLabel titulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_TITULO_ABA);
        label.setForeground(TEXTO_PRINCIPAL);
        return label;
    }

    /** Aplica um espaçamento generoso (padding) num painel. */
    public static void aplicarEspacamento(JComponent componente, int topo, int esquerda, int baixo, int direita) {
        componente.setBorder(new EmptyBorder(topo, esquerda, baixo, direita));
    }

    /**
     * Renderer genérico para combos de enums que possuem o método getDescricao().
     * Usa reflexão para não depender de uma interface comum entre os enums do domínio.
     */
    public static DefaultListCellRenderer rendererDescricaoEnum() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                           boolean isSelected, boolean cellHasFocus) {
                String texto = "";
                if (value != null) {
                    try {
                        Method metodo = value.getClass().getMethod("getDescricao");
                        Object resultado = metodo.invoke(value);
                        texto = String.valueOf(resultado);
                    } catch (Exception e) {
                        texto = value.toString();
                    }
                }
                return super.getListCellRendererComponent(list, texto, index, isSelected, cellHasFocus);
            }
        };
    }
}
