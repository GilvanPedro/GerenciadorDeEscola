package br.com.view;

import br.com.controller.DisciplinaController;
import br.com.controller.ProfessorController;
import br.com.controller.TurmaController;
import br.com.controller.VinculoController;
import br.com.model.entity.Disciplina;
import br.com.model.entity.Professor;
import br.com.model.entity.Turma;
import br.com.model.entity.Vinculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Aba de gerenciamento de Professores: cadastro, edição, exclusão e listagem.
 */
public class AbaProfessores extends JPanel implements AbaAtualizavel {

    private final ProfessorController professorController = new ProfessorController();
    private final DisciplinaController disciplinaController = new DisciplinaController();
    private final VinculoController vinculoController = new VinculoController();
    private final TurmaController turmaController = new TurmaController();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Nome", "CPF", "Data Nasc.", "Telefone", "Endereço", "Email", "Disciplinas", "Vínculos"}, 0) {
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
    private final ListaComCheckbox listaDisciplinas = Campos.listaComCheckbox();
    private final ListaComCheckbox listaVinculos = Campos.listaComCheckbox();
    private final JTextField campoTelefone = Campos.campoTexto(20);
    private final JTextField campoEndereco = Campos.campoTexto(20);
    private final JTextField campoEmail = Campos.campoTexto(20);

    private final JButton botaoAdicionar = new JButton("Adicionar");
    private final JButton botaoEditar = new JButton("Editar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");

    private Integer idEmEdicao = null;
    private List<Disciplina> disciplinasCache = new ArrayList<>();
    private List<Vinculo> vinculosCache = new ArrayList<>();
    private List<Turma> turmasCache = new ArrayList<>();

    public AbaProfessores() {
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
        painelEsquerdo.add(Tema.titulo("Professores cadastrados"), BorderLayout.NORTH);
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
        container.add(Tema.titulo("Dados do professor"), BorderLayout.NORTH);

        PainelFormulario formulario = new PainelFormulario();
        formulario.adicionarLinha("ID", campoId);
        formulario.adicionarLinha("Nome", campoNome);
        formulario.adicionarLinha("CPF", campoCpf);
        formulario.adicionarLinha("Data de Nascimento", campoDataNascimento);
        formulario.adicionarLinhaAlta("Disciplinas", Campos.comRolagem(listaDisciplinas), 90);
        formulario.adicionarLinhaAlta("Vínculos", Campos.comRolagem(listaVinculos), 90);
        formulario.adicionarLinha("Telefone", campoTelefone);
        formulario.adicionarLinha("Endereço", campoEndereco);
        formulario.adicionarLinha("Email", campoEmail);

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
        disciplinasCache = disciplinaController.listarDisciplina();
        vinculosCache = vinculoController.listarVinculos();
        turmasCache = turmaController.listarTurma().stream().map(t -> t.getTurma()).toList();

        List<ItemSelecao> itensDisciplinas = new ArrayList<>();
        for (Disciplina d : disciplinasCache) {
            itensDisciplinas.add(new ItemSelecao(d.getId(), d.getId() + " - " + d.getDisciplina()));
        }
        listaDisciplinas.definirItens(itensDisciplinas);

        List<ItemSelecao> itensVinculos = new ArrayList<>();
        for (Vinculo v : vinculosCache) {
            itensVinculos.add(new ItemSelecao(v.getId(), rotuloVinculo(v)));
        }
        listaVinculos.definirItens(itensVinculos);

        carregarTabela();
    }

    private String rotuloVinculo(Vinculo v) {
        String turma = turmasCache.stream()
                .filter(t -> t.getId() == v.getTurmaId())
                .map(t -> t.getSerie().getDescricao())
                .findFirst()
                .orElse("Turma " + v.getTurmaId());
        return v.getId() + " - " + turma + " (" + v.getDiaSemana().getDescricao() + ", " + v.getPeriodoAula().getDescricao() + ")";
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Professor> professores = professorController.listarProfessores();

        for (Professor p : professores) {
            String disciplinas = nomesDisciplinas(p.getDisciplina());
            String vinculos = p.getVinculo() == null ? "" :
                    p.getVinculo().stream().map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse("");

            modeloTabela.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getCpf(), p.getDataNascimentoFormatada(),
                    p.getTelefone(), p.getEndereco(), p.getEmail(), disciplinas, vinculos
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
        List<Professor> professores = professorController.listarProfessores();
        if (linha >= professores.size()) return;

        Professor p = professores.get(linha);
        idEmEdicao = p.getId();

        campoId.setText(String.valueOf(p.getId()));
        campoNome.setText(p.getNome());
        campoCpf.setText(p.getCpf());
        campoCpf.setEditable(false);
        campoDataNascimento.setText(p.getDataNascimentoFormatada());
        campoTelefone.setText(p.getTelefone());
        campoEndereco.setText(p.getEndereco());
        campoEmail.setText(p.getEmail());

        listaDisciplinas.marcar(p.getDisciplina());
        listaVinculos.marcar(p.getVinculo());

        botaoAdicionar.setEnabled(false);
        botaoEditar.setEnabled(true);
        botaoExcluir.setEnabled(true);
    }

    private void adicionar() {
        try {
            professorController.adicionarProfessor(
                    campoNome.getText().trim(),
                    Campos.cpfSemMascara(campoCpf.getText()),
                    campoDataNascimento.getText().trim(),
                    listaDisciplinas.obterMarcados(),
                    listaVinculos.obterMarcados(),
                    campoTelefone.getText().trim(),
                    campoEndereco.getText().trim(),
                    campoEmail.getText().trim()
            );

            JOptionPane.showMessageDialog(this, "Professor adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
            professorController.editarProfessor(
                    idEmEdicao,
                    campoNome.getText().trim(),
                    campoDataNascimento.getText().trim(),
                    listaDisciplinas.obterMarcados(),
                    listaVinculos.obterMarcados(),
                    campoTelefone.getText().trim(),
                    campoEndereco.getText().trim(),
                    campoEmail.getText().trim()
            );

            JOptionPane.showMessageDialog(this, "Professor atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
                "Tem certeza que deseja excluir o professor de ID " + idEmEdicao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            professorController.excluirProfessor(idEmEdicao);
            JOptionPane.showMessageDialog(this, "Professor excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
        campoTelefone.setText("");
        campoEndereco.setText("");
        campoEmail.setText("");
        listaDisciplinas.desmarcarTodos();
        listaVinculos.desmarcarTodos();

        botaoAdicionar.setEnabled(true);
        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
        tabela.clearSelection();
    }
}
