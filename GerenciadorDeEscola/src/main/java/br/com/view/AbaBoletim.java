package br.com.view;

import br.com.controller.AlunoController;
import br.com.controller.BoletimController;
import br.com.model.dto.AlunoExibicao;
import br.com.model.dto.BoletimExibicao;
import br.com.model.dto.LinhaBoletim;
import br.com.model.entity.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Aba de geração de Boletim: seleciona um aluno e exibe o desempenho dele
 * em todas as disciplinas da turma, com as notas dos 4 bimestres, a média
 * final e a situação (Aprovado, Recuperação, Reprovado ou Em andamento).
 */
public class AbaBoletim extends JPanel implements AbaAtualizavel {

    private final BoletimController boletimController = new BoletimController();
    private final AlunoController alunoController = new AlunoController();

    private final JComboBox<ItemSelecao> comboAluno = Campos.comboItens();
    private final JButton botaoGerar = new JButton("Gerar Boletim");
    private final JLabel rotuloAlunoSelecionado = Tema.rotulo(" ");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"Disciplina", "1º Bim.", "2º Bim.", "3º Bim.", "4º Bim.", "Média Final", "Situação"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public AbaBoletim() {
        setLayout(new BorderLayout());
        setBackground(Tema.BRANCO);
        Tema.aplicarEspacamento(this, 16, 16, 16, 16);

        Tema.estilizarTabela(tabela);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Tema.BRANCO);
        topo.add(Tema.titulo("Boletim do aluno"), BorderLayout.NORTH);

        JPanel painelSelecao = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        painelSelecao.setBackground(Tema.BRANCO);
        painelSelecao.add(Tema.rotulo("Aluno"));
        comboAluno.setPreferredSize(new Dimension(260, comboAluno.getPreferredSize().height));
        painelSelecao.add(comboAluno);
        Tema.botaoPrimario(botaoGerar);
        painelSelecao.add(botaoGerar);
        painelSelecao.add(rotuloAlunoSelecionado);

        topo.add(painelSelecao, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createLineBorder(Tema.CINZA_MEDIO));
        Tema.aplicarEspacamento(scrollTabela, 8, 0, 0, 0);
        add(scrollTabela, BorderLayout.CENTER);

        botaoGerar.addActionListener(e -> gerarBoletim());
    }

    @Override
    public void atualizarDados() {
        List<AlunoExibicao> alunos = alunoController.listarAlunos();
        Object selecionadoAnterior = comboAluno.getSelectedItem();

        comboAluno.removeAllItems();
        for (AlunoExibicao ae : alunos) {
            Aluno a = ae.getAluno();
            comboAluno.addItem(new ItemSelecao(a.getMatricula(), a.getMatricula() + " - " + a.getNome()));
        }

        if (selecionadoAnterior != null) {
            comboAluno.setSelectedItem(selecionadoAnterior);
        }
    }

    private void gerarBoletim() {
        ItemSelecao aluno = (ItemSelecao) comboAluno.getSelectedItem();
        if (aluno == null) {
            JOptionPane.showMessageDialog(this, "Erro: cadastre um aluno antes de gerar o boletim.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BoletimExibicao boletim = boletimController.gerarExibicaoBoletim(aluno.getId());
            modeloTabela.setRowCount(0);

            for (LinhaBoletim linha : boletim.getLinhas()) {
                modeloTabela.addRow(new Object[]{
                        linha.getDisciplinaNome(),
                        formatarNota(linha.getNota1()),
                        formatarNota(linha.getNota2()),
                        formatarNota(linha.getNota3()),
                        formatarNota(linha.getNota4()),
                        formatarNota(linha.getMediaFinal()),
                        linha.getSituacao().getDescricao()
                });
            }

            rotuloAlunoSelecionado.setText("Boletim de: " + boletim.getAluno().getNome());
        } catch (Exception ex) {
            modeloTabela.setRowCount(0);
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatarNota(Double nota) {
        return nota == null ? "—" : String.valueOf(nota);
    }
}
