package de.jkolesnikov.tetris.gui.listenansicht;

import de.jkolesnikov.tetris.model.Spieler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Die Klasse "SpielerCellFactory" implementiert das "Callback" Interface.
 * Um die ListView besser anzupassen.
 */

public class SpielerCellFactory implements Callback<ListView<Spieler>,ListCell<Spieler>> {
    @Override
    public ListCell<Spieler> call(ListView<Spieler> spielerListView) {
        return new SpielerCell();
    }
}
