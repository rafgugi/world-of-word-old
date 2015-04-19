/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import fxml.FxmlDummy;
import fxml.GameController;
import fxml.LoginController;
import fxml.MainController;
import fxml.Navigator;
import java.io.IOException;
import java.net.URL;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.Connection;

/**
 *
 * @author sg
 */
public class Program {

    public Stage stage;
    public FXMLLoader loader;
    public GameController game;
    public LoginController login;
    public Connection connection;

    public Program(Stage stage) {
        this.stage = stage;
        loader = new FXMLLoader();
        game = null;
        connection = new Connection();

        initializeStage();
    }

    private void initializeStage() {
        stage.setTitle("WoW - World of Word!");
        stage.setScene(new Scene(loadMainPane()));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                connection.stopThread();
                connection.send("exit", "exit");
            }
        });
        stage.show();
    }

    /**
     * Loads the main fxml layout. Sets up the vista switching VistaNavigator.
     * Loads the first vista into the fxml layout.
     *
     * @return the loaded pane.
     */
    private AnchorPane loadMainPane() {
        loader = new FXMLLoader();

        AnchorPane mainPane = null;
        try {
            mainPane = (AnchorPane) loader.load(
                    FxmlDummy.class.getResourceAsStream(Navigator.MAIN)
            );
        } catch (IOException ex) {
            System.out.println("Error loading pane: " + ex.getMessage());
        }

        MainController mainController = loader.getController();

        Navigator.setMainController(mainController);
        Object o = Navigator.load(Navigator.LOGIN);

        login = (LoginController) o;
        connection.setLogin(login);
        login.setConnection(connection);
        System.out.println("Login is ready.");
        
        return mainPane;
    }

    public void setScene(String fxmlLocation) {
        try {
            URL location = FxmlDummy.class.getResource(fxmlLocation);
            Parent root;
            root = (Parent) loader.load(location.openStream());
            Scene scene;
            scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println("Error setting scene: " + ex.getMessage());
        }
    }
}
