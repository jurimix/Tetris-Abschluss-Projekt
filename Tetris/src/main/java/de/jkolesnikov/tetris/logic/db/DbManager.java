package de.jkolesnikov.tetris.logic.db;

import de.jkolesnikov.tetris.model.Spieler;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.List;
/**
 * Diese Klasse baut Verbindungen zu Datenbanken auf und hat Zugriff darauf und sorgt dafür
 * das, unterschiedliche Befehle ausgeführt werden, wie löschen u.s.w.
 */
public class DbManager {
    public static final String URL_PREFIX = "jdbc:mariadb://";
    public static final String SERVER_IP = "localhost";
    public static final String DB_NAME = "/spielerhighscore";

    public static final String URL = URL_PREFIX + SERVER_IP + DB_NAME;
    public static final String USERNAME = "Juri";
    public static final String PASSWORD = "1234";

    private static DbManager instanz;
    private static DaoSpieler daoSpieler;

    private DbManager() {
        daoSpieler = new DaoSpieler();
    }

    public static synchronized DbManager getInstanz() {
        if (instanz==null) instanz = new DbManager();
        return instanz;
    }

    private Connection getConnection(){
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connection;
    }

    public void insertDataRecord(Object object) {
        if (object instanceof Spieler spieler) {
            daoSpieler.create(getConnection(), spieler);
        }
    }

    public void insertDataDelete( Object object) {
        if (object instanceof Spieler spieler) {
            daoSpieler.delete(getConnection(), spieler);
        }
    }

    public void insertDataUpdate(Object object){
        if (object instanceof Spieler spieler){
            daoSpieler.update(getConnection(), spieler);
        }
    }

    public List<Spieler> readAllDataRecords() {
        return daoSpieler.readAll(getConnection());
    }
}
