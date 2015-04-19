/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Random;

/**
 *
 * @author sg
 */
public class Filler {

    private final String[][] huruf;
    private final int ordo;

    public Filler(int ordo) {
        this.ordo = ordo;
        huruf = new String[ordo][ordo];
    }

    public String[][] getHuruf() {
        return huruf;
    }

    public int getOrdo() {
        return ordo;
    }

    public void randomHuruf() {
        for (int i = 0; i < ordo; i++) {
            for (int j = 0; j < ordo; j++) {
                huruf[i][j] = "" + (char) (new Random().nextInt(26) + 'A');
            }
        }
    }
}
