/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import objects.Envelope;
import objects.Soal;

/**
 *
 * @author sg
 */
public final class Client implements Runnable {

    private final Socket socket;
    private Program program;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String name;
    private int level;
    private int score;

    public Client(Socket sock, Program program) {
        this.socket = sock;
        this.program = program;
        this.name = "anonymous";
        this.level = 15;
        this.score = 0;
        try {
            out = new ObjectOutputStream(getSocket().getOutputStream());
            in = new ObjectInputStream(getSocket().getInputStream());
        } catch (IOException ex) {
            System.out.println("> Error IO: " + ex.getMessage());
        }
    }

    /**
     * Ngirim pesan msg ke client tsb.
     *
     * @param msg
     */
    public void send(Object msg) {
        try {
            if (msg instanceof Soal) {
                if (((Soal) msg).getOrdo() != level) {
                    return;
                }
            } else if (msg instanceof Envelope) {
                Envelope env = (Envelope) msg;
                if ("waktu".equals(env.getDesc()) || "sisa".equals(env.getDesc())) {
                    String[] string;
                    string = env.getMessage().split(":");
                    if (Integer.parseInt(string[0]) != level) {
                        return;
                    }
                    env = new Envelope(string[1], env.getDesc());
                    msg = env;
                }
            }
            out.writeObject(msg);
            System.out.println("> Send to " + name + ": " + msg);
        } catch (IOException ex) {
            System.out.println("> Sending error: " + ex.getLocalizedMessage());
            System.out.println("when sending " + msg);
        }
    }

    public void send(String msg, String cmd) {
        send(new Envelope(msg, cmd));
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    private void handleEnvelope(Envelope env) throws InterruptedException {
        System.out.println("> Receiving " + env);
        String message = env.getMessage();
        String command = env.getDesc();
        switch (command) {
            case "exit":
                send(null);
                break;
            case "login":
                String[] login = message.split(":");
                if (login.length == 2) {
                    String user = login[0];
                    String pass = login[1];
                    if (user.length() < 4 || user.length() > 10) {
                        send("User length must between 4 and 10.", "login");
                        break;
                    } else if (program.isUserExist(user)) {
                        send("User alredy exist.", "login");
                        break;
                    }
                    name = user;
                    send("success", "login");
                    Thread.sleep(100);
                    program.broadcast(name + " has joined.", "pesan");
                    program.updateScoreboard();
                } else {
                    send("Insert password.", "login");
                }
                break;
            case "level":
                setLevel(Integer.parseInt(message));
                send(program.getSb(level).getCurrentSoal());
                send(program.getSb(level).getCurrentJawab().size() + "", "sisa");
                break;
            case "register":
                break;
            case "pesan":
                if (program.getSb(level).isJawabExist(message)) {
                    score += message.length();
                    send(getScore() + "", "score");
                    program.broadcastSisa(level);
                    program.updateScoreboard();
                }
                String go = "(" + getScore() + ") " + name + ": " + message;
                program.broadcast(go, "pesan");
                break;
            case "minta":
                if (message.equals("init")) {
                    send(getName(), "user");
                    send(program.getSb(level).getCurrentSoal());
                    send(getScore() + "", "score");
                    send(program.getSb(level).getCurrentJawab().size() + "", "sisa");
                }
                break;
        }
    }

    /**
     * Dijalankan di thread. Ini looping terus untuk menerima pesan dari client.
     */
    @Override
    public void run() {
        Object msg;
        try {
            // menunggu client untuk memasukkan pesan.
            while (true) {
                msg = in.readObject(); // IOException
                if (msg == null) { // kalau client ngirim null brarti selesai
                    send(null);
                    break;
                } else if (msg instanceof Envelope) {
                    try {
                        handleEnvelope((Envelope) msg);
                    } catch (InterruptedException ex) {
                        System.out.println("> Error thread.");
                    }
                } else {
                    send(null);
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("> at Thread run: " + e.getMessage());
        }
        System.out.println("> " + name + " has closed.");
        program.broadcast(name + " has left.", "pesan");
        this.program.closeClient(this);
    }
}
