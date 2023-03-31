package de.jkolesnikov.tetris.logic.db;

import java.sql.Connection;
import java.util.List;

/**
 * Interface, welches die CRUD-Funktionalität für eine Modellklasse bereitstellt um bestimmte Objekte
 * in die Datenbank zu schreiben oder von der Datenbank zu lesen.
 */
public interface Dao <T> {
    void create(Connection dbConnection, T object);

    List<T> readAll(Connection dbConnection);

    void update(Connection dbConnection, T object);

    void delete(Connection dbConnection, T object);

}
