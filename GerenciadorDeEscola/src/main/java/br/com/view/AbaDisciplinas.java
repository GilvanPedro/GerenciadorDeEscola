package br.com.view;

import br.com.controller.DisciplinaController;
import br.com.model.entity.Disciplina;
import br.com.model.enums.NivelEnsino;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Aba de gerenciamento de Disciplinas: cadastro, edição, exclusão e listagem.
 */
public class AbaDisciplinas extends JPanel implements AbaAtualizavel {

    private final DisciplinaController disciplinaController = new DisciplinaController();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Disciplina", "Nível de Ensino"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private final JTextField campoId = Campos.campoSomenteLeitura();
    private final JTextField campoNome = Campos.campoTexto(20);
    private final JComboBox<NivelEnsino> comboNivelEnsino = Campos.comboEnum(NivelEnsino.values());

    private final JButton botaoAdicionar = new JButton("Adicionar");
    private final JButton botaoEditar = new JButton("Editar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");

    private Integer idEmEdicao = null;

    public AbaDisciplinas() {
        setLayout(new BorderLayout());
        setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(this, 16, 16, 16, 16);

        Tema.estilizarTabela(tabela);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                carregarParaEdicao(tabela.getSelectedRow());
            }
        });

        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBackground(Tema.BRANCO);
        painelEsquerdo.add(Tema.titulo("Disciplinas cadastradas"), BorderLayout.NORTH);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createLineBorder(Tema.CINZA_MEDIO));
        painelEsquerdo.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelDireito = montarFormulario();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setResizeWeight(0.65);
        splitPane.setBorder(null);
        splitPane.setDividerSize(10);

        add(splitPane, BorderLayout.CENTER);

        limparFormulario();
    }

    private JPanel montarFormulario() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(container, 0, 16, 0, 0);
        container.add(Tema.titulo("Dados da disciplina"), BorderLayout.NORTH);

        PainelFormulario formulario = new PainelFormulario();
        formulario.adicionarLinha("ID", campoId);
        formulario.adicionarLinha("Disciplina", campoNome);
        formulario.adicionarLinha("Nível de Ensino", comboNivelEnsino);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelBotoes.setBackground(Tema.BRANCO);
        Tema.botaoPrimario(botaoAdicionar);
        Tema.botaoPrimario(botaoEditar);
        Tema.botaoPerigo(botaoExcluir);
        Tema.botaoSecundario(botaoLimpar);
        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoLimpar);
        formulario.adicionarLinhaCompleta(painelBotoes);

        botaoAdicionar.addActionListener(e -> adicionar());
        botaoEditar.addActionListener(e -> editar());
        botaoExcluir.addActionListener(e -> excluir());
        botaoLimpar.addActionListener(e -> limparFormulario());

        JScrollPane scrollFormulario = new JScrollPane(formulario);
        scrollFormulario.setBorder(null);
        scrollFormulario.getVerticalScrollBar().setUnitIncrement(16);
        container.add(scrollFormulario, BorderLayout.CENTER);
        return container;
    }

    @Override
    public void atualizarDados() {
        carregarTabela();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Disciplina> disciplinas = disciplinaController.listarDisciplina();

        for (Disciplina d : disciplinas) {
            modeloTabela.addRow(new Object[]{d.getId(), d.getDisciplina(), d.getNivelEnsino().getDescricao()});
        }
    }

    private void carregarParaEdicao(int linha) {
        List<Disciplina> disciplinas = disciplinaController.listarDisciplina();
        if (linha >= disciplinas.size()) return;

        Disciplina d = disciplinas.get(linha);
        idEmEdicao = d.getId();

        campoId.setText(String.valueOf(d.getId()));
        campoNome.setText(d.getDisciplina());
        comboNivelEnsino.setSelectedItem(d.getNivelEnsino());

        botaoAdicionar.setEnabled(false);
        botaoEditar.setEnabled(true);
        botaoExcluir.setEnabled(true);
    }

    private void adicionar() {
        try {
            disciplinaController.adicionarDisciplina(
                    campoNome.getText().trim(),
                    (NivelEnsino) comboNivelEnsino.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Disciplina adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            atualizarDados();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (idEmEdicao == null) return;
        try {
            disciplinaController.editarDisciplina(
                    idEmEdicao,
                    campoNome.getText().trim(),
                    (NivelEnsino) comboNivelEnsino.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Disciplina atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            atualizarDados();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idEmEdicao == null) return;
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a disciplina de ID " + idEmEdicao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            disciplinaController.excluirDisciplina(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Disciplina excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            atualizarDados();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        idEmEdicao = null;
        campoId.setText("(gerado automaticamente)");
        campoNome.setText("");
        comboNivelEnsino.setSelectedIndex(0);

        botaoAdicionar.setEnabled(true);
        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
        tabela.clearSelection();
    }
}
