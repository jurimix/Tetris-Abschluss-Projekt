package de.jkolesnikov.tetris.gui;

import de.jkolesnikov.tetris.gui.listenansicht.SpielerCellFactory;
import de.jkolesnikov.tetris.logic.SpielerVerwalter;
import de.jkolesnikov.tetris.model.Spieler;
import de.jkolesnikov.tetris.settings.AppText;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Die Klasse "SpielerLadenController" ist der Controller für die FXML-Datei, dort kann der Spieler sein Profil laden.
 */
public class SpielerLadenController implements Initializable {
    Spieler spieler;

    @FXML
    private ListView<Spieler> spielerListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpielerCellFactory spielerCellFactory = new SpielerCellFactory();
        spielerListView.setCellFactory(spielerCellFactory);
        spielerListView.setItems(SpielerVerwalter.getInstanz().getSpieler());
        spielerListView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
               spieler = spielerListView.getSelectionModel().getSelectedItem();

            }
        });
    }

    @FXML
    private void spielstandLaden() {
        if (spieler != null) {
            SceneManager.getInstance().wechselZuTetrisSzene(spieler);
            }
        }

    @FXML
    private void oeffnenMenueSzene(){
        SceneManager.getInstance().wechselZuMenueSzene();
    }

    @FXML
    private void sortierenSpieler(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof Button sortButton) {
            switch (sortButton.getText()) {
                case AppText.TXT_PUNKTESTAND -> SpielerVerwalter.getInstanz().sortierenPunktestand();
                case AppText.TXT_NAMEN -> SpielerVerwalter.getInstanz().sortierenSpielerName();

            }
        }
    }
    @FXML
    private void spielerLoeschen() {
        if (spieler != null) {
            Alert loeschenAlert = new Alert(Alert.AlertType.CONFIRMATION);
            loeschenAlert .setTitle(AppText.TXT_SPIELER_LOESCHEN);
            loeschenAlert .setHeaderText(String.format(AppText.TXT_WIRKLICH_LOESCHEN,spieler.getName()));
            loeschenAlert .setContentText(spieler.toString());
            Optional<ButtonType> result = loeschenAlert .showAndWait();

            if (result.get() == ButtonType.OK) {
                SpielerVerwalter.getInstanz().getSpieler().remove(spieler);
                SceneManager.getInstance().wechselZuPunkteStandSzene();
            }
        }
    }

}
