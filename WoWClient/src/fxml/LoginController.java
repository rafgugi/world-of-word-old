/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import objects.Envelope;

/**
 * FXML Controller class
 *
 * @author sg
 */
public class LoginController extends Controller implements Initializable {

    private boolean isLogin;

    @FXML
    private TextField tbUser;
    @FXML
    private PasswordField tbPass;
    @FXML
    private TextField tbPort;
    @FXML
    private TextField tbAddress;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblPass;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initializing login controller.");
        lblStatus.setText("");
        tbPass.setText("asdasd");
        lblPass.setVisible(false);
        tbPass.setVisible(false);
    }

    public void setLoginStatus(final String status) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblStatus.setText(status);
            }
        });
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    private boolean validate() {
        return false;
    }

    @FXML
    private void login(ActionEvent event) {
        String address = tbAddress.getText();
        int port = Integer.parseInt(tbPort.getText());
        if (!getConnection().isConnected()) {
            getConnection().connect(address, port);
        }
        if (getConnection().isConnected()) {
            String message = tbUser.getText() + ":" + tbPass.getText();
            getConnection().send(new Envelope(message, "login"));
        } else {
            setLoginStatus("Can't connect server.");
        }
    }

}
