package de.jkolesnikov.tetris.logic;

import de.jkolesnikov.tetris.logic.db.DbManager;
import de.jkolesnikov.tetris.model.Spieler;
import javafx.collections.ListChangeListener;
/**
 * List Change Listener Klasse für die Spieler.
 */
public class SpielerListChangeListener implements ListChangeListener<Spieler> {

    @Override
    public void onChanged(Change <? extends Spieler> change) {
        while (change.next()){
            if (change.wasReplaced()) {
                System.out.println("Es wurde ersetzt.");
                System.out.println(change.getAddedSubList().get(0));

            } else if (change.wasRemoved()) {
                System.out.println("Es wurde gelöscht.");
                System.out.println(change.getRemoved().get(0));
                Spieler spielerDelete = change.getRemoved().get(0);
                DbManager.getInstanz().insertDataDelete(spielerDelete);

            } else if (change.wasAdded()) {
                System.out.println("Es wurde hinzugefügt.");
                System.out.println(change.getAddedSubList().get(0));
                Spieler spielerToInsert = change.getAddedSubList().get(0);
                DbManager.getInstanz().insertDataRecord(spielerToInsert);

            } else if (change.wasUpdated()) {
                System.out.println("Es wurde aktualisiert.");
                System.out.println(change.getList().get(change.getFrom()));
                Spieler spielerUpdate = change.getList().get(change.getFrom());
                DbManager.getInstanz().insertDataUpdate(spielerUpdate);
            } else if (change.wasPermutated()) {
                System.out.println("Es wurde sortiert.");

            }
        }
    }
}
