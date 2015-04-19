/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import objects.Envelope;
import objects.Scoreboard;
import objects.UserScore;

/**
 *
 * @author sg
 */
public class Program implements Runnable {

    private ServerSocket listener;
    private ArrayList<Client> alclient;
    private ArrayList<Kosakata> soals;
    //private Soal currentSoal;
    private ArrayList<SoalBroadcaster> sb;
    private ArrayList<UserScore> scoreboard;

    public Program() {
        alclient = new ArrayList<>();
        soals = new ArrayList<>();
        sb = new ArrayList<>();
        scoreboard = new ArrayList<>();
        try {
            listener = new ServerSocket(10110);
        } catch (IOException ex) {
            System.out.println("> Unable to start server. " + ex.getMessage());
            System.exit(1);
        }
        ngambilSoal();
        SoalBroadcaster sb1 = new SoalBroadcaster(this, 10);
        sb1.startThread();
        SoalBroadcaster sb2 = new SoalBroadcaster(this, 15);
        sb2.startThread();
        SoalBroadcaster sb3 = new SoalBroadcaster(this, 20);
        sb3.startThread();
        sb.add(sb1);
        sb.add(sb2);
        sb.add(sb3);
    }

    public ArrayList<Kosakata> getSoals() {
        return soals;
    }

    public ArrayList<UserScore> getScoreboard() {
        //updateScoreboard();
        return scoreboard;
    }
    
    public ArrayList<SoalBroadcaster> getSb() {
        return sb;
    }
    
    public SoalBroadcaster getSb(int level) {
        for (SoalBroadcaster ans : getSb()) {
            if (ans.getLevel() == level) {
                return ans;
            }
        }
        return null;
    }

    public void updateScoreboard() {
        ArrayList<UserScore> data = new ArrayList<>();
        for (Client c : alclient) {
            data.add(new UserScore(c.getName(), c.getScore()));
        }
        Collections.sort(data);

        scoreboard.clear();
        //scoreboard = FXCollections.observableArrayList();
        for (int i = 0; i < data.size(); i++) {
            UserScore u = data.get(i);
            u.setRank(i + 1);
            scoreboard.add(u);
        }
        broadcastScoreboard();
    }

    public boolean isUserExist(String user) {
        for (Client client : alclient) {
            if (client.getName().equals(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ngambil soal dari file. list soalnya ada di sources.list. Trus dari situ
     * dibaca satu persatu.
     */
    private void ngambilSoal() {
        ClassLoader cl = getClass().getClassLoader();
        File file = new File(cl.getResource("resources/sources.list").getFile());
        try {
            Scanner s;
            s = new Scanner(file);

            while (s.hasNextLine()) {
                String line = s.nextLine();
                System.out.println("> New source: " + line);

                Kosakata kosakata = new Kosakata(line);
                kosakata.getFromFile();
                getSoals().add(kosakata);
            }
            s.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    /**
     * Ngirim msg ke semua client yang terhubung
     *
     * @param msg
     */
    public void broadcast(Object msg) {
        System.out.println("> Broadcasting " + msg);
        for (Client cli : alclient) {
            cli.send(msg);
        }
    }

    public void broadcast(String msg, String cmd) {
        broadcast(new Envelope(msg, cmd));
    }

    public void broadcastSoal(int level) {
        broadcast(getSb(level).getCurrentSoal());
    }

    public void broadcastSisa(int level) {
        broadcast(level + ":" + getSb(level).getCurrentJawab().size(), "sisa");
    }

    public void broadcastScoreboard() {
        ArrayList<UserScore> u;
        u = (ArrayList<UserScore>) getScoreboard().clone();
        broadcast(new Scoreboard(u));
    }

    public void closeClient(Client client) {
        alclient.remove(client);
        printClients();
    }

    public void printClients() {
        System.out.println("> Currently " + alclient.size() + " client(s).");
    }

    /**
     * Dijalankan di thread. Menunggu ada client yang connect ke server, trus
     * dimasukkan di ArrayList.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Client client = new Client(listener.accept(), this);
                new Thread(client).start();
                alclient.add(client);
                System.out.println("> New client is join.");
                printClients();
            } catch (IOException ex) {
                System.out.println("> Error IO: " + ex.getMessage());
            }
        }
    }
}
