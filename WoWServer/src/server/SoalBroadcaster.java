/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import generator.Puzzle;
import java.util.ArrayList;
import java.util.Random;
import objects.Soal;

/**
 *
 * @author sg
 */
public class SoalBroadcaster implements Runnable {

    private final Program program;
    private final Thread thread;
    private final int level;
    private Soal currentSoal;
    private ArrayList<String> currentJawab;

    public SoalBroadcaster(Program program, int level) {
        this.program = program;
        this.level = level;
        this.thread = new Thread(this);
    }

    public Soal getCurrentSoal() {
        return currentSoal;
    }

    public void setCurrentSoal(Soal currentSoal) {
        this.currentSoal = currentSoal;
    }

    public ArrayList<String> getCurrentJawab() {
        return currentJawab;
    }

    public void setCurrentJawab(ArrayList<String> currentJawab) {
        this.currentJawab = currentJawab;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    public boolean isJawabExist(String jawab) {
        int index = currentJawab.indexOf(jawab.toLowerCase());
        if (index < 0) {
            return false;
        } else {
            currentJawab.remove(index);
            return true;
        }
    }

    public void startThread() {
        thread.start();
    }

    public void randomKosakata() {
        ArrayList<Kosakata> soals = program.getSoals();
        Kosakata k = soals.get(new Random().nextInt(soals.size()));
        Puzzle puzzle = new Puzzle(getLevel(), Puzzle.normalizeStrings(k.getIsi()));
        puzzle.computePuzzle(2, 2);
        
        Soal soal;
        ArrayList<String> jawab;
        soal = new Soal(puzzle.wordFind(), k.getTitle());
        jawab = Puzzle.normalizeWords(puzzle.getCurrentWordArrayList());
        
        setCurrentSoal(soal);
        setCurrentJawab(jawab);
    }

    @Override
    public void run() {
        while (true) {
            //Filler filler;
            //filler = new Filler(15);
            //filler.randomHuruf();
            //program.broadcast(new Soal(filler.getHuruf(), "Random"));
            randomKosakata();
            program.broadcastSoal(level);
            program.broadcastSisa(level);

            try {
                for (int i = getLevel() * 3; i > 0; i--) {
                    program.broadcast(level + ":" + i, "waktu");
                    Thread.sleep(999);
                }
            } catch (InterruptedException ex) {
                System.out.println("> Interrupted: " + ex.getMessage());
            }
        }
    }

}
