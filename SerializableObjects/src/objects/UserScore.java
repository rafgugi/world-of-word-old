/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.io.Serializable;

/**
 *
 * @author ARANDA
 */
public class UserScore implements Comparable<UserScore>, Serializable {

    private String user;
    private int score;
    private int rank;

    public UserScore(String user, int score) {
        this.user = user;
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(UserScore o) {
        return o.getScore() - this.getScore();
    }

    @Override
    public String toString() {
        return "#" + getRank() + " " + getUser() + " (" + getScore() + ")";
    }
}
