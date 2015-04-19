/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ARANDA
 */
public class Scoreboard implements Serializable {

    private ArrayList<UserScore> data;

    public Scoreboard(ArrayList<UserScore> data) {
        this.data = data;
    }

    /**
     * @return the data
     */
    public ArrayList<UserScore> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ArrayList<UserScore> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String s;
        int size = getData() == null ? -1 : getData().size();
        s = "Scoreboard(" + size + ")";
        for (UserScore u : getData()) {
            s += "\n" + u;
        }
        return s;
    }
}
