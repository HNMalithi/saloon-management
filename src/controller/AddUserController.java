/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dao.SystemUserDAO;
import generator.IDGenerator;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.SystemUser;

/**
 * FXML Controller class
 *
 * @author Ishadi Kariyawasam
 */
public class AddUserController implements Initializable {

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtMobile;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private ComboBox<String> cmbUserType;

    @FXML
    private JFXButton btnDone;

    @FXML
    void OnClickedDone(MouseEvent event) throws Exception {

        if (emptyTxtCheck()) {
            SystemUser systemUser = new SystemUser(userId, txtFirstName.getText(), txtLastName.getText(), txtMobile.getText(),
                    txtUsername.getText(), txtPassword.getText(), userType, curDate);

            SystemUserDAO userDAO = new SystemUserDAO();
            boolean result = userDAO.addSystemUser(systemUser);
            if (result) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User Added!", ButtonType.OK);
                alert.showAndWait();
                Stage tmpStage = (Stage) btnDone.getScene().getWindow();
                tmpStage.close();
            }
        }
    }

    private String userType;

    @FXML
    void SelectUserType(ActionEvent event) {
        userType = cmbUserType.getSelectionModel().getSelectedItem();
    }

    @FXML
    void ValidateFirstName(ActionEvent event) {

    }

    @FXML
    void ValidateLastName(ActionEvent event) {

    }

    @FXML
    void ValidateMobile(ActionEvent event) {

    }

    @FXML
    void ValidateUsername(ActionEvent event) {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadUserTypesBmb();
        LoadUserId();
        LoadCurrentDate();
    }

    private String curDate;

    private void LoadCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        curDate = dateFormat.format(date);
    }

    private void LoadUserTypesBmb() {
        ObservableList<String> options = FXCollections.observableArrayList("Admin", "User");
        cmbUserType.setItems(options);
    }

    private boolean emptyTxtCheck() {
        if (txtFirstName.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert first name!", ButtonType.OK);
            a.show();
            return false;
        } else {
            if (txtLastName.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Please insert last name!", ButtonType.OK);
                a.show();
                return false;
            } else {
                if (txtMobile.getText().trim().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Please insert mobile number!", ButtonType.OK);
                    a.show();
                    return false;
                } else {
                    if (txtUsername.getText().trim().isEmpty()) {
                        Alert a = new Alert(Alert.AlertType.WARNING, "Please insert username!", ButtonType.OK);
                        a.show();
                        return false;
                    } else {
                        if (txtPassword.getText().trim().isEmpty()) {
                            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert password!", ButtonType.OK);
                            a.show();
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    private String userId;

    private void LoadUserId() {
        try {
            String id = IDGenerator.getNewID("systemUser", "userId", "U");
            userId = id;
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
