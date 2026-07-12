package br.com.view;

import br.com.controller.AlunoController;
import br.com.controller.DisciplinaController;
import br.com.controller.NotaController;
import br.com.controller.TurmaController;
import br.com.model.dto.AlunoExibicao;
import br.com.model.dto.TurmaExibicao;
import br.com.model.entity.Aluno;
import br.com.model.entity.Disciplina;
import br.com.model.entity.Nota;
import br.com.model.entity.Turma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Aba de gerenciamento de Notas: primeiro você escolhe a turma, depois vê,
 * lança e edita as notas apenas dos alunos matriculados nela.
 */
public class AbaNotas extends JPanel implements AbaAtualizavel {

    private final NotaController notaController = new NotaController();
    private final AlunoController alunoController = new AlunoController();
    private final DisciplinaController disciplinaController = new DisciplinaController();
    private final TurmaController turmaController = new TurmaController();

    private final JComboBox<ItemSelecao> comboTurma = Campos.comboItens();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Aluno", "Disciplina", "Bimestre", "Nota"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private final JTextField campoId = Campos.campoSomenteLeitura();
    private final JComboBox<ItemSelecao> comboAluno = Campos.comboItens();
    private final JComboBox<ItemSelecao> comboDisciplina = Campos.comboItens();
    private final JComboBox<Integer> comboBimestre = new JComboBox<>(new Integer[]{1, 2, 3, 4});
    private final JSpinner spinnerValor = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10.0, 0.1));

    private final JButton botaoAdicionar = new JButton("Lançar Nota");
    private final JButton botaoEditar = new JButton("Editar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");

    private Integer idEmEdicao = null;
    private List<Disciplina> disciplinasCache = new ArrayList<>();
    private List<AlunoExibicao> alunosDaTurmaCache = new ArrayList<>();

    public AbaNotas() {
        setLayout(new BorderLayout());
        setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(this, 16, 16, 16, 16);

        comboBimestre.setFont(Tema.FONTE_BASE);
        spinnerValor.setFont(Tema.FONTE_BASE);

        Tema.estilizarTabela(tabela);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                carregarParaEdicao(tabela.getSelectedRow());
            }
        });

        JPanel topo = montarSeletorTurma();
        add(topo, BorderLayout.NORTH);

        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(painelEsquerdo, 12, 0, 0, 0);
        painelEsquerdo.add(Tema.titulo("Notas da turma"), BorderLayout.NORTH);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createLineBorder(Tema.CINZA_MEDIO));
        painelEsquerdo.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelDireito = montarFormulario();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);
        splitPane.setDividerSize(10);

        add(splitPane, BorderLayout.CENTER);

        habilitarPainel(false);
    }

    private JPanel montarSeletorTurma() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Tema.BRANCO);
        painel.add(Tema.titulo("Selecione a turma"), BorderLayout.NORTH);

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        linha.setBackground(Tema.BRANCO);
        linha.add(Tema.rotulo("Turma"));
        comboTurma.setPreferredSize(new Dimension(280, comboTurma.getPreferredSize().height));
        linha.add(comboTurma);
        painel.add(linha, BorderLayout.CENTER);

        comboTurma.addActionListener(e -> carregarTurmaSelecionada());

        return painel;
    }

    private JPanel montarFormulario() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(container, 12, 16, 0, 0);
        container.add(Tema.titulo("Lançamento de nota"), BorderLayout.NORTH);

        PainelFormulario formulario = new PainelFormulario();
        formulario.adicionarLinha("ID", campoId);
        formulario.adicionarLinha("Aluno", comboAluno);
        formulario.adicionarLinha("Disciplina", comboDisciplina);
        formulario.adicionarLinha("Bimestre", comboBimestre);
        formulario.adicionarLinha("Valor da Nota (0-10)", spinnerValor);

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
        ItemSelecao turmaSelecionadaAnterior = (ItemSelecao) comboTurma.getSelectedItem();

        List<TurmaExibicao> turmas = turmaController.listarTurma();
        comboTurma.removeAllItems();
        for (TurmaExibicao te : turmas) {
            Turma t = te.getTurma();
            comboTurma.addItem(new ItemSelecao(t.getId(),
                    t.getId() + " - " + t.getSerie().getDescricao() + " (" + t.getTurno().getDescricao() + " / " + t.getAnoLetivo() + ")"));
        }

        disciplinasCache = disciplinaController.listarDisciplina();

        if (turmaSelecionadaAnterior != null) {
            selecionarItemNoCombo(comboTurma, turmaSelecionadaAnterior.getId());
        }

        carregarTurmaSelecionada();
    }

    private void carregarTurmaSelecionada() {
        ItemSelecao turmaSelecionada = (ItemSelecao) comboTurma.getSelectedItem();

        if (turmaSelecionada == null) {
            alunosDaTurmaCache = new ArrayList<>();
            comboAluno.removeAllItems();
            comboDisciplina.removeAllItems();
            modeloTabela.setRowCount(0);
            habilitarPainel(false);
            return;
        }

        List<AlunoExibicao> todosAlunos = alunoController.listarAlunos();
        alunosDaTurmaCache = todosAlunos.stream()
                .filter(ae -> ae.getAluno().getTurmaId() == turmaSelecionada.getId())
                .toList();

        comboAluno.removeAllItems();
        for (AlunoExibicao ae : alunosDaTurmaCache) {
            Aluno a = ae.getAluno();
            comboAluno.addItem(new ItemSelecao(a.getMatricula(), a.getMatricula() + " - " + a.getNome()));
        }

        comboDisciplina.removeAllItems();
        for (Disciplina d : disciplinasCache) {
            comboDisciplina.addItem(new ItemSelecao(d.getId(), d.getId() + " - " + d.getDisciplina()));
        }

        habilitarPainel(!alunosDaTurmaCache.isEmpty());
        carregarTabela();
    }

    private void habilitarPainel(boolean habilitado) {
        comboAluno.setEnabled(habilitado);
        comboDisciplina.setEnabled(habilitado);
        comboBimestre.setEnabled(habilitado);
        spinnerValor.setEnabled(habilitado);
        botaoAdicionar.setEnabled(habilitado);
        tabela.setEnabled(habilitado);
        if (!habilitado) {
            botaoEditar.setEnabled(false);
            botaoExcluir.setEnabled(false);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<Integer> matriculasDaTurma = alunosDaTurmaCache.stream()
                .map(ae -> ae.getAluno().getMatricula())
                .toList();

        List<Nota> notas = notaController.listarNotas().stream()
                .filter(n -> matriculasDaTurma.contains(n.getAlunoMatricula()))
                .toList();

        for (Nota n : notas) {
            String nomeAluno = alunosDaTurmaCache.stream()
                    .filter(ae -> ae.getAluno().getMatricula() == n.getAlunoMatricula())
                    .map(ae -> ae.getAluno().getNome())
                    .findFirst()
                    .orElse("Matrícula " + n.getAlunoMatricula());

            String nomeDisciplina = disciplinasCache.stream()
                    .filter(d -> d.getId() == n.getDisciplinaId())
                    .map(Disciplina::getDisciplina)
                    .findFirst()
                    .orElse("ID " + n.getDisciplinaId());

            modeloTabela.addRow(new Object[]{n.getId(), nomeAluno, nomeDisciplina, n.getBimestre() + "º Bimestre", n.getValor()});
        }
    }

    private void carregarParaEdicao(int linha) {
        List<Integer> matriculasDaTurma = alunosDaTurmaCache.stream()
                .map(ae -> ae.getAluno().getMatricula())
                .toList();

        List<Nota> notas = notaController.listarNotas().stream()
                .filter(n -> matriculasDaTurma.contains(n.getAlunoMatricula()))
                .toList();
        if (linha >= notas.size()) return;

        Nota n = notas.get(linha);
        idEmEdicao = n.getId();

        campoId.setText(String.valueOf(n.getId()));
        selecionarItemNoCombo(comboAluno, n.getAlunoMatricula());
        selecionarItemNoCombo(comboDisciplina, n.getDisciplinaId());
        comboBimestre.setSelectedItem(n.getBimestre());
        spinnerValor.setValue(n.getValor());

        botaoAdicionar.setEnabled(false);
        botaoEditar.setEnabled(true);
        botaoExcluir.setEnabled(true);
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
            ItemSelecao aluno = (ItemSelecao) comboAluno.getSelectedItem();
            ItemSelecao disciplina = (ItemSelecao) comboDisciplina.getSelectedItem();
            if (aluno == null || disciplina == null) {
                throw new IllegalArgumentException("Erro: selecione uma turma com alunos e cadastre disciplinas antes de lançar notas.");
            }

            notaController.lancarNota(
                    aluno.getId(),
                    disciplina.getId(),
                    (Integer) comboBimestre.getSelectedItem(),
                    (Double) spinnerValor.getValue()
            );

            JOptionPane.showMessageDialog(this, "Nota lançada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarTabela();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (idEmEdicao == null) return;
        try {
            ItemSelecao aluno = (ItemSelecao) comboAluno.getSelectedItem();
            ItemSelecao disciplina = (ItemSelecao) comboDisciplina.getSelectedItem();
            if (aluno == null || disciplina == null) {
                throw new IllegalArgumentException("Erro: selecione um aluno e uma disciplina.");
            }

            notaController.editarNota(
                    idEmEdicao,
                    aluno.getId(),
                    disciplina.getId(),
                    (Integer) comboBimestre.getSelectedItem(),
                    (Double) spinnerValor.getValue()
            );

            JOptionPane.showMessageDialog(this, "Nota atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarTabela();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idEmEdicao == null) return;
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a nota de ID " + idEmEdicao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            notaController.excluirNota(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Nota excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarTabela();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        idEmEdicao = null;
        campoId.setText("(gerado automaticamente)");
        if (comboAluno.getItemCount() > 0) comboAluno.setSelectedIndex(0);
        if (comboDisciplina.getItemCount() > 0) comboDisciplina.setSelectedIndex(0);
        comboBimestre.setSelectedIndex(0);
        spinnerValor.setValue(0.0);

        botaoAdicionar.setEnabled(!alunosDaTurmaCache.isEmpty());
        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
        tabela.clearSelection();
    }
}
