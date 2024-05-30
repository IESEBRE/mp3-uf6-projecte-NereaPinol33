package org.example.controller;

import org.example.model.entities.Recepta;
import org.example.model.exceptions.DAOException;
import org.example.model.entities.Recepta.Receptes;
import org.example.view.ModelComponentsVisuals;
import org.example.model.impls.IngredientDAOJDBCOracleImpl;
import org.example.view.ReceptaView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

public class Controller implements PropertyChangeListener { //1. Implementació de interfície PropertyChangeListener
    /**
     * The model components visuals.
     * <p>
     *
     */


    private ModelComponentsVisuals modelComponentsVisuals =new ModelComponentsVisuals();
    private IngredientDAOJDBCOracleImpl dadesIngredients;
    private ReceptaView view;

    public Controller(IngredientDAOJDBCOracleImpl dadesIngredients, ReceptaView view) {
        /**
         * Instantiates a new Controller.
         *
         * @param dadesIngredients the dades ingredients
         * @param view             the view
         */
        this.dadesIngredients = dadesIngredients;
        this.view = view;

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        //canvis.addPropertyChangeListener(this);

        lligaVistaModel();

        afegirListeners();

        //Si no hem tingut cap poroblema amb la BD, mostrem la finestra
        view.setVisible(true);

    }

    private void lligaVistaModel() {


        //Carreguem la taula d'receptas en les dades de la BD
        try {
            setModelTaulaRecepta(modelComponentsVisuals.getModelTaulaRecepta(),dadesIngredients.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }

            //Fixem el model de la taula dels receptas
        JTable taula = view.getTaula();
        taula.setModel(this.modelComponentsVisuals.getModelTaulaRecepta());
        //Amago la columna que conté l'objecte recepta
        taula.getColumnModel().getColumn(2).setMinWidth(0);
        taula.getColumnModel().getColumn(2).setMaxWidth(0);
        taula.getColumnModel().getColumn(2).setPreferredWidth(0);

        //Fixem el model de la taula de matrícules
        JTable taulaIng = view.gettaulaIng();
        taulaIng.setModel(this.modelComponentsVisuals.getModeltaulaIng());

        //Posem valor a el combo d'MPs
        view.getcomboIng().setModel(modelComponentsVisuals.getComboBoxModel());

        //Desactivem la pestanya de la matrícula
        view.getPestanyes().setEnabledAt(1, false);
        view.getPestanyes().setTitleAt(1, "Ingredients de ...");

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        canvis.addPropertyChangeListener(this);
    }

    private void setModelTaulaRecepta(DefaultTableModel modelTaulaRecepta, List<Recepta> all) {
        /**
         * Sets model taula recepta.
         *
         */

        // Fill the table model with data from the collection
        for (Recepta cuina : all) {
            modelTaulaRecepta.addRow(new Object[]{cuina.getNom(), cuina.getTemps(), cuina});
        }
    }

    private void afegirListeners() {
        /**
         * Afegir listeners.
         */

        ModelComponentsVisuals modelo = this.modelComponentsVisuals;
        DefaultTableModel model = modelo.getModelTaulaRecepta();
        DefaultTableModel modelRe = modelo.getModeltaulaIng();
        JTable taula = view.getTaula();
        JTable taulaIng = view.gettaulaIng();
        JButton insertarButton = view.getInsertarButton();
        JButton modificarButton = view.getModificarButton();
        JButton borrarButton = view.getBorrarButton();
        JTextField campNom = view.getCampNom();
        JTextField campTemps = view.getcampTemps();
        JTabbedPane pestanyes = view.getPestanyes();
        JTextField campQuant = view.getcampQuant();

        //Botó insertar
        view.getInsertarButton().addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField campNom = view.getCampNom();
                        JTextField campTemps = view.getcampTemps();

                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de l'recepta
                            // Comprovem que totes les caselles continguen informació
                            if (campNom.getText().isBlank() || campTemps.getText().isBlank()) {
                                JOptionPane.showMessageDialog(null, "Falta omplir alguna dada!!");
                            } else {
                                try {
                                    NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault()); // Creem un número que entèn la cultura que utilitza l'aplicació
                                    double temps = num.parse(campTemps.getText().trim()).doubleValue(); // intentem convertir el text a double
                                    if (temps < 1 || temps > 800) throw new ParseException("", 0);

                                    Recepta al = new Recepta(campNom.getText(), temps, new TreeSet<Receptes>()); // Feem un treeset a la segona pestanya

                                    // Verifica que `al` no sea null antes de llamar a `save`
                                    if (al != null) {
                                        System.out.println("Recepta creada: " + al.getNom() + ", " + al.getTemps());
                                        dadesIngredients.save(al);

                                        // Si no hay excepciones, actualizamos el modelo y la vista
                                        model.addRow(new Object[]{campNom.getText(), temps, al});
                                        campNom.setText("Bizcocho de Oreo");
                                        campNom.setSelectionStart(0);
                                        campNom.setSelectionEnd(campNom.getText().length());
                                        campTemps.setText("40");
                                        campNom.requestFocus(); // intentem que el foco vaigue al camp del nom
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Error: Recepta és null");
                                    }

                                } catch (ParseException parseException) {
                                    setExcepcio(new DAOException(3));
                                    campTemps.setSelectionStart(0);
                                    campTemps.setSelectionEnd(campTemps.getText().length());
                                    campTemps.requestFocus();
                                } catch (DAOException daoException) {
                                    setExcepcio(daoException);
                                }
                            }
                        } else { // Si estem a la pestanya de la receptes
                            // Obtinc l'recepta de la columna que conté l'objecte, pero si el campQuant esta buit no fa res
                            Recepta al = (Recepta) model.getValueAt(taula.getSelectedRow(), 2);
                            if (!campQuant.getText().isBlank()) {
                                Receptes m = new Receptes((Receptes.Ingedient) view.getcomboIng().getSelectedItem(), Integer.parseInt(view.getcampQuant().getText()));
                                al.getReceptes().add(m);
                                ompliReceptes(al, modelRe);
                            }
                        }
                    }
                }
        );
        //Botó borrar a les dos pestanyes
        view.getBorrarButton().addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de l'recepta
                            int selectedRow = taula.getSelectedRow();
                            if (selectedRow != -1) {
                                Recepta al = (Recepta) model.getValueAt(selectedRow, 2);
                                try {
                                    dadesIngredients.delete(al.getId());
                                    model.removeRow(selectedRow);
                                } catch (DAOException daoException) {
                                    setExcepcio(daoException);
                                }
                            }
                        } else { // Si estem a la pestanya de la receptes
                            int selectedRow = taula.getSelectedRow();
                            if (selectedRow != -1) {
                                Recepta al = (Recepta) model.getValueAt(selectedRow, 2);
                                int filaSel = taulaIng.getSelectedRow();
                                if (filaSel != -1) {
                                    modelRe.removeRow(filaSel);
                                }
                            }
                        }
                    }
                }
        );

        //Botó modificar
        view.getModificarButton().addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de l'recepta
                            if (taula.getSelectedRow() != -1) {
                                try {
                                    String tempsText = campTemps.getText().trim();
                                    if (tempsText.isEmpty()) {
                                        throw new ParseException("El camp de temps està buit", 0);
                                    }

                                    NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault()); // Creem un número que entèn la cultura que utilitza l'aplicació
                                    double temps = num.parse(tempsText).doubleValue(); // intentem convertir el text a double

                                    if (temps < 1 || temps > 120) throw new ParseException("El valor de temps está fora de rang", 0);

                                    // Obtén la receta seleccionada
                                    Recepta al = (Recepta) model.getValueAt(taula.getSelectedRow(), 2); // Cambiado el índice a 2

                                    // Actualiza los valores de la receta
                                    al.setNom(campNom.getText());
                                    al.setTemps(temps);

                                    // Actualiza la base de datos
                                    try {
                                        dadesIngredients.update(al);

                                        // Si no hay excepciones, actualiza la vista
                                        model.setValueAt(campNom.getText(), taula.getSelectedRow(), 0);
                                        model.setValueAt(temps, taula.getSelectedRow(), 1);
                                        campNom.setText("");
                                        campTemps.setText("");
                                        campNom.requestFocus();
                                    } catch (DAOException daoException) {
                                        daoException.printStackTrace();
                                        setExcepcio(daoException);
                                    }
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                    setExcepcio(new DAOException(3));
                                    campTemps.setSelectionStart(0);
                                    campTemps.setSelectionEnd(campTemps.getText().length());
                                    campTemps.requestFocus();
                                }
                            }
                        } else if (taulaIng.getSelectedRow() != -1) { // Si estem a la pestanya de la receptes
                            // Obtinc l'recepta de la columna que conté l'objecte
                            Recepta al = (Recepta) model.getValueAt(taula.getSelectedRow(), 2); // Cambiado el índice a 2
                            int filaSel = taulaIng.getSelectedRow();
                            Receptes m = al.getReceptes().stream().skip(filaSel).findFirst().get();
                            m.setIngedient((Receptes.Ingedient) view.getcomboIng().getSelectedItem());
                            m.setNota(Integer.parseInt(view.getcampQuant().getText()));
                            ompliReceptes(al, modelRe);
                        }
                    }
                }
        );

        taula.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                /**
                 * {@inheritDoc}
                 * @param e
                 * @see MouseAdapter#mouseClicked(MouseEvent)

                 */
                super.mouseClicked(e);

                //Obtenim el número de la fila seleccionada
                int filaSel = taula.getSelectedRow();

                if (filaSel != -1) {        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    campNom.setText(model.getValueAt(filaSel, 0).toString());
                    campTemps.setText(model.getValueAt(filaSel, 1).toString().replaceAll("\\.", ","));

                    //Activem la pestanya de la Ingredients de l'recepta seleccionat
                    view.getPestanyes().setEnabledAt(1, true);
                    view.getPestanyes().setTitleAt(1, "Ingredients de " + campNom.getText());

                    //Posem valor a el combo d'MPs
                    //view.getcomboIng().setModel(modelo.getComboBoxModel());
                    ompliReceptes((Recepta) model.getValueAt(filaSel, 2),modelRe);
                } else {                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campNom.setText("");
                    campTemps.setText("");

                    //Desactivem pestanyes
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Ingredients de ...");
                }
            }
        });
        campNom.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * See the class description
             * for {@code KeyEvent} for a definition of
             * a key typed event.
             * @param e
             */
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                JTextField textField = (JTextField) e.getSource();
                String text = textField.getText();

                char keyChar = e.getKeyChar();
                String regex = "^[A-Z][a-zA-Z]*$";

                if (Character.isLetter(keyChar)) {
                    text += keyChar;
                } else if (keyChar == KeyEvent.VK_BACK_SPACE || keyChar == KeyEvent.VK_DELETE) {
                    return;
                } else {
                    mostrarMensajeError();
                    e.consume();
                    setExcepcio(new DAOException(34));
                    return;
                }
                if (!text.matches(regex)) {
                    mostrarMensajeError();
                    e.consume();
                    setExcepcio(new DAOException(34));
                }
            }
            private void mostrarMensajeError() {
                JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "Aquest camp ha de començar amb una majúscula i només pot contenir lletres", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        campTemps.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(!Character.isDigit(e.getKeyChar()) && e.getKeyChar()!=KeyEvent.VK_PERIOD && e.getKeyChar()!=KeyEvent.VK_BACK_SPACE && e.getKeyChar()!=KeyEvent.VK_DELETE) {
                    JOptionPane.showMessageDialog(null, "Has d'introduir un número!!");
                    e.consume();
                    setExcepcio(new DAOException(3));
                }
            }
        });
        //No acceptem lletres en el camp de la quantitat
        campQuant.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(!Character.isDigit(e.getKeyChar()) && e.getKeyChar()!=KeyEvent.VK_BACK_SPACE && e.getKeyChar()!=KeyEvent.VK_DELETE) {
                    JOptionPane.showMessageDialog(null, "Has d'introduir un número enter!!");
                    e.consume();
                    setExcepcio(new DAOException(3));
                }
            }
        });



//        campNom.addFocusListener(new FocusAdapter() {
//            /**
//             * Invoked when a component loses the keyboard focus.
//             *
//             * @param e
//             */
//            @Override
//            public void focusLost(FocusEvent e) {
//                super.focusLost(e);
//                String regex1="^[A-ZÀ-ÚÑÇ][a-zà-úñç]+\\s+[A-ZÀ-ÚÑÇ][a-zà-úñç]+\\s+[A-ZÀ-ÚÑÇ][a-zà-úñç]+$",
//                        regex2="^[A-ZÀ-ÚÑÇ][a-zà-úñç]+(\\s*,\\s*)[A-ZÀ-ÚÑÇ][a-zà-úñç]+\\s+[A-ZÀ-ÚÑÇ][a-zà-úñç]+$";;
//                //String regex="[À-ú]";
//                //Pattern pattern = Pattern.compile(regex);
//                if(campNom.getText().isBlank() || (!campNom.getText().matches(regex1) && !campNom.getText().matches(regex2))){
//                    setExcepcio(new DAOException(2));
//                }
//            }
//        });
        //throw new LaMeuaExcepcio(1,"Ha petat la base de dades");
    }



    private static void ompliReceptes(Recepta al, DefaultTableModel modelRe) {

        //Omplim el model de la taula de Ingredients de l'recepta seleccionat
        modelRe.setRowCount(0);
        // Fill the table model with data from the collection
        for (Receptes receptes : al.getReceptes()) {
            modelRe.addRow(new Object[]{receptes.getIngedient(), receptes.getNota()});
        }
    }


    //TRACTAMENT D'EXCEPCIONS

    //2. Propietat lligada per controlar quan genero una excepció
    public static final String PROP_EXCEPCIO="excepcio";
    private DAOException excepcio;

    public DAOException getExcepcio() {
        /**
         * Gets excepcio.
         *
         */
        return excepcio;
    }

    public void setExcepcio(DAOException excepcio) {
        /**
         * Sets excepcio.
         *
         */
        DAOException valorVell=this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell,excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    PropertyChangeSupport canvis=new PropertyChangeSupport(this);


    //4. Mètode on posarem el codi de tractament de les excepcions --> generat per la interfície PropertyChangeListener
    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /**
         * Property change.
         *
         */
        DAOException rebuda=(DAOException)evt.getNewValue();

        try {
            throw rebuda;
        } catch (DAOException e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            switch(evt.getPropertyName()){
                case PROP_EXCEPCIO:

                    switch(rebuda.getTipo()){
                        case 0:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            System.exit(1);
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            //this.view.getCampNom().setText(rebuda.getMissatge());
                            this.view.getCampNom().setSelectionStart(0);
                            this.view.getCampNom().setSelectionEnd(this.view.getCampNom().getText().length());
                            this.view.getCampNom().requestFocus();

                            break;
                    }


            }
        }
    }

}
