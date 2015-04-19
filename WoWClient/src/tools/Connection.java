/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import fxml.GameController;
import fxml.LoginController;
import fxml.Navigator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import objects.Envelope;
import objects.Scoreboard;
import objects.Soal;

/**
 *
 * @author sg
 */
public final class Connection implements Runnable {

    private String address;
    private int port;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isRunning;
    private GameController game;
    private LoginController login;

    public Connection() {
        socket = null;
        in = null;
        out = null;
        //connect("localhost", 10110);
    }

    public boolean isConnected() {
        return !(socket == null);
    }

    /**
     * Connect to given address and port. Default port is 10110.
     *
     * @param address
     * @param port
     */
    public void connect(String address, int port) {
        this.address = address;
        this.port = port;
        try {
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            isRunning = true;
            System.out.println("Connection established.");
            startThread();
        } catch (IOException ex) {
            System.out.println("Can't connect to server.");
            isRunning = false;
        }
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    public void setLogin(LoginController login) {
        this.login = login;
    }

    /**
     * Send envelope to server.
     *
     * @param envelope
     */
    public void send(Envelope envelope) {
        try {
            out.writeObject(envelope);
            System.out.println("Sending " + envelope);
        } catch (NullPointerException | IOException ex) {
            System.out.println("Error sending message.");
            isRunning = false;
        }
    }

    /**
     * Send envelope to server with given message and command.
     *
     * @param message
     * @param desc
     */
    public void send(String message, String desc) {
        send(new Envelope(message, desc));
    }

    /**
     * Send envelope to server. Default command is "pesan".
     *
     * @param message
     */
    public void send(String message) {
        send(message, "pesan");
    }

    private Object recv() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error receiving message.");
            isRunning = false;
        }
        return null;
    }

    public void close() {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException ex) {
            MessageBox.show("Error closing socket: " + ex.getMessage());
            isRunning = false;
        }
    }

    public void stopThread() {
        send("exit", "exit");
        isRunning = false;
    }

    public void startThread() {
        isRunning = true;
        new Thread(this).start();
    }

    private void handleEnvelope(Envelope env) {
        String message = env.getMessage();
        String command = env.getDesc();
        switch (command) {
            case "sout":
                System.out.println(message);
                break;
            case "login":
                String status;
                switch (message) {
                    case "success":
                        status = "Login success. Please wait.";
                        login.setLogin(true);
                        Object object = Navigator.load(Navigator.GAME);
                        setGame((GameController) object);
                        game.setConnection(this);
                        send(new Envelope("init", "minta"));
                        break;
                    default:
                        status = message;
                        break;
                }
                login.setLoginStatus(status);
                break;
            case "pesan":
                if (game != null) {
                    game.appendToChat(message + "\n");
                }
                break;
            case "user":
                if (game != null) {
                    game.setUser(message);
                }
                break;
            case "waktu":
                if (game != null) {
                    game.setWaktu(message);
                }
                break;
            case "score":
                if (game != null) {
                    game.setScore(message);
                }
                break;
            case "sisa":
                if (game != null) {
                    game.setSisa(message);
                }
                break;
        }
    }

    private void handleSoal(Soal soal) {
        if (game != null) {
            game.fillText(soal.getSoal(), soal.getOrdo());
            game.setKategori(soal.getKategori());
        }
    }

    private void handleScoreboard(Scoreboard msg) {
        if (game != null) {
            game.setScoreboard(msg.getData());
        }
    }

    @Override
    public void run() {
        System.out.println("Thread started.");
        while (isRunning) {
            Object msg = recv();
            System.out.println("Receiving " + msg);
            if (msg == null) {
                System.out.println("Why nulll??");
            } else if (msg instanceof Envelope) {
                handleEnvelope((Envelope) msg);
            } else if (msg instanceof Soal) {
                handleSoal((Soal) msg);
            } else if (msg instanceof Scoreboard) {
                handleScoreboard((Scoreboard) msg);
            }
        }
        System.out.println("Thread closed.");
    }
}
