package br.com.view;

import br.com.controller.DisciplinaController;
import br.com.controller.ProfessorController;
import br.com.controller.TurmaController;
import br.com.controller.VinculoController;
import br.com.model.dto.TurmaExibicao;
import br.com.model.entity.Disciplina;
import br.com.model.entity.Professor;
import br.com.model.entity.Turma;
import br.com.model.entity.Vinculo;
import br.com.model.enums.DiaSemana;
import br.com.model.enums.PeriodoAula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Aba de gerenciamento de Vínculos (grade de horários): cadastro, edição,
 * exclusão e listagem.
 */
public class AbaVinculos extends JPanel implements AbaAtualizavel {

    private final VinculoController vinculoController = new VinculoController();
    private final ProfessorController professorController = new ProfessorController();
    private final DisciplinaController disciplinaController = new DisciplinaController();
    private final TurmaController turmaController = new TurmaController();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Professor", "Disciplinas", "Turma", "Dia da Semana", "Período"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private final JTextField campoId = Campos.campoSomenteLeitura();
    private final JComboBox<ItemSelecao> comboProfessor = Campos.comboItens();
    private final ListaComCheckbox listaDisciplinas = Campos.listaComCheckbox();
    private final JComboBox<ItemSelecao> comboTurma = Campos.comboItens();
    private final JComboBox<DiaSemana> comboDiaSemana = Campos.comboEnum(DiaSemana.values());
    private final JComboBox<PeriodoAula> comboPeriodoAula = Campos.comboEnum(PeriodoAula.values());

    private final JButton botaoAdicionar = new JButton("Adicionar");
    private final JButton botaoEditar = new JButton("Editar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");

    private Integer idEmEdicao = null;
    private List<Professor> professoresCache = new ArrayList<>();
    private List<Disciplina> disciplinasCache = new ArrayList<>();
    private List<Turma> turmasCache = new ArrayList<>();

    public AbaVinculos() {
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
        painelEsquerdo.add(Tema.titulo("Vínculos cadastrados"), BorderLayout.NORTH);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createLineBorder(Tema.CINZA_MEDIO));
        painelEsquerdo.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelDireito = montarFormulario();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);
        splitPane.setDividerSize(10);

        add(splitPane, BorderLayout.CENTER);

        limparFormulario();
    }

    private JPanel montarFormulario() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(container, 0, 16, 0, 0);
        container.add(Tema.titulo("Dados do vínculo"), BorderLayout.NORTH);

        PainelFormulario formulario = new PainelFormulario();
        formulario.adicionarLinha("ID", campoId);
        formulario.adicionarLinha("Professor", comboProfessor);
        formulario.adicionarLinhaAlta("Disciplinas", Campos.comRolagem(listaDisciplinas), 100);
        formulario.adicionarLinha("Turma", comboTurma);
        formulario.adicionarLinha("Dia da Semana", comboDiaSemana);
        formulario.adicionarLinha("Período da Aula", comboPeriodoAula);

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
        professoresCache = professorController.listarProfessores();
        comboProfessor.removeAllItems();
        for (Professor p : professoresCache) {
            comboProfessor.addItem(new ItemSelecao(p.getId(), p.getId() + " - " + p.getNome()));
        }

        disciplinasCache = disciplinaController.listarDisciplina();
        List<ItemSelecao> itensDisciplinas = new ArrayList<>();
        for (Disciplina d : disciplinasCache) {
            itensDisciplinas.add(new ItemSelecao(d.getId(), d.getId() + " - " + d.getDisciplina()));
        }
        listaDisciplinas.definirItens(itensDisciplinas);

        List<TurmaExibicao> turmasExibicao = turmaController.listarTurma();
        turmasCache = turmasExibicao.stream().map(TurmaExibicao::getTurma).toList();
        comboTurma.removeAllItems();
        for (Turma t : turmasCache) {
            comboTurma.addItem(new ItemSelecao(t.getId(),
                    t.getId() + " - " + t.getSerie().getDescricao() + " (" + t.getTurno().getDescricao() + " / " + t.getAnoLetivo() + ")"));
        }

        carregarTabela();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Vinculo> vinculos = vinculoController.listarVinculos();

        for (Vinculo v : vinculos) {
            String nomeProfessor = professoresCache.stream()
                    .filter(p -> p.getId() == v.getProfessorId())
                    .map(Professor::getNome)
                    .findFirst()
                    .orElse("ID " + v.getProfessorId());

            String nomesDisciplinas = nomesDisciplinas(v.getDisciplinaId());

            String nomeTurma = turmasCache.stream()
                    .filter(t -> t.getId() == v.getTurmaId())
                    .map(t -> t.getSerie().getDescricao() + " (" + t.getTurno().getDescricao() + ")")
                    .findFirst()
                    .orElse("ID " + v.getTurmaId());

            modeloTabela.addRow(new Object[]{
                    v.getId(), nomeProfessor, nomesDisciplinas, nomeTurma,
                    v.getDiaSemana().getDescricao(), v.getPeriodoAula().getDescricao()
            });
        }
    }

    private String nomesDisciplinas(List<Integer> ids) {
        if (ids == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int id : ids) {
            Disciplina d = disciplinasCache.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
            if (sb.length() > 0) sb.append(", ");
            sb.append(d != null ? d.getDisciplina() : ("ID " + id));
        }
        return sb.toString();
    }

    private void carregarParaEdicao(int linha) {
        List<Vinculo> vinculos = vinculoController.listarVinculos();
        if (linha >= vinculos.size()) return;

        Vinculo v = vinculos.get(linha);
        idEmEdicao = v.getId();

        campoId.setText(String.valueOf(v.getId()));
        selecionarItemNoCombo(comboProfessor, v.getProfessorId());
        selecionarItemNoCombo(comboTurma, v.getTurmaId());
        comboDiaSemana.setSelectedItem(v.getDiaSemana());
        comboPeriodoAula.setSelectedItem(v.getPeriodoAula());
        listaDisciplinas.marcar(v.getDisciplinaId());

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
            ItemSelecao professor = (ItemSelecao) comboProfessor.getSelectedItem();
            ItemSelecao turma = (ItemSelecao) comboTurma.getSelectedItem();
            if (professor == null || turma == null) {
                throw new IllegalArgumentException("Erro: cadastre professores e turmas antes de criar um vínculo.");
            }

            vinculoController.adicionarVinculo(
                    professor.getId(),
                    listaDisciplinas.obterMarcados(),
                    turma.getId(),
                    (DiaSemana) comboDiaSemana.getSelectedItem(),
                    (PeriodoAula) comboPeriodoAula.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Vínculo adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
            ItemSelecao professor = (ItemSelecao) comboProfessor.getSelectedItem();
            ItemSelecao turma = (ItemSelecao) comboTurma.getSelectedItem();
            if (professor == null || turma == null) {
                throw new IllegalArgumentException("Erro: selecione um professor e uma turma.");
            }

            vinculoController.editarVinculo(
                    idEmEdicao,
                    professor.getId(),
                    listaDisciplinas.obterMarcados(),
                    turma.getId(),
                    (DiaSemana) comboDiaSemana.getSelectedItem(),
                    (PeriodoAula) comboPeriodoAula.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Vínculo atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
                "Tem certeza que deseja excluir o vínculo de ID " + idEmEdicao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            vinculoController.excluirVinculo(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Vínculo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
        if (comboProfessor.getItemCount() > 0) comboProfessor.setSelectedIndex(0);
        if (comboTurma.getItemCount() > 0) comboTurma.setSelectedIndex(0);
        comboDiaSemana.setSelectedIndex(0);
        comboPeriodoAula.setSelectedIndex(0);
        listaDisciplinas.desmarcarTodos();

        botaoAdicionar.setEnabled(true);
        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
        tabela.clearSelection();
    }
}
