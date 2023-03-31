package de.jkolesnikov.tetris.logic;

import de.jkolesnikov.tetris.logic.db.DbManager;
import de.jkolesnikov.tetris.model.Spieler;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.util.Callback;

import java.util.Comparator;
import java.util.List;

/**
 * Ist als Singleton implementiert und stellt die Spieler als ObservableList zur Verfügung.
 */
public class SpielerVerwalter {
    private static SpielerVerwalter instanz;

    private ObservableList<Spieler> spieler;
    private List<Spieler> spielerList;

    private boolean sortiertToggle;

    //region Konstruktoren
    private SpielerVerwalter(){
        spielerList = DbManager.getInstanz().readAllDataRecords();
        spieler= FXCollections.observableArrayList(new Callback<Spieler, Observable[]>() {
            @Override
            public Observable[] call(Spieler spieler) {
                return new Observable[]{spieler.nameProperty(),spieler.punktestandProperty(),
                spieler.spielzeitProperty()};
            }
        });

        spieler.addAll(spielerList);

        SpielerListChangeListener listener = new SpielerListChangeListener();
        spieler.addListener(listener);

    }
    //endregion

    public static synchronized SpielerVerwalter getInstanz() {
        if (instanz == null) instanz = new SpielerVerwalter();
        return instanz;
    }

    public ObservableList<Spieler> getSpieler(){
        return spieler;
    }

    public void sortierenSpielerName() {
        sortiertToggle =!sortiertToggle;
        spieler.sort(new Comparator<Spieler>() {
            @Override
            public int compare(Spieler name, Spieler naechsterName) {
                String aktuellerName = name.getName();
                String naechsterSpielerName = naechsterName.getName();

                if (sortiertToggle){
                    return aktuellerName.compareTo(naechsterSpielerName);
                }else {
                    return -aktuellerName.compareTo(naechsterSpielerName);
                }
            }
        });
    }


    public void sortierenPunktestand(){
        sortiertToggle =!sortiertToggle;
        spieler.sort(new Comparator<Spieler>() {
            @Override
            public int compare(Spieler punktestand, Spieler naechsterPunktestand) {
                Integer aktuellerPunktestand = punktestand.getPunktestand();
                Integer naechsterSpielerPunktestand = naechsterPunktestand.getPunktestand();

                if (!sortiertToggle){
                    return aktuellerPunktestand.compareTo(naechsterSpielerPunktestand);
                }else {
                    return -aktuellerPunktestand.compareTo(naechsterSpielerPunktestand);
                }
            }
        });
    }

}
