module de.jkolesnikov.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;

    opens de.jkolesnikov.tetris to javafx.fxml;
    exports de.jkolesnikov.tetris;
    exports de.jkolesnikov.tetris.gui;
    opens de.jkolesnikov.tetris.gui to javafx.fxml;
}