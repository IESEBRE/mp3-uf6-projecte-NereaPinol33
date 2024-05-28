package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Recepta;
import org.example.model.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class IngredientDAOJDBCOracleImpl implements DAO<Recepta> {


    @Override
    public Recepta get(Long id) throws DAOException {

        //Declaració de variables del mètode
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Recepta recepta = null;

        //Accés a la BD usant l'API JDBC
        try {

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
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
        //Declaració de variables del mètode
        List<Recepta> recepta = new ArrayList<>();

        //Accés a la BD usant l'API JDBC
        try (Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/xe",
                "C##HR",
                "HR"
        );
             PreparedStatement st = con.prepareStatement("SELECT * FROM RECEPTA");
             ResultSet rs = st.executeQuery();
        ) {

            while (rs.next()) {
                recepta.add(new Recepta(rs.getLong("id"), rs.getString("nom"), rs.getDouble("temps"),
                        new TreeSet<Recepta.Receptes>()));
            }
        } catch (SQLException throwables) {
            int tipoError = throwables.getErrorCode();
            switch (throwables.getErrorCode()) {
                case 17002: //l'he obtingut posant un sout en el throwables.getErrorCode()
                    tipoError = 0;
                    break;
                default:
                    tipoError = 1;  //error desconegut
            }
            throw new DAOException(tipoError);
        }


        return recepta;
    }

    public Long getLastId() throws DAOException {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Long lastId = null;

        try {
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(ID) AS MAX_ID FROM RECEPTA");

            if (rs.next()) {
                lastId = rs.getLong("MAX_ID");
            }
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
        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;

        // Asegúrate de que obj no sea null
        if (obj == null) {
            throw new DAOException(1);
        }

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
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
            System.out.println("Connection established: " + (con != null));
            st = con.prepareStatement("INSERT INTO RECEPTA (ID, NOM, TEMPS) VALUES (?, ?, ?)");
            st.setLong(1, obj.getId());
            st.setString(2, obj.getNom());
            st.setDouble(3, obj.getTemps());
            st.executeUpdate();
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
        Connection con = null;
        PreparedStatement st = null;

        if (obj == null) {
            throw new DAOException(777);
        }

        try {
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
            System.out.println("Connection established: " + (con != null));

            st = con.prepareStatement("UPDATE RECEPTA SET NOM = ?, TEMPS = ? WHERE ID = ?");
            st.setString(1, obj.getNom());
            st.setDouble(2, obj.getTemps());
            st.setLong(3, obj.getId());

            System.out.println("Executing update for Recepta ID: " + obj.getId());
            int rowsUpdated = st.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);

        } catch (SQLException throwables) {
            System.err.println("SQLException occurred: " + throwables.getMessage());
            throw new DAOException(1);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
                System.out.println("Resources closed successfully.");
            } catch (SQLException e) {
                System.err.println("SQLException during resource cleanup: " + e.getMessage());
                throw new DAOException(1);
            }
        }
    }


    @Override
    public void delete(Long id) throws DAOException {
        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;

        //Accés a la BD usant l'API JDBC
        try {
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
            st = con.prepareStatement("DELETE FROM RECEPTA WHERE ID = ?");
            st.setLong(1, id);
            st.executeUpdate();
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

