/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author sg
 */
public class Navigator {

    public static final String MAIN = "Main.fxml";
    public static final String LOGIN = "Login.fxml";
    public static final String GAME = "Game.fxml";

    private static MainController mainController;

    /**
     * @param aMainController the mainController to set
     */
    public static void setMainController(MainController aMainController) {
        mainController = aMainController;
    }

    public static Object load(String fxml) {
        System.out.println("Loading " + fxml);
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(
                    FxmlDummy.class.getResource(fxml)
            );
            mainController.setMainpane((Node) loader.load());
        } catch (IOException e) {
            System.out.println("Failed in loading " + fxml);
            return null;
        }
        return loader.getController();
    }
}
