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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.Envelope;
import objects.Soal;

/**
 *
 * @author sg
 */
public class Receiver implements Runnable {
    private boolean isRunning;
    private GameController game;
    private LoginController login;
    private static ObjectInputStream in;

    public Receiver(Socket socket) {
        in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void handleEnvelope(Envelope env) {
        String message = env.getMessage();
        String command = env.getDesc();
        switch (command) {
            case "sout":
                System.out.println(message);
                break;
            case "login":
                String status = "";
                switch (message) {
                    case "success":
                        status = "Login success. Please wait.";
                        login.setLogin(true);
                        Navigator.load(Navigator.GAME);
                        break;
                    case "wrong":
                        status = "Login failed.";
                        break;
                }
                login.setLoginStatus(status);
                break;
            case "pesan":
                game.appendToChat(message + "\n");
                break;
        }
    }

    private void handleSoal(Soal soal) {
        if (game != null) {
            game.fillText(soal.getSoal(), soal.getOrdo());
        }
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    public void setLogin(LoginController login) {
        this.login = login;
    }

    public Object recv() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error receiving message.");
        }
        return null;
    }

    @Override
    public void run() {
        System.out.println("Thread started.");
        while (isRunning) {
            Object msg = recv();
            System.out.println(msg);
            if (msg == null) {
            } else if (msg instanceof Envelope) {
                handleEnvelope((Envelope) msg);
            } else if (msg instanceof Soal) {
                handleSoal((Soal) msg);
            }
        }
        System.out.println("Thread closed.");
    }
    
}
