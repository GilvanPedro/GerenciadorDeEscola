package br.com.view;

import br.com.controller.AlunoController;
import br.com.controller.TurmaController;
import br.com.model.dto.AlunoExibicao;
import br.com.model.dto.TurmaExibicao;
import br.com.model.entity.Aluno;
import br.com.model.entity.Turma;
import br.com.model.enums.NivelEnsino;
import br.com.model.enums.Series;
import br.com.model.enums.Turnos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Aba de gerenciamento de Turmas: cadastro, edição, exclusão e listagem.
 *
 * O vínculo aluno-turma é feito principalmente pelo cadastro/edição do Aluno
 * (que escolhe a turma dele). Aqui, ao selecionar uma turma, é exibida a
 * lista de alunos matriculados nela, com ações rápidas para adicionar um
 * aluno já existente ou remover um aluno da turma.
 */
public class AbaTurmas extends JPanel implements AbaAtualizavel {

    private final TurmaController turmaController = new TurmaController();
    private final AlunoController alunoController = new AlunoController();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Série", "Turno", "Nível de Ensino", "Ano Letivo", "Alunos"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private final JTextField campoId = Campos.campoSomenteLeitura();
    private final JComboBox<Series> comboSerie = Campos.comboEnum(Series.values());
    private final JComboBox<Turnos> comboTurno = Campos.comboEnum(Turnos.values());
    private final JComboBox<NivelEnsino> comboNivelEnsino = Campos.comboEnum(NivelEnsino.values());
    private final JSpinner spinnerAnoLetivo = new JSpinner(new SpinnerNumberModel(Year.now().getValue(), 2000, 2100, 1));

    private final JButton botaoAdicionar = new JButton("Adicionar");
    private final JButton botaoEditar = new JButton("Editar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");

    // Painel de alunos matriculados na turma selecionada
    private final DefaultListModel<ItemSelecao> modeloAlunosDaTurma = new DefaultListModel<>();
    private final JList<ItemSelecao> listaAlunosDaTurma = new JList<>(modeloAlunosDaTurma);
    private final JComboBox<ItemSelecao> comboAlunoParaAdicionar = Campos.comboItens();
    private final JButton botaoAdicionarAluno = new JButton("Adicionar à turma");
    private final JButton botaoRemoverAluno = new JButton("Remover da turma");
    private final JLabel rotuloStatusTurma = Tema.rotulo("Selecione uma turma na tabela para gerenciar os alunos dela.");

    private Integer idEmEdicao = null;
    private List<AlunoExibicao> alunosCache = new ArrayList<>();

    public AbaTurmas() {
        setLayout(new BorderLayout());
        setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(this, 16, 16, 16, 16);
        spinnerAnoLetivo.setFont(Tema.FONTE_BASE);

        Tema.estilizarTabela(tabela);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                carregarParaEdicao(tabela.getSelectedRow());
            }
        });

        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBackground(Tema.BRANCO);
        painelEsquerdo.add(Tema.titulo("Turmas cadastradas"), BorderLayout.NORTH);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createLineBorder(Tema.CINZA_MEDIO));
        painelEsquerdo.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelDireito = montarFormulario();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setResizeWeight(0.55);
        splitPane.setBorder(null);
        splitPane.setDividerSize(10);

        add(splitPane, BorderLayout.CENTER);

        limparFormulario();
    }

    private JPanel montarFormulario() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(container, 0, 16, 0, 0);

        JPanel painelDados = new JPanel(new BorderLayout());
        painelDados.setBackground(Tema.BRANCO);
        painelDados.add(Tema.titulo("Dados da turma"), BorderLayout.NORTH);

        PainelFormulario formulario = new PainelFormulario();
        formulario.adicionarLinha("ID", campoId);
        formulario.adicionarLinha("Série", comboSerie);
        formulario.adicionarLinha("Turno", comboTurno);
        formulario.adicionarLinha("Nível de Ensino", comboNivelEnsino);
        formulario.adicionarLinha("Ano Letivo", spinnerAnoLetivo);

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

        painelDados.add(formulario, BorderLayout.CENTER);

        JPanel painelAlunos = montarPainelAlunosDaTurma();

        container.add(painelDados, BorderLayout.NORTH);
        container.add(painelAlunos, BorderLayout.CENTER);
        return container;
    }

    private JPanel montarPainelAlunosDaTurma() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(painel, 16, 0, 0, 0);

        painel.add(Tema.titulo("Alunos matriculados nesta turma"), BorderLayout.NORTH);

        listaAlunosDaTurma.setFont(Tema.FONTE_BASE);
        listaAlunosDaTurma.setBackground(Tema.BRANCO);
        listaAlunosDaTurma.setSelectionBackground(Tema.ACCENT_CLARO);
        listaAlunosDaTurma.setSelectionForeground(Tema.TEXTO_PRINCIPAL);
        JScrollPane scrollLista = Campos.comRolagem(listaAlunosDaTurma);
        Tema.aplicarEspacamento(scrollLista, 8, 0, 8, 0);
        painel.add(scrollLista, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel();
        painelInferior.setLayout(new BoxLayout(painelInferior, BoxLayout.Y_AXIS));
        painelInferior.setBackground(Tema.BRANCO);

        rotuloStatusTurma.setFont(Tema.FONTE_BASE);
        JPanel painelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        painelStatus.setBackground(Tema.BRANCO);
        painelStatus.add(rotuloStatusTurma);
        painelInferior.add(painelStatus);

        JPanel painelAdicionar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        painelAdicionar.setBackground(Tema.BRANCO);
        comboAlunoParaAdicionar.setPreferredSize(new Dimension(220, comboAlunoParaAdicionar.getPreferredSize().height));
        Tema.botaoPrimario(botaoAdicionarAluno);
        painelAdicionar.add(comboAlunoParaAdicionar);
        painelAdicionar.add(botaoAdicionarAluno);
        painelInferior.add(painelAdicionar);

        JPanel painelRemover = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        painelRemover.setBackground(Tema.BRANCO);
        Tema.botaoPerigo(botaoRemoverAluno);
        painelRemover.add(botaoRemoverAluno);
        painelInferior.add(painelRemover);

        painel.add(painelInferior, BorderLayout.SOUTH);

        botaoAdicionarAluno.addActionListener(e -> adicionarAlunoATurma());
        botaoRemoverAluno.addActionListener(e -> removerAlunoDaTurma());

        return painel;
    }

    @Override
    public void atualizarDados() {
        alunosCache = alunoController.listarAlunos();
        carregarTabela();

        if (idEmEdicao != null) {
            atualizarPainelAlunosDaTurma(idEmEdicao);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<TurmaExibicao> turmas = turmaController.listarTurma();

        for (TurmaExibicao te : turmas) {
            Turma t = te.getTurma();
            StringBuilder alunos = new StringBuilder();
            for (Aluno a : te.getAlunos()) {
                if (alunos.length() > 0) alunos.append(", ");
                alunos.append(a.getNome());
            }

            modeloTabela.addRow(new Object[]{
                    t.getId(), t.getSerie().getDescricao(), t.getTurno().getDescricao(),
                    t.getNivelEnsino().getDescricao(), t.getAnoLetivo(), alunos.toString()
            });
        }
    }

    private void carregarParaEdicao(int linha) {
        List<TurmaExibicao> turmas = turmaController.listarTurma();
        if (linha >= turmas.size()) return;

        Turma t = turmas.get(linha).getTurma();
        idEmEdicao = t.getId();

        campoId.setText(String.valueOf(t.getId()));
        comboSerie.setSelectedItem(t.getSerie());
        comboTurno.setSelectedItem(t.getTurno());
        comboNivelEnsino.setSelectedItem(t.getNivelEnsino());
        spinnerAnoLetivo.setValue(t.getAnoLetivo());

        botaoAdicionar.setEnabled(false);
        botaoEditar.setEnabled(true);
        botaoExcluir.setEnabled(true);

        atualizarPainelAlunosDaTurma(idEmEdicao);
    }

    /** Recarrega a lista de alunos matriculados na turma informada e o combo de alunos disponíveis para adicionar. */
    private void atualizarPainelAlunosDaTurma(int turmaId) {
        List<TurmaExibicao> turmas = turmaController.listarTurma();
        TurmaExibicao turmaExibicao = turmas.stream()
                .filter(te -> te.getTurma().getId() == turmaId)
                .findFirst()
                .orElse(null);

        modeloAlunosDaTurma.clear();
        comboAlunoParaAdicionar.removeAllItems();

        if (turmaExibicao == null) {
            rotuloStatusTurma.setText("Turma não encontrada.");
            habilitarAcoesDeAlunos(false);
            return;
        }

        List<Integer> matriculasNaTurma = new ArrayList<>();
        for (Aluno a : turmaExibicao.getAlunos()) {
            modeloAlunosDaTurma.addElement(new ItemSelecao(a.getMatricula(), a.getMatricula() + " - " + a.getNome()));
            matriculasNaTurma.add(a.getMatricula());
        }

        for (AlunoExibicao ae : alunosCache) {
            Aluno a = ae.getAluno();
            if (!matriculasNaTurma.contains(a.getMatricula())) {
                comboAlunoParaAdicionar.addItem(new ItemSelecao(a.getMatricula(), a.getMatricula() + " - " + a.getNome()));
            }
        }

        rotuloStatusTurma.setText(matriculasNaTurma.isEmpty()
                ? "Nenhum aluno matriculado nesta turma ainda."
                : matriculasNaTurma.size() + " aluno(s) matriculado(s) nesta turma.");
        habilitarAcoesDeAlunos(true);
    }

    private void habilitarAcoesDeAlunos(boolean habilitado) {
        listaAlunosDaTurma.setEnabled(habilitado);
        comboAlunoParaAdicionar.setEnabled(habilitado);
        botaoAdicionarAluno.setEnabled(habilitado);
        botaoRemoverAluno.setEnabled(habilitado);
    }

    private void adicionarAlunoATurma() {
        if (idEmEdicao == null) return;
        ItemSelecao alunoSelecionado = (ItemSelecao) comboAlunoParaAdicionar.getSelectedItem();
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Não há alunos disponíveis para adicionar (todos já estão nesta turma ou não há alunos cadastrados).", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            turmaController.adicionarAlunoNaTurma(idEmEdicao, alunoSelecionado.getId());
            atualizarDados();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerAlunoDaTurma() {
        if (idEmEdicao == null) return;
        ItemSelecao alunoSelecionado = listaAlunosDaTurma.getSelectedValue();
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na lista para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Remover " + alunoSelecionado + " desta turma?",
                "Confirmar remoção", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            turmaController.removerAlunoDaTurma(idEmEdicao, alunoSelecionado.getId());
            atualizarDados();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionar() {
        try {
            turmaController.adicionarTurma(
                    (Series) comboSerie.getSelectedItem(),
                    (Turnos) comboTurno.getSelectedItem(),
                    (NivelEnsino) comboNivelEnsino.getSelectedItem(),
                    (Integer) spinnerAnoLetivo.getValue(),
                    null
            );

            JOptionPane.showMessageDialog(this, "Turma adicionada com sucesso! Agora você pode matricular alunos nela pelo cadastro/edição do Aluno, ou pela lista abaixo.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
            turmaController.editarTurma(
                    idEmEdicao,
                    (Series) comboSerie.getSelectedItem(),
                    (Turnos) comboTurno.getSelectedItem(),
                    (NivelEnsino) comboNivelEnsino.getSelectedItem(),
                    (Integer) spinnerAnoLetivo.getValue(),
                    null
            );

            JOptionPane.showMessageDialog(this, "Turma atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
                "Tem certeza que deseja excluir a turma de ID " + idEmEdicao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            turmaController.excluirTurma(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Turma excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
        comboSerie.setSelectedIndex(0);
        comboTurno.setSelectedIndex(0);
        comboNivelEnsino.setSelectedIndex(0);
        spinnerAnoLetivo.setValue(Year.now().getValue());

        botaoAdicionar.setEnabled(true);
        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
        tabela.clearSelection();

        modeloAlunosDaTurma.clear();
        comboAlunoParaAdicionar.removeAllItems();
        rotuloStatusTurma.setText("Selecione uma turma na tabela para gerenciar os alunos dela.");
        habilitarAcoesDeAlunos(false);
    }
}
