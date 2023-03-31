package de.jkolesnikov.tetris.gui;

import de.jkolesnikov.tetris.Main;
import de.jkolesnikov.tetris.model.Spieler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Die Klasse "SceneManager" ist zuständig für die Verwaltung der Szenen in der JavaFX-Anwendung.
 * Sie ist als Singleton implementiert, um sicherzustellen, dass nur eine Instanz dieser Klasse existiert.
 */
public class SceneManager {

    private static SceneManager instanz;
    private Stage stage;

    //Konstruktoren
    SceneManager() {
    }
    //endregion

    //Singleton sorgt dafür das die Szene nur einmal wechselt
    public static synchronized SceneManager getInstance() {
        if (instanz == null) instanz = new SceneManager();
        return instanz; //return ist neues Objekt des SceneManger die Klasse wird aufgerufen
    }
    //endregion

    // Methode zum Setzen der Stage
    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Tetris");
        wechselZuMenueSzene();
    }


    private void wechselSzene(Scene scene){
        stage.setScene(scene);
        stage.show();

    }

    public void wechselZuTetrisSzene(Spieler spieler) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tetris.fxml"));
            Scene tetrisSzene = new Scene(fxmlLoader.load());

            TetrisController tetrisController = fxmlLoader.getController();
            tetrisController.spieler(spieler);

            wechselSzene(tetrisSzene);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void wechselZuMenueSzene() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu.fxml"));
            Scene menuSzene = new Scene(fxmlLoader.load());
            wechselSzene(menuSzene);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public  void wechselZuPunkteStandSzene(){
        try {FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("punktestand-view.fxml"));
            Scene punktestandSzene = new Scene(fxmlLoader.load());

            wechselSzene(punktestandSzene);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void wechselZuSpielstandSzene(){
        try {FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("spieler-laden-view.fxml"));
            Scene punktestandSzene = new Scene(fxmlLoader.load());

            wechselSzene(punktestandSzene);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
