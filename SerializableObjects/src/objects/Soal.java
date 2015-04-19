/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.io.Serializable;

/**
 *
 * @author sg
 */
public final class Soal implements Serializable {

    private String[][] soal;
    private int ordo;
    private String kategori;

    public Soal(String[][] soal, String kategori) {
        setSoal(soal);
        setKategori(kategori);
    }

    /**
     * @return the soal
     */
    public String[][] getSoal() {
        return soal;
    }

    /**
     * @param soal the soal to set
     */
    public void setSoal(String[][] soal) {
        this.soal = soal;
        setOrdo(soal.length);
    }

    /**
     * @return the ordo
     */
    public int getOrdo() {
        return ordo;
    }

    /**
     * @param ordo the ordo to set
     */
    private void setOrdo(int ordo) {
        this.ordo = ordo;
    }

    /**
     * @return the kategori
     */
    public String getKategori() {
        return kategori;
    }

    /**
     * @param kategori the kategori to set
     */
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    @Override
    public String toString() {
        return "Soal(" + kategori + ", " + ordo + ")";
    }
}
