package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReceptaView extends JFrame{

    private JTabbedPane pestanyes;
    private JTable taula;
    private JScrollPane scrollPane1;
    private JButton insertarButton;
    private JButton modificarButton;
    private JButton borrarButton;
    private JTextField campNom;
    private JTextField campTemps;
    private JPanel panel;
    private JTable taulaIng;
    private JComboBox comboIng;
    private JTextField campQuant;
    //private JTabbedPane PanelPestanya;

    //Getters


    public JTable gettaulaIng() {
        return taulaIng;
    }

    public JComboBox getcomboIng() {
        return comboIng;
    }

    public JTextField getcampQuant() {
        return campQuant;
    }

    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

    public JTable getTaula() {
        return taula;
    }

    public JButton getBorrarButton() {
        return borrarButton;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JButton getInsertarButton() {
        return insertarButton;
    }

    public JTextField getCampNom() {
        return campNom;
    }

    public JTextField getcampTemps() {
        return campTemps;
    }



    //Constructor de la classe
    public ReceptaView() {
        /**
         * Constructor de la classe ReceptaView
         */


        //Per poder vore la finestra
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
    }

        private void createUIComponents() {
        /**
         * Mètode que crea els components de la finestra
         */
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane();
        taula = new JTable();
        pestanyes = new JTabbedPane();
        taula.setModel(new DefaultTableModel());
        taula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane1.setViewportView(taula);

    }
}
