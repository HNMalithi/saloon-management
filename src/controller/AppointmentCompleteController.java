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
import dao.AppoinmentDAO;
import dto.AppointmentDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AppointmentCompleteController implements Initializable {

    @FXML
    private TextField txtTotal;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblContact;

    @FXML
    private Label lblAdvance;

    @FXML
    private Label lblAppointmentDate;

    @FXML
    private TextField txtCash;

    @FXML
    private JFXButton btnDone;

    @FXML
    void OnClickedDone(MouseEvent event) throws Exception {
        if (emptyTxtCheck()) {
            if (!txtTotal.getText().matches("[0-9]*")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Total!", ButtonType.OK);
                a.show();
                txtTotal.selectAll();
                txtTotal.requestFocus();
            }
            if (!txtCash.getText().matches("[0-9]*")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Cash!", ButtonType.OK);
                a.show();
                txtCash.selectAll();
                txtCash.requestFocus();
            }
            if (txtTotal.getText().matches("[0-9]*") && txtCash.getText().matches("[0-9]*")) {
                AppoinmentDAO dAO = new AppoinmentDAO();
                boolean result = dAO.updateAppoinmentStatusAndAmount(dto.getAppoinmentNo(), (dto.getAmount() + Double.parseDouble(txtTotal.getText())));
                if (result) {
                    double change = Double.parseDouble(txtCash.getText()) - Double.parseDouble(txtTotal.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Change : Rs." + change + "\nThank You!", ButtonType.OK);
                    alert.showAndWait();
                    Stage tmpStage = (Stage) btnDone.getScene().getWindow();
                    tmpStage.close();
                }
            }
        }
    }

    @FXML
    void ValidateCash(ActionEvent event) {
        String txt = txtCash.getText();
        if (!txt.matches("[0-9]*")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Payment!", ButtonType.OK);
            a.show();
            txtCash.selectAll();
            txtCash.requestFocus();
        } else {
            btnDone.requestFocus();
        }
    }

    @FXML
    void ValidateTotal(ActionEvent event) {
        String txt = txtTotal.getText();
        if (!txt.matches("[0-9]*")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Payment!", ButtonType.OK);
            a.show();
            txtTotal.selectAll();
            txtTotal.requestFocus();
        } else {
            txtCash.requestFocus();
        }
    }

    /**
     * Initializes the controller class.
     */
    private AppointmentDTO dto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dto = HomeAppoinmentController.appoinmentDTO;
        lblAppointmentDate.setText(dto.getAppoinmentDate());
        lblCustomerName.setText(dto.getCustomerName());
        lblContact.setText(dto.getContactNo());
        lblAdvance.setText("Advance : Rs." + dto.getAmount());

    }

    private boolean emptyTxtCheck() {
        if (txtTotal.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert total amount!", ButtonType.OK);
            a.show();
            return false;
        } else {
            if (txtCash.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Please insert cash!", ButtonType.OK);
                a.show();
                return false;
            }
            return true;
        }
    }

}
