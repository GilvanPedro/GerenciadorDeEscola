package br.com.view;

import br.com.controller.AlunoController;
import br.com.controller.ResponsavelController;
import br.com.controller.TurmaController;
import br.com.model.dto.AlunoExibicao;
import br.com.model.dto.ResponsavelExibicao;
import br.com.model.dto.TurmaExibicao;
import br.com.model.entity.Aluno;
import br.com.model.entity.Responsavel;
import br.com.model.enums.Series;
import br.com.model.enums.SituacaoAluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Aba de gerenciamento de Alunos: cadastro, edição, exclusão e listagem.
 */
public class AbaAlunos extends JPanel implements AbaAtualizavel {

    private final AlunoController alunoController = new AlunoController();
    private final TurmaController turmaController = new TurmaController();
    private final ResponsavelController responsavelController = new ResponsavelController();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"Matrícula", "Nome", "CPF", "Data Nasc.", "Série", "Turma", "Situação", "Responsáveis"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private final JTextField campoMatricula = Campos.campoSomenteLeitura();
    private final JTextField campoNome = Campos.campoTexto(20);
    private final JFormattedTextField campoCpf = Campos.campoCpf();
    private final JFormattedTextField campoDataNascimento = Campos.campoData();
    private final JComboBox<Series> comboSerie = Campos.comboEnum(Series.values());
    private final JComboBox<ItemSelecao> comboTurma = Campos.comboItens();
    private final JComboBox<SituacaoAluno> comboSituacao = Campos.comboEnum(SituacaoAluno.values());
    private final ListaComCheckbox listaResponsaveis = Campos.listaComCheckbox();

    private final JButton botaoAdicionar = new JButton("Adicionar");
    private final JButton botaoEditar = new JButton("Editar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");

    private Integer matriculaEmEdicao = null;
    private List<TurmaExibicao> turmasCache = new ArrayList<>();
    private List<ResponsavelExibicao> responsaveisCache = new ArrayList<>();

    public AbaAlunos() {
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
        painelEsquerdo.add(Tema.titulo("Alunos cadastrados"), BorderLayout.NORTH);
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
        container.add(Tema.titulo("Dados do aluno"), BorderLayout.NORTH);

        PainelFormulario formulario = new PainelFormulario();
        formulario.adicionarLinha("Matrícula", campoMatricula);
        formulario.adicionarLinha("Nome", campoNome);
        formulario.adicionarLinha("CPF", campoCpf);
        formulario.adicionarLinha("Data de Nascimento", campoDataNascimento);
        formulario.adicionarLinha("Série", comboSerie);
        formulario.adicionarLinha("Turma", comboTurma);
        formulario.adicionarLinha("Situação", comboSituacao);
        formulario.adicionarLinhaAlta("Responsáveis", Campos.comRolagem(listaResponsaveis), 100);

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
        turmasCache = turmaController.listarTurma();
        responsaveisCache = responsavelController.listarResponsaveis();

        comboTurma.removeAllItems();
        for (TurmaExibicao t : turmasCache) {
            comboTurma.addItem(new ItemSelecao(t.getTurma().getId(),
                    t.getTurma().getId() + " - " + t.getTurma().getSerie().getDescricao()
                            + " (" + t.getTurma().getTurno().getDescricao() + " / " + t.getTurma().getAnoLetivo() + ")"));
        }

        List<ItemSelecao> itensResponsaveis = new ArrayList<>();
        for (ResponsavelExibicao r : responsaveisCache) {
            itensResponsaveis.add(new ItemSelecao(r.getResponsavel().getId(),
                    r.getResponsavel().getId() + " - " + r.getResponsavel().getNome()));
        }
        listaResponsaveis.definirItens(itensResponsaveis);

        carregarTabela();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<AlunoExibicao> alunos = alunoController.listarAlunos();

        for (AlunoExibicao ae : alunos) {
            Aluno a = ae.getAluno();

            String nomeTurma = turmasCache.stream()
                    .filter(t -> t.getTurma().getId() == a.getTurmaId())
                    .map(t -> t.getTurma().getSerie().getDescricao() + " (" + t.getTurma().getTurno().getDescricao() + ")")
                    .findFirst()
                    .orElse("—");

            StringBuilder nomesResponsaveis = new StringBuilder();
            for (Responsavel r : ae.getResponsaveis()) {
                if (nomesResponsaveis.length() > 0) nomesResponsaveis.append(", ");
                nomesResponsaveis.append(r.getNome());
            }

            modeloTabela.addRow(new Object[]{
                    a.getMatricula(),
                    a.getNome(),
                    a.getCpf(),
                    a.getDataNascimentoFormatada(),
                    a.getSerie(),
                    nomeTurma,
                    a.getSituacao().getDescricao(),
                    nomesResponsaveis.toString()
            });
        }
    }

    private void carregarParaEdicao(int linha) {
        List<AlunoExibicao> alunos = alunoController.listarAlunos();
        if (linha >= alunos.size()) return;

        AlunoExibicao ae = alunos.get(linha);
        Aluno a = ae.getAluno();

        matriculaEmEdicao = a.getMatricula();
        campoMatricula.setText(String.valueOf(a.getMatricula()));
        campoNome.setText(a.getNome());
        campoCpf.setText(a.getCpf());
        campoDataNascimento.setText(a.getDataNascimentoFormatada());
        campoCpf.setEditable(false);
        campoDataNascimento.setEditable(false);

        selecionarEnumNoCombo(comboSerie, a.getSerie() != null ? buscarSeriePorDescricaoOuNome(a.getSerie()) : null);
        selecionarItemNoCombo(comboTurma, a.getTurmaId());
        comboSituacao.setSelectedItem(a.getSituacao());

        listaResponsaveis.marcar(a.getResponsavelId());

        botaoAdicionar.setEnabled(false);
        botaoEditar.setEnabled(true);
        botaoExcluir.setEnabled(true);
    }

    // A Turma guarda a série do aluno como texto simples (String), então tentamos
    // localizar o enum correspondente comparando o "name()" com o valor salvo.
    private Series buscarSeriePorDescricaoOuNome(String valor) {
        for (Series s : Series.values()) {
            if (s.name().equalsIgnoreCase(valor) || s.getDescricao().equalsIgnoreCase(valor)) {
                return s;
            }
        }
        return null;
    }

    private <E> void selecionarEnumNoCombo(JComboBox<E> combo, E valor) {
        if (valor != null) {
            combo.setSelectedItem(valor);
        }
    }

    private void selecionarItemNoCombo(JComboBox<ItemSelecao> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void adicionar() {
        try {
            ItemSelecao turmaSelecionada = (ItemSelecao) comboTurma.getSelectedItem();
            if (turmaSelecionada == null) {
                throw new IllegalArgumentException("Erro: cadastre uma turma antes de cadastrar um aluno.");
            }

            alunoController.adicionarAluno(
                    campoNome.getText().trim(),
                    Campos.cpfSemMascara(campoCpf.getText()),
                    campoDataNascimento.getText().trim(),
                    ((Series) comboSerie.getSelectedItem()).name(),
                    turmaSelecionada.getId(),
                    (SituacaoAluno) comboSituacao.getSelectedItem(),
                    listaResponsaveis.obterMarcados()
            );

            JOptionPane.showMessageDialog(this, "Aluno adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            atualizarDados();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (matriculaEmEdicao == null) return;
        try {
            ItemSelecao turmaSelecionada = (ItemSelecao) comboTurma.getSelectedItem();
            if (turmaSelecionada == null) {
                throw new IllegalArgumentException("Erro: selecione uma turma.");
            }

            alunoController.editarAluno(
                    matriculaEmEdicao,
                    campoNome.getText().trim(),
                    ((Series) comboSerie.getSelectedItem()).name(),
                    turmaSelecionada.getId(),
                    (SituacaoAluno) comboSituacao.getSelectedItem(),
                    listaResponsaveis.obterMarcados()
            );

            JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            atualizarDados();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (matriculaEmEdicao == null) return;
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o aluno de matrícula " + matriculaEmEdicao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            alunoController.excluirAluno(matriculaEmEdicao);
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            atualizarDados();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        matriculaEmEdicao = null;
        campoMatricula.setText("(gerada automaticamente)");
        campoNome.setText("");
        campoCpf.setValue(null);
        campoCpf.setText("");
        campoCpf.setEditable(true);
        campoDataNascimento.setValue(null);
        campoDataNascimento.setText("");
        campoDataNascimento.setEditable(true);
        comboSerie.setSelectedIndex(0);
        if (comboTurma.getItemCount() > 0) comboTurma.setSelectedIndex(0);
        comboSituacao.setSelectedIndex(0);
        listaResponsaveis.desmarcarTodos();

        botaoAdicionar.setEnabled(true);
        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
        tabela.clearSelection();
    }
}
