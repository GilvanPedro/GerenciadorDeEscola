package br.com.view;

import javax.swing.*;
import java.awt.*;

/**
 * Janela principal do sistema "Gerenciador de Escola".
 * Organiza os módulos do sistema em abas (JTabbedPane), cada uma implementada
 * em sua própria classe, seguindo um visual minimalista e consistente.
 */
public class InterfaceEscola extends JFrame {

    public InterfaceEscola() {
        super("Gerenciador de Escola");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);

        getContentPane().setBackground(Tema.BRANCO);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(Tema.FONTE_ROTULO);
        abas.setBackground(Tema.BRANCO);
        abas.setForeground(Tema.TEXTO_PRINCIPAL);

        AbaAlunos abaAlunos = new AbaAlunos();
        AbaProfessores abaProfessores = new AbaProfessores();
        AbaResponsaveis abaResponsaveis = new AbaResponsaveis();
        AbaDisciplinas abaDisciplinas = new AbaDisciplinas();
        AbaTurmas abaTurmas = new AbaTurmas();
        AbaNotas abaNotas = new AbaNotas();
        AbaVinculos abaVinculos = new AbaVinculos();
        AbaBoletim abaBoletim = new AbaBoletim();

        abas.addTab("Alunos", abaAlunos);
        abas.addTab("Professores", abaProfessores);
        abas.addTab("Responsáveis", abaResponsaveis);
        abas.addTab("Disciplinas", abaDisciplinas);
        abas.addTab("Turmas", abaTurmas);
        abas.addTab("Notas", abaNotas);
        abas.addTab("Vínculos", abaVinculos);
        abas.addTab("Boletim", abaBoletim);

        Tema.aplicarHoverAbas(abas);

        AbaAtualizavel[] abasAtualizaveis = {
                abaAlunos, abaProfessores, abaResponsaveis, abaDisciplinas,
                abaTurmas, abaNotas, abaVinculos, abaBoletim
        };

        // Sempre que o usuário troca de aba, os dados dela são recarregados,
        // garantindo que informações cadastradas em outras abas (turmas, alunos,
        // disciplinas, etc.) apareçam atualizadas nos combos e listas.
        abas.addChangeListener(e -> {
            int indice = abas.getSelectedIndex();
            if (indice >= 0 && indice < abasAtualizaveis.length) {
                abasAtualizaveis[indice].atualizarDados();
            }
        });

        // Atualiza a primeira aba assim que a janela é exibida
        abaAlunos.atualizarDados();

        add(abas, BorderLayout.CENTER);
    }
}
