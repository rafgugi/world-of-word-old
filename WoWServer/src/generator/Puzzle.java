/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author sg
 */
public class Puzzle {

    private final int ordo;
    private final int maxloop;
    private int debug;
    private ArrayList<Word> availableWords;
    private ArrayList<Word> currentWordArrayList;
    private ArrayList<ArrayList<Character>> grid;

    public Puzzle(int ordo, ArrayList<Word> availableWords) {
        this.ordo = ordo;
        this.maxloop = 5000;
        debug = 0;
        this.availableWords = availableWords;
        currentWordArrayList = new ArrayList<>();
        clearGrid();
        randomizeWordArrayList();
    }

    public static Boolean containWord(ArrayList<Word> obj, String str) {
        for (Word w : obj) {
            if (w.word.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static String[][] normalizeGrid(ArrayList<ArrayList<Character>> grid) {
        int ordo = grid.size();
        String[][] ans = new String[ordo][ordo];

        for (int i = 0; i < ordo; i++) {
            for (int j = 0; j < ordo; j++) {
                ans[i][j] = "" + grid.get(i).get(j);
            }
        }
        return ans;
    }

    public static ArrayList<String> normalizeWords(ArrayList<Word> words) {
        ArrayList<String> ans = new ArrayList<>();
        for (Word word : words) {
            ans.add(word.word);
        }
        return ans;
    }

    public static ArrayList<Word> normalizeStrings(ArrayList<String> strings) {
        ArrayList<Word> ans = new ArrayList<>();
        for (String string : strings) {
            ans.add(new Word(string));
        }
        return ans;
    }

    private void clearGrid() {
        this.grid = new ArrayList<>();
        for (int i = 0; i < ordo; i++) {
            ArrayList<Character> eaRow = new ArrayList<>();
            for (int j = 0; j < ordo; j++) {
                eaRow.add('-');
            }
            getGrid().add(eaRow);
        }
    }

    private void randomizeWordArrayList() {
        ArrayList<Word> tempArrayList = new ArrayList<>();

        for (Word w : availableWords) {
            tempArrayList.add(w);
        }
        Collections.shuffle(tempArrayList);

        //Collections.sort(tempArrayList);
        this.availableWords = tempArrayList;
    }

    public void computePuzzle(long timeLimit, int iter) {
        int count = 0;
        Puzzle copy = new Puzzle(ordo, this.availableWords);

        long t = System.currentTimeMillis();
        long end = t + (timeLimit * 1000);
        while ((System.currentTimeMillis() < end) || count == 0) {
            (this.debug)++;
            copy.currentWordArrayList = new ArrayList<>();
            copy.clearGrid();
            copy.randomizeWordArrayList();
            for (int x = 0; x < iter; x++) {
                for (Word word : availableWords) {
                    if (!containWord(copy.currentWordArrayList, word.word)) {
                        copy.fitAndAdd(word);
                    }
                }
            }
            if (copy.getCurrentWordArrayList().size() > this.getCurrentWordArrayList().size()) {
                this.currentWordArrayList = copy.getCurrentWordArrayList();
                this.grid = copy.getGrid();
            }
            count++;
        }
    }

    public void fitAndAdd(Word word) {
        Boolean fit = false;
        int count = 0;
        ArrayList<Coordinate> coordlist = this.suggestCoord(word);
        int vertical, tcol, trow;

        while (fit == false && count < this.maxloop) {
            if (this.getCurrentWordArrayList().isEmpty()) {
                Random rand = new Random();
                vertical = rand.nextInt(2);
                tcol = 1;
                trow = 1;

                if (this.checkFitScore(tcol, trow, vertical, word) > 0) {
                    fit = true;
                    this.setWord(tcol, trow, vertical, word);
                }
            } else {
                try {
                    tcol = coordlist.get(count).col;
                    trow = coordlist.get(count).row;
                    vertical = coordlist.get(count).vertical;
                    if (coordlist.get(count).score > 0) {
                        fit = true;
                        this.setWord(tcol, trow, vertical, word);
                    }
                } catch (Exception ex) {
                    return;
                }
            }
            count++;
        }
    }

    public ArrayList<Coordinate> suggestCoord(Word word) {
        ArrayList<Coordinate> coordlist = new ArrayList<>();
        int glc = -1;
        for (Character givenLetter : word.word.toCharArray()) {
            glc++;
            int rowc = 0;
            for (ArrayList<Character> trow : this.getGrid()) {
                rowc++;
                int colc = 0;
                for (Character cell : trow) {
                    colc++;
                    if (givenLetter.equals(cell)) {
                        if (rowc - glc > 0) {
                            if (((rowc - glc) + word.length) <= ordo) {
                                coordlist.add(new Coordinate(colc, rowc - glc, 1, colc + (rowc - glc), 0));
                            }
                        }
                        if (colc - glc > 0) {
                            if (((colc - glc) + word.length) <= ordo) {
                                coordlist.add(new Coordinate(colc - glc, rowc, 0, rowc + (colc - glc), 0));
                            }
                        }
                    }
                }
            }
        }
        ArrayList<Coordinate> newCoordlist = this.sortCoordlist(coordlist, word);
        return newCoordlist;
    }

    public ArrayList<Coordinate> sortCoordlist(ArrayList<Coordinate> coordlist, Word word) {
        ArrayList<Coordinate> newCoordlist = new ArrayList<>();

        for (Coordinate coord : coordlist) {
            if ((coord.score = this.checkFitScore(coord.col, coord.row, coord.vertical, word)) > 0) {
                newCoordlist.add(coord);
            }
        }
        //Collections.shuffle(newCoordlist);

        Collections.sort(newCoordlist);
        return newCoordlist;
    }

    public int checkFitScore(int col, int row, int vertical, Word word) {
        if (col < 1 || row < 1) {
            return 0;
        }

        int count = 1;
        int score = 1;
        Character activeCell;

        for (Character letter : word.word.toCharArray()) {

            activeCell = this.getCell(col, row);
            if (activeCell == '.') {
                return 0;
            }
            if (!activeCell.equals('-') && !activeCell.equals(letter)) {
                return 0;
            }
            if (activeCell.equals(letter)) {
                score++;
            }
            if (vertical > 0) {
                if (!activeCell.equals(letter)) {
                    try {
                        if (!(this.checkIfCellClear(col + 1, row))) {
                            return 0;
                        }
                    } catch (Exception ex) {
                    }
                    try {
                        if (!(this.checkIfCellClear(col - 1, row))) {
                            return 0;
                        }
                    } catch (Exception ex) {
                    }
                }
                try {
                    if (count == 1) {
                        if (!(this.checkIfCellClear(col, row - 1))) {
                            return 0;
                        }
                    }
                } catch (Exception ex) {
                }
                try {
                    if (count == word.word.length()) {
                        if (!(this.checkIfCellClear(col, row + 1))) {
                            return 0;
                        }
                    }
                } catch (Exception ex) {
                }
                row++;
            } else {
                if (!activeCell.equals(letter)) {
                    try {
                        if (!(this.checkIfCellClear(col, row - 1))) {
                            return 0;
                        }
                    } catch (Exception ex) {
                    }
                    try {
                        if (!(this.checkIfCellClear(col, row + 1))) {
                            return 0;
                        }
                    } catch (Exception ex) {
                    }
                }
                try {
                    if (count == 1) {
                        if (!(this.checkIfCellClear(col - 1, row))) {
                            return 0;
                        }
                    }
                } catch (Exception ex) {
                }
                try {
                    if (count == word.word.length()) {
                        if (!(this.checkIfCellClear(col + 1, row))) {
                            return 0;
                        }
                    }
                } catch (Exception ex) {
                }
                col++;
            }
            count++;
        }
        return score;
    }

    public void setWord(int col, int row, int vertical, Word word) {
        word.col = col;
        word.row = row;
        word.vertical = vertical;
        this.getCurrentWordArrayList().add(word);
        for (Character letter : word.word.toCharArray()) {
            this.setCell(col, row, letter);
            if (vertical > 0) {
                row++;
            } else {
                col++;
            }
        }
    }

    public void setCell(int col, int row, Character value) {
        this.getGrid().get(row - 1).set(col - 1, value);
    }

    public Character getCell(int col, int row) {
        return this.getGrid().get(row - 1).get(col - 1);
    }

    public Boolean checkIfCellClear(int col, int row) {
        return this.getCell(col, row).equals('-');
    }
    /*
     public String wordBank() {
     String outStr = "";
     ArrayList<Word> tempArrayList = this.currentWordArrayList;
     Collections.shuffle(tempArrayList);

     for (Word word : tempArrayList) {
     outStr += word.word + "\n";
     }
     return outStr;
     }
     */

    public String[][] wordFind() {
        String[][] ans = new String[ordo][ordo];
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        final int len = alphabet.length();
        for (int r = 0; r < ordo; r++) {
            for (int i = 0; i < ordo; i++) {
                char c = grid.get(r).get(i);
                if (c == '-') {
                    c = alphabet.charAt(new Random().nextInt(len));
                }
                ans[r][i] = "" + c;
            }
        }
        return ans;
    }

    /**
     * @return the currentWordArrayList
     */
    public ArrayList<Word> getCurrentWordArrayList() {
        return currentWordArrayList;
    }

    /**
     * @return the grid
     */
    public ArrayList<ArrayList<Character>> getGrid() {
        return grid;
    }

}
