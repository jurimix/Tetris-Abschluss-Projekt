package de.jkolesnikov.tetris.gui;

import de.jkolesnikov.tetris.model.Spieler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Die Klasse "MenuController" ist der Controller für die FXML-Datei des Hauptmenüs von Tetris.
 */
public class MenuController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private Button spielenButton;
    @FXML
    private Button ladenButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spielenButton.setDisable(true);
        ladenButton.setDisable(false);
    }

    @FXML
    private void oeffnenSpielerLadenSzene() {
        SceneManager.getInstance().wechselZuSpielstandSzene();
    }


    @FXML
    private void oeffnenTetrisSzeneMitSpieler() {
        String name = nameTextField.getText().trim();
        if (!name.isEmpty()) {
            Spieler spieler = new Spieler();
            spieler.setName(name);

            SceneManager.getInstance().wechselZuTetrisSzene(spieler);
        }
    }

    @FXML
    private void textFeld() {
        spielenButton.setDisable(nameTextField.getText().trim().isEmpty());
        ladenButton.setDisable(!nameTextField.getText().trim().isEmpty());

    }
}