package de.jkolesnikov.tetris.logic.db;

import de.jkolesnikov.tetris.model.Spieler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt Methoden zur Verfügung, um Spieler in die Datenbank zu schreiben und
 * diese auch wieder auszulesen.
 */
public class DaoSpieler implements Dao<Spieler> {
    @Override
    public void create(Connection dbConnection, Spieler spieler) {
        PreparedStatement statement = null;
        try {
            statement = dbConnection.prepareStatement(
                    "INSERT INTO highscore (spielername, punktestand, spielzeit) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, spieler.getName());
            statement.setInt(2, spieler.getPunktestand());
            statement.setDouble(3, spieler.getSpielzeit());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int insertId = generatedKeys.getInt("insert_id");
                spieler.setId(insertId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Spieler> readAll(Connection dbConnection) {
        List<Spieler> spielerListe = new ArrayList<>();

        Statement statement = null;

        try {
            statement = dbConnection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM highscore");

            while(resultSet.next()) {

                Spieler spieler = new Spieler(
                        resultSet.getString("spielername"),
                        resultSet.getInt("punktestand"),
                        resultSet.getDouble("spielzeit")
                );
                spieler.setId(resultSet.getInt("id"));

                spielerListe.add(spieler);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return spielerListe;
    }

    @Override
    public void update(Connection dbConnection, Spieler spieler) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement("UPDATE highscore SET spielername = ?, punktestand = ?, spielzeit = ? WHERE id = ?;");
            statement.setString(1, spieler.getName());
            statement.setInt(2, spieler.getPunktestand());
            statement.setDouble(3, spieler.getSpielzeit());
            statement.setInt(4, spieler.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Connection dbConnection, Spieler spieler) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement("DELETE FROM highscore WHERE id = ?");
            statement.setInt(1,spieler.getId());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
