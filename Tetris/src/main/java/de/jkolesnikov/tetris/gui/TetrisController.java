package de.jkolesnikov.tetris.gui;

import de.jkolesnikov.tetris.logic.SpielerVerwalter;
import de.jkolesnikov.tetris.logic.musik.AudioManager;
import de.jkolesnikov.tetris.model.Spieler;
import de.jkolesnikov.tetris.model.Stein;
import de.jkolesnikov.tetris.settings.AppText;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Hier wird Tetris dargestellt als Array und später auf dem BorderPane im GridPane gezeichnet.
 */
public class TetrisController {
    Stein tStein = new Stein(new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 0, 0}});
    Stein oStein = new Stein(new int[][]{{2, 2}, {2, 2}});
    Stein jStein = new Stein(new int[][]{{0, 3, 0}, {0, 3, 0}, {0, 3, 3}});
    Stein lStein = new Stein(new int[][]{{0, 4, 0}, {0, 4, 0}, {4, 4, 0}});
    Stein zStein = new Stein(new int[][]{{5, 5, 0}, {0, 5, 5}, {0, 0, 0}});
    Stein sStein = new Stein(new int[][]{{0, 6, 6}, {6, 6, 0}, {0, 0, 0}});
    Stein iStein = new Stein(new int[][]{{0, 0, 0, 0}, {7, 7, 7, 7}, {0, 0, 0, 0}, {0, 0, 0, 0}});

    private final int spielfeldHoehe = 26;
    private final int spielfeldBreite = 12;
    private final int[][] spielfeldMitZahlen = new int[spielfeldHoehe][spielfeldBreite];
    private int positionHoeheBewegen = 3;
    private int positionBreiteBewegung = 4;
    private int punktestand = 0;
    private int geschwindigkeit = 1000;

    private boolean entfernen;
    private boolean bewegenRechts;
    private boolean bewegenLinks;
    private boolean drehen;
    private boolean bewegenUnten;
    private boolean kollision;
    private boolean neuerSpieler = true;
    private boolean spielVorbei = false;

    private double spielzeit = 0;

    private Stein randomStein;
    private Spieler spieler;

    private Timer timer;

    @FXML
    private Label punktestandLabel;
    @FXML
    private Label spielerNameLabel;
    @FXML
    private Label spielzeitLabel;
    @FXML
    BorderPane borderpane;
    @FXML
    private GridPane spielfeld;


    public void spieler(Spieler spieler) {

        this.spieler = spieler;
        spielerNameLabel.setText(spieler.getName());
        if (spieler.getSpielzeit() != 0d) {
            System.out.println("nicht neuer Spieler");
            neuerSpieler = false;
        }
    }

    @FXML
    public void initialize() {
        starteSpiel();
        AudioManager.getInstanz().startMusik("musik/tetris.mp3");

    }

    private void starteSpiel() {

        generiereSpielfeldMitZahlen();
        spielzeit();
        generiereRandomStein();
        bewegenOderPlatzieren();
        automatischeBewegung();

        spielfeld.setFocusTraversable(true);
        spielfeld.requestFocus();

        spielfeld.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                drehen = true;
                kollisionErkannt();
                if (drehen) {
                    randomStein.drehen();
                    kollisionErkannt();
                    bewegenOderPlatzieren();
                }
            }
        });

        spielfeld.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) {
                if (bewegenLinks) {
                    --positionBreiteBewegung;
                    bewegenOderPlatzieren();
                }
            } else if (event.getCode() == KeyCode.D) {
                if (bewegenRechts) {
                    ++positionBreiteBewegung;
                    bewegenOderPlatzieren();
                }

            } else if (event.getCode() == KeyCode.S) {
                if (bewegenUnten) {
                    ++positionHoeheBewegen;
                    bewegenOderPlatzieren();
                }
            }
        });
    }


    private void automatischeBewegung() {
        Thread thread = new Thread(() -> {
            while (!spielVorbei) {
                try {
                    Thread.sleep(geschwindigkeit);
                    Platform.runLater(() -> {
                        System.out.println(geschwindigkeit);
                        if (bewegenUnten) {
                            positionHoeheBewegen++;
                            bewegenOderPlatzieren();

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    private void bewegenOderPlatzieren() {
        linksRechtsUntenKollision();
        if (!kollision) {
            steinSetztenImArray();
            entfernen = true;
            spielfeldZeichner();
            entferneSteinVomSpielfeldMitZahlen();

        } else if (kollision) {
            spielZuEnde();
            steinSetztenImArray();
            entfernen = false;
            spielfeldZeichner();
            ueberpruefeReihen();
            positionHoeheBewegen = 3;
            positionBreiteBewegung = 4;
            generiereRandomStein();
            bewegenUnten = true;
            schwierigkeitsgrad();
        }


    }


    private void generiereSpielfeldMitZahlen() {
        for (int i = 0; i < spielfeldMitZahlen.length; i++) {
            for (int j = 0; j < spielfeldMitZahlen[i].length; j++) {

                if (i == spielfeldMitZahlen.length - 1 || j == 0 || j == spielfeldMitZahlen[i].length - 1) {
                    spielfeldMitZahlen[i][j] = 10;
                } else {
                    spielfeldMitZahlen[i][j] = 0;
                }
            }
        }
    }


    private void generiereRandomStein() {
        Stein[] steineArray = new Stein[]
                {tStein, oStein, jStein, lStein, zStein, sStein, iStein};

        Random random = new Random();
        int randomZahl = random.nextInt(steineArray.length);
        this.randomStein = steineArray[randomZahl];
    }


    private void linksRechtsUntenKollision() {
        kollision = false;
        bewegenLinks = true;
        bewegenRechts = true;
        bewegenUnten = true;

        for (int i = 0; i < randomStein.getForm().length; i++) {
            for (int j = 0; j < randomStein.getForm()[i].length; j++) {
                if (randomStein.getForm()[i][j] != 0) {

                    int aktuellePositionX = j + positionBreiteBewegung;
                    int aktuellePositionY = i + positionHoeheBewegen;
                    int positionNachDemStein = (j + positionBreiteBewegung) + 1;
                    int positionUnterDemStein = (i + positionHoeheBewegen) + 1;
                    int positionHinterDemStein = (j + positionBreiteBewegung) - 1;

                    if (spielfeldMitZahlen[aktuellePositionY][positionNachDemStein] != 0 &&
                            spielfeldMitZahlen[aktuellePositionY][positionHinterDemStein] != 0) {
                        drehen = false;

                    }

                    try {
                        if (spielfeldMitZahlen[aktuellePositionY][positionNachDemStein] != 0) {

                            System.out.println("rechts");
                            bewegenRechts = false;
                        }
                    } catch (Exception e) {
                        System.out.println("error rechts bewegen");
                        positionBreiteBewegung--;
                    }

                    try {
                        if (spielfeldMitZahlen[aktuellePositionY][positionHinterDemStein] != 0) {
                            System.out.println("links");
                            bewegenLinks = false;
                        }
                    } catch (Exception e) {
                        System.out.println("error links bewegen");
                        positionBreiteBewegung++;

                    }
                    try {
                        if (spielfeldMitZahlen[positionUnterDemStein][aktuellePositionX] != 0) {
                            bewegenUnten = false;
                            kollision = true;
                            System.out.println("kollision");
                        }
                    } catch (Exception e) {
                        kollisionErkannt();
                        System.out.println("Exception kollision");
                    }
                }
            }
        }
    }


    private void spielzeit() {
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    spielzeit = spielzeit + 0.1;
                    String zeitFormat = String.format(AppText.TXT_FORMAT, spielzeit);
                    spielzeitLabel.setText(AppText.TXT_ZEIT + zeitFormat);
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 100);
    }


    private void kollisionErkannt() {

        for (int i = 0; i < randomStein.getForm().length; i++) {
            for (int j = 0; j < randomStein.getForm()[i].length; j++) {

                if (randomStein.getForm()[i][j] != 0) {

                    int aktuellePositionX = j + positionBreiteBewegung;
                    int aktuellePositionY = i + positionHoeheBewegen;
                    int positionNachDemStein = (j + positionBreiteBewegung) + 1;
                    int positionUnterDemStein = (i + positionHoeheBewegen) + 1;
                    int positionHinterDemStein = (j + positionBreiteBewegung) - 1;


                    if ((randomStein.getForm()[0].length + positionBreiteBewegung) > spielfeldBreite - 2) {
                        positionBreiteBewegung--;
                        System.out.println("behandlung drehen rechts");


                    } else if (positionBreiteBewegung < 1) {
                        positionBreiteBewegung++;
                        System.out.println("behandlung drehen links");

                    }
                    if (spielfeldMitZahlen[aktuellePositionY][aktuellePositionX] != 0) {
                        if (spielfeldMitZahlen[aktuellePositionY][positionNachDemStein] != 0 &&
                                spielfeldMitZahlen[aktuellePositionY][positionHinterDemStein] != 0) {
                            drehen = false;
                            break;
                        }
                        if (spielfeldMitZahlen[aktuellePositionY][positionNachDemStein] == 0) {
                            System.out.println("nach dem stein rechts");
                            positionBreiteBewegung--;
                        } else if (spielfeldMitZahlen[aktuellePositionY][positionHinterDemStein] == 0) {
                            System.out.println("hinter dem stein links");
                            positionBreiteBewegung++;
                        }
                        if (spielfeldMitZahlen[positionUnterDemStein][aktuellePositionX] == 0) {
                            System.out.println("unterhalb des steines");
                            positionHoeheBewegen--;

                        }

                    }

                }
            }
        }
    }

    private void ueberpruefeReihen() {

        for (int i = spielfeldMitZahlen.length - 2; i >= 4; i--) {

            boolean reiheVoll = true;

            for (int j = 1; j < spielfeldMitZahlen[i].length - 1; j++) {

                if (spielfeldMitZahlen[i][j] == 0) {
                    reiheVoll = false;
                    break;
                }
            }
            if (reiheVoll) {

                for (int j = 1; j < spielfeldMitZahlen[i].length - 1; j++) {
                    spielfeldMitZahlen[i][j] = 0;
                }

                punktestand = punktestand + 10;
                punktestandLabel.setText(AppText.TXT_PUNKTE + punktestand);

                for (int k = i; k > 4; k--) {

                    for (int j = 1; j < spielfeldMitZahlen[k].length - 1; j++) {
                        spielfeldMitZahlen[k][j] = spielfeldMitZahlen[k - 1][j];

                    }
                }
                for (int j = 1; j < spielfeldMitZahlen[1].length - 1; j++) {
                    spielfeldMitZahlen[4][j] = 0;
                }
                ueberpruefeReihen();
            }
        }
    }


    private void steinSetztenImArray() {
        for (int i = 0; i < randomStein.getForm().length; i++) {
            for (int j = 0; j < randomStein.getForm()[i].length; j++) {

                if (randomStein.getForm()[i][j] != 0) {
                    spielfeldMitZahlen[i + positionHoeheBewegen][j + positionBreiteBewegung] = randomStein.getForm()[i][j];

                }

            }
        }
    }

    private void entferneSteinVomSpielfeldMitZahlen() {
        if (entfernen) {

            for (int i = 0; i < randomStein.getForm().length; i++) {
                for (int j = 0; j < randomStein.getForm()[i].length; j++) {

                    if (randomStein.getForm()[i][j] != 0) {
                        spielfeldMitZahlen[i + positionHoeheBewegen][j + positionBreiteBewegung] = 0;
                    }
                }
            }
        }
    }

    private void spielfeldZeichner() {
        spielfeld.getChildren().clear();

        for (int i = 4; i < spielfeldHoehe; i++) {
            for (int j = 0; j < spielfeldBreite; j++) {

                Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();

                switch (spielfeldMitZahlen[i][j]) {
                    case 1 -> gc.setFill(Color.PURPLE);
                    case 2 -> gc.setFill(Color.YELLOW);
                    case 3 -> gc.setFill(Color.ORANGE);
                    case 4 -> gc.setFill(Color.BLUE);
                    case 5 -> gc.setFill(Color.RED);
                    case 6 -> gc.setFill(Color.GREEN);
                    case 7 -> gc.setFill(Color.CYAN);
                    case 10 -> gc.setFill(Color.BLACK);
                    default -> gc.setFill(Color.WHITE);
                }

                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setStroke(Color.BLACK);

                gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
                spielfeld.add(canvas, j, i);
            }
        }
    }

    public void schwierigkeitsgrad() {
        if (punktestand > 50 && punktestand < 100) {
            geschwindigkeit = 600;

        }else if (punktestand >= 100 && punktestand < 200) {
            geschwindigkeit = 400;

        } else if (punktestand >= 200) {
            geschwindigkeit = 200;
        }



    }

    public void spielZuEnde() {

        for (int j = 1; j < spielfeldMitZahlen[5].length - 1; j++) {

            if (spielfeldMitZahlen[5][j] != 0) {
                System.out.println("gameover");
                spielVorbei = true;
                timer.cancel();
                Alert spielZuEnde = new Alert(Alert.AlertType.INFORMATION);
                spielZuEnde.setTitle(AppText.TXT_SPIEL_BEENDET);
                spielZuEnde.setHeaderText(AppText.TXT_SPIEL_BEENDET);

                spielZuEnde.setContentText(String.format
                        (AppText.TXT_FORMAT_PUNKTE_SPIELZEIT, spieler.getName(), punktestand, spielzeit));
                Optional<ButtonType> weiter = spielZuEnde.showAndWait();

                if (weiter.get() == ButtonType.OK) {
                    AudioManager.getInstanz().stopMusik();
                    if (neuerSpieler) {
                        spieler.setSpielzeit(spielzeit);
                        spieler.setPunktestand(punktestand);
                        SpielerVerwalter.getInstanz().getSpieler().add(spieler);
                        SceneManager.getInstance().wechselZuPunkteStandSzene();
                    } else {
                        spieler.setName(spieler.getName());
                        spieler.setSpielzeit(spielzeit);
                        spieler.setPunktestand(punktestand);
                        SceneManager.getInstance().wechselZuPunkteStandSzene();

                    }

                }
            }
        }
    }
}
