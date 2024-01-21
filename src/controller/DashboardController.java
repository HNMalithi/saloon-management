/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.SystemUser;

/**
 * FXML Controller class
 *
 * @author User
 */
public class DashboardController implements Initializable {

    @FXML
    private Label lblFirstName;

    @FXML
    private JFXButton btnNewUser;

    @FXML
    private Label lblUserType;

    @FXML
    private Label lblTime;

    @FXML
    private AnchorPane btnHome;

    @FXML
    private AnchorPane btnSellsStock;

    @FXML
    private AnchorPane btnRent;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private AnchorPane pnlMiddle;

    @FXML
    private AnchorPane btnEmployees;

    @FXML
    private AnchorPane btnReports;

    @FXML
    void OnClickedNewUser(MouseEvent event) {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AddUser.fxml"));
            stage.setTitle("Add New User!");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickedLogout(MouseEvent event) {
        Stage tmpStage = (Stage) btnLogout.getScene().getWindow();
        tmpStage.close();

        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            stage.setTitle("Saloan Mgt!");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean admin = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadHome();

        setNavBtn_MouseEnterExitColors();
        setNavBtn_MouseClickedColor();

        setTimeToLbl();

        LoadUserInfo();
    }

    @FXML
    void OnClickedHome(MouseEvent event) {

        callColor = 1;
        setNavBtn_MouseClickedColor();

        this.pnlMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/Home.fxml"));
            this.pnlMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void OnClickedSellsStock(MouseEvent event) {

        if (admin) {
            callColor = 4;
            setNavBtn_MouseClickedColor();
            this.pnlMiddle.getChildren().clear();
            try {
                AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/SallesStock.fxml"));
                this.pnlMiddle.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "You can not access this!", ButtonType.OK);
            a.show();
        }

    }

    @FXML
    void OnCilckRent(MouseEvent event) {
        if (admin) {
            callColor = 5;
            setNavBtn_MouseClickedColor();

            this.pnlMiddle.getChildren().clear();
            try {
                AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/Rent.fxml"));
                this.pnlMiddle.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "You can not access this!", ButtonType.OK);
            a.show();
        }

    }

    @FXML
    void OnClickedReports(MouseEvent event) {
        if (admin) {
            callColor = 8;
            setNavBtn_MouseClickedColor();

            this.pnlMiddle.getChildren().clear();
            try {
                AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/Reports.fxml"));
                this.pnlMiddle.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "You can not access this!", ButtonType.OK);
            a.show();
        }

    }

    @FXML
    void OnClickEmployees(MouseEvent event) {
        if (admin) {
            callColor = 7;
            setNavBtn_MouseClickedColor();

            this.pnlMiddle.getChildren().clear();
            try {
                AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/Packages.fxml"));
                this.pnlMiddle.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "You can not access this!", ButtonType.OK);
            a.show();
        }

    }

    private void LoadHome() {
        this.pnlMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/Home.fxml"));
            this.pnlMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int callColor = 1;

    private String mouseClickColor = "-fx-background-color:  #b30059;";
    private String mouseEnterColor = "-fx-background-color:  #800040;";
    private String mouseExitColor = "-fx-background-color:  #660033;";

    private void setNavBtn_MouseEnterExitColors() {

        btnHome.setOnMouseEntered(e -> btnHome.setStyle(mouseEnterColor));
        btnSellsStock.setOnMouseEntered(e -> btnSellsStock.setStyle(mouseEnterColor));
        btnRent.setOnMouseEntered(e -> btnRent.setStyle(mouseEnterColor));
        btnEmployees.setOnMouseEntered(e -> btnEmployees.setStyle(mouseEnterColor));
        btnReports.setOnMouseEntered(e -> btnReports.setStyle(mouseEnterColor));

        btnHome.setOnMouseExited(e -> btnHome.setStyle(mouseExitColor));
        btnSellsStock.setOnMouseExited(e -> btnSellsStock.setStyle(mouseExitColor));
        btnRent.setOnMouseExited(e -> btnRent.setStyle(mouseExitColor));
        btnEmployees.setOnMouseExited(e -> btnEmployees.setStyle(mouseExitColor));
        btnReports.setOnMouseExited(e -> btnReports.setStyle(mouseExitColor));

    }

    private void setNavBtn_MouseClickedColor() {

        switch (callColor) {
            case 1:
                setNavBtn_DefaultColor();
                setNavBtn_MouseEnterExitColors();
                btnHome.setStyle(mouseClickColor);
                btnHome.setOnMouseExited(e -> btnHome.setStyle(mouseClickColor));
                btnHome.setOnMouseEntered(e -> btnHome.setStyle(mouseClickColor));
                break;
            case 4:
                setNavBtn_DefaultColor();
                setNavBtn_MouseEnterExitColors();
                btnSellsStock.setStyle(mouseClickColor);
                btnSellsStock.setOnMouseExited(e -> btnSellsStock.setStyle(mouseClickColor));
                btnSellsStock.setOnMouseEntered(e -> btnSellsStock.setStyle(mouseClickColor));
                break;
            case 5:
                setNavBtn_DefaultColor();
                setNavBtn_MouseEnterExitColors();
                btnRent.setStyle(mouseClickColor);
                btnRent.setOnMouseExited(e -> btnRent.setStyle(mouseClickColor));
                btnRent.setOnMouseEntered(e -> btnRent.setStyle(mouseClickColor));
                break;
            case 7:
                setNavBtn_DefaultColor();
                setNavBtn_MouseEnterExitColors();
                btnEmployees.setStyle(mouseClickColor);
                btnEmployees.setOnMouseExited(e -> btnEmployees.setStyle(mouseClickColor));
                btnEmployees.setOnMouseEntered(e -> btnEmployees.setStyle(mouseClickColor));
                break;
            case 8:
                setNavBtn_DefaultColor();
                setNavBtn_MouseEnterExitColors();
                btnReports.setStyle(mouseClickColor);
                btnReports.setOnMouseExited(e -> btnReports.setStyle(mouseClickColor));
                btnReports.setOnMouseEntered(e -> btnReports.setStyle(mouseClickColor));
                break;
        }
    }

    private void setNavBtn_DefaultColor() {
        btnHome.setStyle(mouseExitColor);
        btnSellsStock.setStyle(mouseExitColor);
        btnRent.setStyle(mouseExitColor);
        btnEmployees.setStyle(mouseExitColor);
        btnReports.setStyle(mouseExitColor);
    }

    private void setTimeToLbl() {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            lblTime.setText(LocalDateTime.now().format(timeFormatter));

        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private SystemUser user;

    private void LoadUserInfo() {
        user = LoginController.systemUser;
        lblFirstName.setText(user.getFirstName());
        lblUserType.setText(user.getUserPost());
        if (user.getUserPost().equals("Admin")) {
            admin = true;
        } else {
            btnNewUser.setDisable(true);
        }

    }

}
