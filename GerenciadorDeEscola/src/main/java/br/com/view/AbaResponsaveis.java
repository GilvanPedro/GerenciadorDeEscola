package br.com.view;

import br.com.controller.AlunoController;
import br.com.controller.ResponsavelController;
import br.com.model.dto.AlunoExibicao;
import br.com.model.dto.ResponsavelExibicao;
import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Aba de gerenciamento de Responsáveis: cadastro, edição, exclusão e listagem.
 */
public class AbaResponsaveis extends JPanel implements AbaAtualizavel {

    private final ResponsavelController responsavelController = new ResponsavelController();
    private final AlunoController alunoController = new AlunoController();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Nome", "CPF", "Data Nasc.", "Endereço", "Telefone", "Alunos vinculados"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private final JTextField campoId = Campos.campoSomenteLeitura();
    private final JTextField campoNome = Campos.campoTexto(20);
    private final JFormattedTextField campoCpf = Campos.campoCpf();
    private final JFormattedTextField campoDataNascimento = Campos.campoData();
    private final JTextField campoEndereco = Campos.campoTexto(20);
    private final JTextField campoTelefone = Campos.campoTexto(20);
    private final ListaComCheckbox listaAlunos = Campos.listaComCheckbox();

    private final JButton botaoAdicionar = new JButton("Adicionar");
    private final JButton botaoEditar = new JButton("Editar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");

    private Integer idEmEdicao = null;

    public AbaResponsaveis() {
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
        painelEsquerdo.add(Tema.titulo("Responsáveis cadastrados"), BorderLayout.NORTH);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createLineBorder(Tema.CINZA_MEDIO));
        painelEsquerdo.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelDireito = montarFormulario();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setResizeWeight(0.62);
        splitPane.setBorder(null);
        splitPane.setDividerSize(10);

        add(splitPane, BorderLayout.CENTER);

        limparFormulario();
    }

    private JPanel montarFormulario() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(container, 0, 16, 0, 0);
        container.add(Tema.titulo("Dados do responsável"), BorderLayout.NORTH);

        PainelFormulario formulario = new PainelFormulario();
        formulario.adicionarLinha("ID", campoId);
        formulario.adicionarLinha("Nome", campoNome);
        formulario.adicionarLinha("CPF", campoCpf);
        formulario.adicionarLinha("Data de Nascimento", campoDataNascimento);
        formulario.adicionarLinha("Endereço", campoEndereco);
        formulario.adicionarLinha("Telefone", campoTelefone);
        formulario.adicionarLinhaAlta("Alunos vinculados", Campos.comRolagem(listaAlunos), 110);

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
        List<AlunoExibicao> alunos = alunoController.listarAlunos();

        List<ItemSelecao> itensAlunos = new ArrayList<>();
        for (AlunoExibicao ae : alunos) {
            Aluno a = ae.getAluno();
            itensAlunos.add(new ItemSelecao(a.getMatricula(), a.getMatricula() + " - " + a.getNome()));
        }
        listaAlunos.definirItens(itensAlunos);

        carregarTabela();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<ResponsavelExibicao> responsaveis = responsavelController.listarResponsaveis();

        for (ResponsavelExibicao re : responsaveis) {
            Responsavel r = re.getResponsavel();
            StringBuilder alunos = new StringBuilder();
            for (Aluno a : re.getAlunos()) {
                if (alunos.length() > 0) alunos.append(", ");
                alunos.append(a.getNome());
            }

            modeloTabela.addRow(new Object[]{
                    r.getId(), r.getNome(), r.getCpf(), r.getDataNascimentoFormatada(),
                    r.getEndereco(), r.getTelefone(), alunos.toString()
            });
        }
    }

    private void carregarParaEdicao(int linha) {
        List<ResponsavelExibicao> responsaveis = responsavelController.listarResponsaveis();
        if (linha >= responsaveis.size()) return;

        Responsavel r = responsaveis.get(linha).getResponsavel();
        idEmEdicao = r.getId();

        campoId.setText(String.valueOf(r.getId()));
        campoNome.setText(r.getNome());
        campoCpf.setText(r.getCpf());
        campoCpf.setEditable(false);
        campoDataNascimento.setText(r.getDataNascimentoFormatada());
        campoDataNascimento.setEditable(false);
        campoEndereco.setText(r.getEndereco());
        campoTelefone.setText(r.getTelefone());

        listaAlunos.marcar(r.getAlunosId());

        botaoAdicionar.setEnabled(false);
        botaoEditar.setEnabled(true);
        botaoExcluir.setEnabled(true);
    }

    private void adicionar() {
        try {
            responsavelController.adicionarResponsavel(
                    campoNome.getText().trim(),
                    Campos.cpfSemMascara(campoCpf.getText()),
                    campoDataNascimento.getText().trim(),
                    campoEndereco.getText().trim(),
                    campoTelefone.getText().trim(),
                    listaAlunos.obterMarcados()
            );

            JOptionPane.showMessageDialog(this, "Responsável adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
            responsavelController.editarResponsavel(
                    idEmEdicao,
                    campoNome.getText().trim(),
                    campoEndereco.getText().trim(),
                    campoTelefone.getText().trim(),
                    listaAlunos.obterMarcados()
            );

            JOptionPane.showMessageDialog(this, "Responsável atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
                "Tem certeza que deseja excluir o responsável de ID " + idEmEdicao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            responsavelController.excluirResponsavel(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Responsável excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
        campoCpf.setValue(null);
        campoCpf.setText("");
        campoCpf.setEditable(true);
        campoDataNascimento.setValue(null);
        campoDataNascimento.setText("");
        campoDataNascimento.setEditable(true);
        campoEndereco.setText("");
        campoTelefone.setText("");
        listaAlunos.desmarcarTodos();

        botaoAdicionar.setEnabled(true);
        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
        tabela.clearSelection();
    }
}
