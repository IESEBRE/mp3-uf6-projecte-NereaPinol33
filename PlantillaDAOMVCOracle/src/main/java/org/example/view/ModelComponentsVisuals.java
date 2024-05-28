package org.example.view;

import org.example.model.entities.Recepta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModelComponentsVisuals {

    private DefaultTableModel modelTaulaRecepta;
    private DefaultTableModel modeltaulaIng;
    private ComboBoxModel<Recepta.Receptes.Ingedient> comboBoxModel;

    //Getters


    public ComboBoxModel<Recepta.Receptes.Ingedient> getComboBoxModel() {
        return comboBoxModel;
    }

    public DefaultTableModel getModelTaulaRecepta() {
        return modelTaulaRecepta;
    }

    public DefaultTableModel getModeltaulaIng() {
        return modeltaulaIng;
    }

    public ModelComponentsVisuals() {


        //Anem a definir l'estructura de la taula dels receptas
        modelTaulaRecepta =new DefaultTableModel(new Object[]{"Nom","Temps","Object"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }



            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Double.class;
                    default:
                        return Object.class;
                }
            }
        };




        //Anem a definir l'estructura de la taula de les matrícules
        modeltaulaIng =new DefaultTableModel(new Object[]{"Ingredient","Quantitat"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }

            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Recepta.Receptes.Ingedient.class;
                    case 1:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };



        //Estructura del comboBox
        comboBoxModel=new DefaultComboBoxModel<>(Recepta.Receptes.Ingedient.values());



    }
}
