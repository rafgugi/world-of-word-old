/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author sg
 */
public class WoWClient extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Program program = new Program(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("blablabala");
        launch(args);
    }
}
