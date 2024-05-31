package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Recepta;
import org.example.model.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

public class IngredientDAOJDBCOracleImpl implements DAO<Recepta> {
    /**
     * Aquesta classe implementa la interfície DAO per a la classe Recepta
     */


    @Override
    public Recepta get(Long id) throws DAOException {


        //Declaració de variables del mètode
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Recepta recepta = null;

        //Accés a la BD usant l'API JDBC

        Properties prop = new Properties();
        InputStream ins;

        try {
            ins = new FileInputStream("./src/main/resources/dataBase.properties");
            prop.load(ins);
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            con = DriverManager.getConnection(
                    url, user, password
            );
//            st = con.prepareStatement("SELECT * FROM recepta WHERE id=?;");
            st = con.createStatement();
//            st = con.prepareStatement("SELECT * FROM recepta WHERE id=?;");
//            st.setLong(1, id);
            rs = st.executeQuery("SELECT * FROM RECEPTA;");
//            recepta = new Recepta(rs.getLong(1), rs.getString(2));
//            st.close();
            if (rs.next()) {
                recepta = new Recepta(Long.valueOf(rs.getString(1)), rs.getString(2));
            }
        } catch (FileNotFoundException notEx) {
            throw new DAOException(5);
        } catch (IOException ioex){
            throw new DAOException(6);
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
        return recepta;
    }

    @Override
    public List<Recepta> getAll() throws DAOException {
        /**
         * Aquest mètode retorna totes les receptes de la BD
         * @return
         * @throws DAOException
         */
        //Declaració de variables del mètode
        List<Recepta> recepta = new ArrayList<>();
        Properties prop = new Properties();
        InputStream ins;
        Connection con = null;
        PreparedStatement st=null;
        ResultSet rs=null;


        //Accés a la BD usant l'API JDBC
        try {
            ins = new FileInputStream("./src/main/resources/dataBase.properties");
            prop.load(ins);
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            con = DriverManager.getConnection(url, user, password);
            st = con.prepareStatement("SELECT * FROM RECEPTA");
            rs = st.executeQuery();

            while (rs.next()) {
                recepta.add(new Recepta(rs.getLong("id"), rs.getString("nom"), rs.getDouble("temps"),
                        new TreeSet<Recepta.Receptes>()));
            }
        } catch (FileNotFoundException notEx) {
            throw new DAOException(5);
        } catch (IOException ioex){
            throw new DAOException(6);
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
        return recepta;
    }

    public Long getLastId() throws DAOException {
        /**
         * Aquest mètode retorna l'última ID de la taula RECEPTA
         * @return
         * @throws DAOException
         */
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Long lastId = null;

        Properties prop = new Properties();
        InputStream ins;

        try {
            ins = new FileInputStream("./src/main/resources/dataBase.properties");
            prop.load(ins);
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");
            con = DriverManager.getConnection(
                    url, user, password
            );
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(ID) AS MAX_ID FROM RECEPTA");

            if (rs.next()) {
                lastId = rs.getLong("MAX_ID");
            }
        } catch (FileNotFoundException notEx) {
            throw new DAOException(5);
        } catch (IOException ioex){
            throw new DAOException(6);
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
        return lastId;
    }



    @Override
    public void save(Recepta obj) throws DAOException {
        /**
         * Aquest mètode guarda una nova recepta a la BD
         * @param obj
         * @throws DAOException
         */
        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;


        // Asegúrate de que obj no sea null
        if (obj == null) {
            throw new DAOException(1);
        }
        Properties prop = new Properties();
        InputStream ins;

        // Asigna una nueva ID si es null
        if (obj.getId() == null) {
            Long lastId = getLastId();
            obj.setId((lastId != null) ? lastId + 1 : 1);
        }

        System.out.println("Recepta ID: " + obj.getId());
        System.out.println("Recepta Nom: " + obj.getNom());
        System.out.println("Recepta Temps: " + obj.getTemps());

        //Accés a la BD usant l'API JDBC
        try {
            ins = new FileInputStream("./src/main/resources/dataBase.properties");
            prop.load(ins);
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            con = DriverManager.getConnection(
                    url, user, password
            );
            System.out.println("Connexió establida: " + (con != null));
            st = con.prepareStatement("INSERT INTO RECEPTA (ID, NOM, TEMPS) VALUES (?, ?, ?)");
            st.setLong(1, obj.getId());
            st.setString(2, obj.getNom());
            st.setDouble(3, obj.getTemps());
            st.executeUpdate();
        } catch (FileNotFoundException notEx) {
            throw new DAOException(5);
        } catch (IOException ioex){
            throw new DAOException(6);
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
    }


    @Override
    public void update(Recepta obj) throws DAOException {
        /**
         * Aquest mètode actualitza una recepta a la BD
         * @param obj
         * @throws DAOException
         */
        Connection con = null;
        PreparedStatement st = null;

        if (obj == null) {
            throw new DAOException(777);
        }
        Properties prop = new Properties();
        InputStream ins;


        try {
            ins = new FileInputStream("./src/main/resources/dataBase.properties");
            prop.load(ins);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            con = DriverManager.getConnection(
                    url, user, password
            );
            System.out.println("Connexió establida: " + (con != null));

            st = con.prepareStatement("UPDATE RECEPTA SET NOM = ?, TEMPS = ? WHERE ID = ?");
            st.setString(1, obj.getNom());
            st.setDouble(2, obj.getTemps());
            st.setLong(3, obj.getId());

            System.out.println("Executant modificació per a Recepta ID: " + obj.getId());
            int rowsUpdated = st.executeUpdate();
            System.out.println("Files modificades: " + rowsUpdated);

        }catch (FileNotFoundException notEx) {
            throw new DAOException(5);

        } catch (IOException ioex){
            throw new DAOException(6);

        } catch (SQLException throwables) {
            System.err.println("SQLException: " + throwables.getMessage());
            throw new DAOException(1);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
                System.out.println("Resources tancat correctament.");
            } catch (SQLException e) {
                System.err.println("SQLException mentre resource limpiantse: " + e.getMessage());
                throw new DAOException(1);
            }
        }
    }


    @Override
    public void delete(Long id) throws DAOException {
        /**
         * Aquest mètode elimina una recepta de la BD
         * @param id
         * @throws DAOException
         */
        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;
        Properties prop = new Properties();
        InputStream ins;

        //Accés a la BD usant l'API JDBC
        try {
            ins = new FileInputStream("./src/main/resources/dataBase.properties");
            prop.load(ins);
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            con = DriverManager.getConnection(
                    url, user, password
            );
            st = con.prepareStatement("DELETE FROM RECEPTA WHERE ID = ?");
            st.setLong(1, id);
            st.executeUpdate();
        } catch (FileNotFoundException notEx) {
            throw new DAOException(5);
        } catch (IOException ioex){
            throw new DAOException(6);
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
    }



}

