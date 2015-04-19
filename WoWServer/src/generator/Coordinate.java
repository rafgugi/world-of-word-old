/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

/**
 *
 * @author sg
 */
public class Coordinate implements Comparable<Coordinate> {

    public int col;
    public int row;
    public int vertical;
    public int colrow;
    public int score;

    public Coordinate(int colc, int rowc, int verticalc, int colrowc, int scorec) {
        this.col = colc;
        this.row = rowc;
        this.vertical = verticalc;
        this.colrow = colrowc;
        this.score = scorec;
    }

    @Override
    public int compareTo(Coordinate that) {
        if (this.score < that.score) {
            return 1;
        } else if (this.score > that.score) {
            return -1;
        } else {
            return 0;
        }
    }
}
