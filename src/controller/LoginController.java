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
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.SystemUser;

/**
 * FXML Controller class
 *
 * @author Ishadi Kariyawasam
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Label lblError;

    @FXML
    void OnClickedLogin(MouseEvent event) throws Exception {
        if (emptyTxtCheck()) {
            SystemUserDAO userDAO = new SystemUserDAO();
            systemUser = userDAO.loginByUsername(txtUsername.getText(), txtPassword.getText());
            if (systemUser != null) {
                Stage tmpStage = (Stage) btnLogin.getScene().getWindow();
                tmpStage.close();
                Stage stage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
                    stage.setTitle("Saloan Mgt!");
                    stage.setResizable(false);
                    stage.setScene(new Scene(root, 1190, 720));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                lblError.setText("Invalid username or password!");
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    public static SystemUser systemUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        systemUser = new SystemUser();

    }

    private boolean emptyTxtCheck() {
        if (txtUsername.getText().trim().isEmpty()) {
            lblError.setText("Insert username!");
            return false;
        } else {
            if (txtPassword.getText().trim().isEmpty()) {
                lblError.setText("Insert password!");
                return false;
            }
        }
        return true;
    }

}
