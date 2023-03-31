package de.jkolesnikov.tetris.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Modellklasse für Spieler
 */
public class Spieler {
    private final int nullPunkte = 0;
    private final String leererName = "-leer_";
    private final double keineSpielzeit =0.0;

    private int id;

    private SimpleIntegerProperty punktestand;
    private SimpleStringProperty name;
    private SimpleDoubleProperty spielzeit;

    public Spieler() {
        this.punktestand = new SimpleIntegerProperty(nullPunkte);
        name = new SimpleStringProperty(leererName);
        spielzeit = new SimpleDoubleProperty(keineSpielzeit);
    }

    public Spieler(String name, int punktestand,double spielzeit) {
        this.name = new SimpleStringProperty(name);
        this.punktestand = new SimpleIntegerProperty(punktestand);
        this.spielzeit = new SimpleDoubleProperty(spielzeit);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPunktestand() {
        return punktestand.get();
    }

    public SimpleIntegerProperty punktestandProperty() {
        return punktestand;
    }

    public void setPunktestand(int punktestand) {
        this.punktestand.set(punktestand);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getSpielzeit() {
        return spielzeit.get();
    }

    public SimpleDoubleProperty spielzeitProperty() {

        return spielzeit;
    }

    public void setSpielzeit(double spielzeit) {
        this.spielzeit.set(spielzeit);
    }

    @Override
    public String toString() {
        return name.get() +
                        "\nPunkte: " + punktestand.get() +
                       "\nSpielzeit: " + spielzeit.get();
    }
}
