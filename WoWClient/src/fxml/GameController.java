/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import objects.UserScore;

/**
 * FXML Controller class
 *
 * @author sg
 */
public class GameController extends Controller implements Initializable {

    private GraphicsContext gc;

    @FXML
    private Canvas kanfas;
    @FXML
    private Label lblKategori;
    @FXML
    private TextArea taJawaban;
    @FXML
    private TextField tbJawab;
    @FXML
    private Label lblWaktu;
    @FXML
    private Label lblScore;
    @FXML
    private Label lblAvailable;
    @FXML
    private Label lblUser;
    @FXML
    private ToggleButton btnLv3;
    @FXML
    private ToggleButton btnLv2;
    @FXML
    private ToggleButton btnLv1;
    @FXML
    private TableView<UserScore> tableScore;
    @FXML
    private TableColumn columnRank;
    @FXML
    private TableColumn columnUser;
    @FXML
    private TableColumn columnScore;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        taJawaban.setEditable(false);
        taJawaban.setFocusTraversable(false);
        
        tableScore.setEditable(false);

        gc = kanfas.getGraphicsContext2D();
        setKategori("");
        setScore("");
        setSisa("");
        setWaktu("");
        //clearKanfas();
    }

    public void clearKanfas() {
        //Platform.runLater(new Runnable() {
        //  @Override
        //  public void run() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, kanfas.getWidth(), kanfas.getHeight());
        gc.setFill(Color.BLACK);
        gc.strokeRect(0, 0, kanfas.getWidth(), kanfas.getHeight());
        // }
        //});
    }

    public void fillText(final String[][] huruf, final int ordo) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                double textW;
                double textH;
                double initX;
                double initY;
                double width;
                double heigh;
                Font myFont;

                clearKanfas();
                width = kanfas.getWidth();
                heigh = kanfas.getHeight();
                myFont = new Font("Courier New Bold", width / (ordo + 2));
                textW = myFont.getSize() + 1;
                textH = myFont.getSize() + 1;
                initX = (width - textW * ordo) / 2 + textW / 4;
                initY = (heigh - textH * ordo) / 2 + textH * 3 / 4;

                gc.setFont(myFont);

                for (int i = 0; i < ordo; i++) {
                    for (int j = 0; j < ordo; j++) {
                        String s = huruf[i][j].toUpperCase();
                        gc.fillText(s, initX + textW * j, initY + textH * i);
                    }
                }
            }
        });
    }

    private boolean jawab() {
        String s = tbJawab.getText();
        send(s);
        tbJawab.clear();
        return true;
    }

    public void send(String message) {
        getConnection().send(message);
    }

    public void appendToChat(final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                taJawaban.appendText(message);
            }
        });
    }

    public void setWaktu(final String waktu) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblWaktu.setText(waktu);
            }
        });
    }

    public void setUser(final String user) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblUser.setText(user);
            }
        });
    }

    public void setKategori(final String kategori) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblKategori.setText(kategori);
            }
        });
    }

    public void setScore(final String score) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblScore.setText(score);
            }
        });
    }

    public void setSisa(final String availableWords) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblAvailable.setText(availableWords);
            }
        });
    }

    public void setScoreboard(final ArrayList<UserScore> scoreboard) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<UserScore> data;
                data = FXCollections.observableArrayList(scoreboard);
                columnRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
                columnUser.setCellValueFactory(new PropertyValueFactory<>("user"));
                columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
                tableScore.setItems(data);
            }
        });
    }

    @FXML
    private void btnOnClick(MouseEvent event) {
        jawab();
        tbJawab.requestFocus();
    }

    @FXML
    private void tbOnPress(ActionEvent event) {
        jawab();
    }

    @FXML
    private void btnLv1Click(ActionEvent event) {
        tbJawab.requestFocus();
        btnLv1.setSelected(true);
        btnLv2.setSelected(false);
        btnLv3.setSelected(false);
        getConnection().send("10", "level");
    }

    @FXML
    private void btnLv2Click(ActionEvent event) {
        tbJawab.requestFocus();
        btnLv1.setSelected(false);
        btnLv2.setSelected(true);
        btnLv3.setSelected(false);
        getConnection().send("15", "level");
    }

    @FXML
    private void btnLv3Click(ActionEvent event) {
        tbJawab.requestFocus();
        btnLv1.setSelected(false);
        btnLv2.setSelected(false);
        btnLv3.setSelected(true);
        getConnection().send("20", "level");
    }

}
