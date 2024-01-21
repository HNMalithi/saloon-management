/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author User
 */
public class HomeController implements Initializable {
    
    @FXML
    private JFXButton btnSellHome;

    @FXML
    private JFXButton btnHomeRent;

    @FXML
    private JFXButton btnHomeAppoinment;

    @FXML
    private JFXButton btnPackages;
     
    @FXML
    private AnchorPane pnlHomeMiddle;
    
    @FXML
    private JFXButton btnAllRents;
    
    @FXML
    private JFXButton btnServiceHome;

    private String onClickedColor = "-fx-background-color: #b30059";
    private String defaultColor = "-fx-background-color: #660033";
    
    @FXML
    void OnClickedHomeAppoinmentBtn(MouseEvent event) {

        btnHomeAppoinment.setStyle(onClickedColor);
        btnHomeRent.setStyle(defaultColor);
        btnSellHome.setStyle(defaultColor);
        btnAllRents.setStyle(defaultColor);
        btnPackages.setStyle(defaultColor);
        btnServiceHome.setStyle(defaultColor);
        
        this.pnlHomeMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/HomeAppoinment.fxml"));
            this.pnlHomeMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     @FXML
    void OnClickedHomeServiceBtn(MouseEvent event) {
        btnServiceHome.setStyle(onClickedColor);
        btnHomeAppoinment.setStyle(defaultColor);
        btnHomeRent.setStyle(defaultColor);
        btnSellHome.setStyle(defaultColor);
        btnAllRents.setStyle(defaultColor);
        btnPackages.setStyle(defaultColor);

        this.pnlHomeMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/HomeService.fxml"));
            this.pnlHomeMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void OnClickedHomeRentBtn(MouseEvent event) {
        
        btnHomeRent.setStyle(onClickedColor);
        btnHomeAppoinment.setStyle(defaultColor);
        btnSellHome.setStyle(defaultColor);
        btnAllRents.setStyle(defaultColor);
        btnPackages.setStyle(defaultColor);
        btnServiceHome.setStyle(defaultColor);
        
        this.pnlHomeMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/HomeRent.fxml"));
            this.pnlHomeMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickedHomeSellBtn(MouseEvent event) {
        
        btnSellHome.setStyle(onClickedColor);
        btnHomeRent.setStyle(defaultColor);
        btnHomeAppoinment.setStyle(defaultColor);
        btnAllRents.setStyle(defaultColor);
        btnPackages.setStyle(defaultColor);
        btnServiceHome.setStyle(defaultColor);
        
        this.pnlHomeMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/HomeSell.fxml"));
            this.pnlHomeMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickedHomeRentAllBtn(MouseEvent event) {
        
        btnAllRents.setStyle(onClickedColor);
        btnSellHome.setStyle(defaultColor);
        btnHomeRent.setStyle(defaultColor);
        btnHomeAppoinment.setStyle(defaultColor);
        btnPackages.setStyle(defaultColor);
        btnServiceHome.setStyle(defaultColor);
        
        this.pnlHomeMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/HomeRentLast.fxml"));
            this.pnlHomeMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @FXML
    void OnClickedPackagesBtn(MouseEvent event) {
        btnPackages.setStyle(onClickedColor);
        btnAllRents.setStyle(defaultColor);
        btnSellHome.setStyle(defaultColor);
        btnHomeRent.setStyle(defaultColor);
        btnHomeAppoinment.setStyle(defaultColor);
        btnServiceHome.setStyle(defaultColor);
        
        this.pnlHomeMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/HomePackages.fxml"));
            this.pnlHomeMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadDefaultHome();
    }    

    private void LoadDefaultHome() {
        btnServiceHome.setStyle(onClickedColor);
        btnHomeAppoinment.setStyle(defaultColor);
        btnHomeRent.setStyle(defaultColor);
        btnSellHome.setStyle(defaultColor);
        btnAllRents.setStyle(defaultColor);
        btnPackages.setStyle(defaultColor);
        
        this.pnlHomeMiddle.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(this.getClass().getResource("/view/HomeService.fxml"));
            this.pnlHomeMiddle.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      
    
}
