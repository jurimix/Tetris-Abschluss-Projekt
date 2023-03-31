package de.jkolesnikov.tetris.gui.listenansicht;

import de.jkolesnikov.tetris.model.Spieler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

/**
 * Die Klasse "SpielerCell" ist eine benutzerdefinierte Zelle für eine ListView,
 * die einen Spieler mit seinem Namen, Punktestand und Spielzeit anzeigt.
 */
public class SpielerCell extends ListCell<Spieler> {
    private final Label nameLabel = new Label();
    private final Label punkteLabel = new Label();
    private final Label spielzeitLabel = new Label();
    private final GridPane gridPane = new GridPane();

    public SpielerCell() {
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        nameLabel.setMinWidth(120);
        punkteLabel.setMinWidth(70);
        spielzeitLabel.setMinWidth(70);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(punkteLabel, 1, 0);
        gridPane.add(spielzeitLabel, 2, 0);
        setGraphic(gridPane);
    }

    @Override
    protected void updateItem(Spieler spielerAnzeigen, boolean istLeer) {
        super.updateItem(spielerAnzeigen, istLeer);

        if (istLeer || spielerAnzeigen == null) {
            setText(null);
            setGraphic(null);
        } else {
            nameLabel.setText(spielerAnzeigen.getName());
            punkteLabel.setText(String.valueOf(spielerAnzeigen.getPunktestand()));
            spielzeitLabel.setText(String.format("%.1f sek.", spielerAnzeigen.getSpielzeit()));
            setGraphic(gridPane);
        }
    }
}
