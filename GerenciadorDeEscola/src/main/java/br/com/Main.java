package br.com;

import br.com.view.InterfaceEscola;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        //Iniciar a interface
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Mantém o Look and Feel padrão caso o do sistema não esteja disponível
            }
            new InterfaceEscola().setVisible(true);
        });
    }
}