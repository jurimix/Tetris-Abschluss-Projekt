package de.jkolesnikov.tetris;

import de.jkolesnikov.tetris.gui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Das ist die Main Klasse und startet im SceneManager die Stage
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.getInstance().setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}