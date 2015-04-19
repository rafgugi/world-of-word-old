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
public class Word implements Comparable<Word> {

    public String word;
    public int length;
    public int row;
    public int col;
    public int vertical;
    public int number;

    public Word(String word) {
        this.word = word;
        this.length = word.length();
        this.row = this.col = this.number = 0;
        this.vertical = -1;
    }

    @Override
    public int compareTo(Word that) {
        if (this.length < that.length) {
            return 1;
        } else if (this.length > that.length) {
            return -1;
        } else {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return word;
    }

}
