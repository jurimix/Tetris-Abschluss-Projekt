package de.jkolesnikov.tetris.model;

/**
 * Modellklasse für die Steine/Form
 */
public class Stein {
    private int [][] form;

    public Stein(int[][] form) {
        this.form = form;
    }

    public int[][] getForm() {
        return form;
    }


    public void drehen() {
        int n = form.length;
        int[][] neueForm = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                neueForm[i][j] = form[n-j-1][i];
            }
        }

        form = neueForm;
    }

}

