/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author sg
 */
public class Kosakata {

    public ArrayList<String> isi;
    private final String source;
    private final String title;

    public Kosakata(String source, String title) {
        isi = new ArrayList<>();
        this.source = "resources/" + source;
        this.title = title;
    }

    public Kosakata(String[] s) {
        this(s[0], s[1]);
    }

    public Kosakata(String s) {
        this(s.split(";"));
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getIsi() {
        return isi;
    }

    public String getRandomString() {
        return isi.get(new Random().nextInt(isi.size()));
    }

    public void getFromFile() {
        ClassLoader cl = getClass().getClassLoader();
        File file = new File(cl.getResource(source).getFile());
        try {
            Scanner s;
            s = new Scanner(file);

            while (s.hasNextLine()) {
                String line = s.nextLine();
                isi.add(line.trim());
                //System.out.println(line);
            }
            s.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println("> Kosakata: " + this + " added");
    }
    
    @Override
    public String toString() {
        return title + "(" + isi.size() + ")";
    }
}
