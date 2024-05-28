package org.example.view;

import org.example.model.entities.Recepta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModelComponentsVisuals {
    /**
     * Aquesta classe conté els models de les taules i del comboBox
     */

    private DefaultTableModel modelTaulaRecepta;
    private DefaultTableModel modeltaulaIng;
    private ComboBoxModel<Recepta.Receptes.Ingedient> comboBoxModel;

    //Getters


    public ComboBoxModel<Recepta.Receptes.Ingedient> getComboBoxModel() {
        /**
         * Getter del comboBoxModel
         * @return
         */
        return comboBoxModel;
    }

    public DefaultTableModel getModelTaulaRecepta() {
        /**
         * Getter del modelTaulaRecepta
         * @return
         */
        return modelTaulaRecepta;
    }

    public DefaultTableModel getModeltaulaIng() {
        /**
         * Getter del modeltaulaIng
         * @return
         */
        return modeltaulaIng;
    }

    public ModelComponentsVisuals() {
        /**
         * Constructor de la classe ModelComponentsVisuals
         */


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
                /**
                 * Returns true regardless of parameter values.
                 *
                 * @param row    the row whose value is to be queried
                 * @param column the column whose value is to be queried
                 * @return true
                 * @see #setValueAt
                 */

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }

            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                /**
                 * Permet definir el tipo de cada columna
                 * @param column
                 * @return
                 */
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
