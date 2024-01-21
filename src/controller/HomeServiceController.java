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
import com.jfoenix.controls.JFXTextField;
import dao.CustomerDAO;
import dao.ServiceDAO;
import db.DBConnection;
import generator.IDGenerator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Customer;
import model.Service;

/**
 * FXML Controller class
 *
 * @author Ishadi Kariyawasam
 */
public class HomeServiceController implements Initializable {

    @FXML
    private TextField txtTotal;

    @FXML
    private TextField txtCustPrice;

    @FXML
    private JFXButton btnDone;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtCustomerMobile;

    @FXML
    private TextField txtServiceDescription;

    @FXML
    void OnClickedDone(MouseEvent event) throws ClassNotFoundException, SQLException, Exception{

        if (emptyTxtCheck()) {
            if (!txtCustomerName.getText().matches("[A-Za-z .]+")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Name!", ButtonType.OK);
                a.show();
                txtCustomerName.selectAll();
                txtCustomerName.requestFocus();
            }
            if (!txtCustomerMobile.getText().matches("[0-9]{10}")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Mobile!", ButtonType.OK);
                a.show();
                txtCustomerMobile.selectAll();
                txtCustomerMobile.requestFocus();
            }
            if (!txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Cash!", ButtonType.OK);
                a.show();
                txtCustPrice.selectAll();
                txtCustPrice.requestFocus();
            }
            if (!txtTotal.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Total!", ButtonType.OK);
                a.show();
                txtTotal.selectAll();
                txtTotal.requestFocus();
            }
            if (txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && (!(Double.parseDouble(txtCustPrice.getText()) >= Double.parseDouble(txtTotal.getText())))) {
                Alert a = new Alert(Alert.AlertType.WARNING, "AAA!", ButtonType.OK);
                a.show();
                txtCustPrice.selectAll();
                txtCustPrice.requestFocus();
            }

            if (txtCustomerName.getText().matches("[A-Za-z .]+") && txtCustomerMobile.getText().matches("[0-9]{10}") && txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && txtTotal.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && (Double.parseDouble(txtCustPrice.getText()) >= Double.parseDouble(txtTotal.getText()))) {
                Customer c = new Customer(customerId, txtCustomerName.getText(), txtCustomerMobile.getText(), curDate);
                boolean addCustomer;
                boolean saveService;

                Connection connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                
                if (hasACustomer) {
                    try {
                        saveService = serviceDAO.addService(new Service(serviceNo, customerId, txtServiceDescription.getText(), Double.parseDouble(txtTotal.getText()), curDate));
                        if (!saveService) {
                            connection.rollback();
                        }
                    } finally {
                        connection.setAutoCommit(true);
                    }
                    double change = Double.parseDouble(txtCustPrice.getText()) - Double.parseDouble(txtTotal.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Change : Rs." + change + "\nThank You!", ButtonType.OK);
                    alert.showAndWait();
                    ClearAllTxt();
                } else {
                    try {
                        addCustomer = customerDAO.addCustomer(c);
                        if (!addCustomer) {
                            connection.rollback();
                        }
                        saveService = serviceDAO.addService(new Service(serviceNo, customerId, txtServiceDescription.getText(), Double.parseDouble(txtTotal.getText()), curDate));
                        if (!saveService) {
                            connection.rollback();
                        }
                    } finally {
                        connection.setAutoCommit(true);
                    }
                    double change = Double.parseDouble(txtCustPrice.getText()) - Double.parseDouble(txtTotal.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Change : Rs." + change + "\nThank You!", ButtonType.OK);
                    alert.showAndWait();
                    ClearAllTxt();
                }
            }
        }
    }

    @FXML
    void ValidateCash(ActionEvent event) {
        if (!txtCustPrice.getText().matches("[0-9]*")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Cash!", ButtonType.OK);
            a.show();
            txtCustPrice.selectAll();
            txtCustPrice.requestFocus();
        }
    }

    @FXML
    void ValidateCustName(ActionEvent event) {
        String txt = txtCustomerName.getText();
        if (!txt.matches("[A-Za-z .]+")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Name!", ButtonType.OK);
            a.show();
            txtCustomerName.selectAll();
            txtCustomerName.requestFocus();
        } else {
            txtCustomerMobile.requestFocus();
        }
    }

    private boolean hasACustomer = false;

    @FXML
    void ValidateMobile(ActionEvent event) throws Exception {
        String txt = txtCustomerMobile.getText();
        if (!txt.matches("[0-9]{10}")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Mobile!", ButtonType.OK);
            a.show();
            txtCustomerMobile.selectAll();
            txtCustomerMobile.requestFocus();
        } else {
            Customer c = customerDAO.searchByMobile(txt);
            if (c != null) {
                txtCustomerName.setText(c.getCustName());
                customerId = c.getCustID();
                hasACustomer = true;
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Not found any previous record!", ButtonType.OK);
                a.show();
            }
        }
    }

    @FXML
    void ValidateServiceDescription(ActionEvent event) {
        txtTotal.requestFocus();
    }

    @FXML
    void ValidateTotal(ActionEvent event) {
        if (!txtTotal.getText().matches("[0-9]*")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Total!", ButtonType.OK);
            a.show();
            txtTotal.selectAll();
            txtTotal.requestFocus();
        } else {
            txtCustPrice.requestFocus();
        }
    }

    /**
     * Initializes the controller class.
     */
    private CustomerDAO customerDAO = new CustomerDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadCustomerId();
        LoadServiceId();
        LoadCurrentDate();
    }

    private String customerId;

    private void LoadCustomerId() {
        try {
            String id = IDGenerator.getNewID("customer", "custId", "C");
            customerId = id;
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(HomeServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String serviceNo;

    private void LoadServiceId() {
        try {
            String id = IDGenerator.getNewID("service", "serviceNo", "S");
            serviceNo = id;
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(HomeServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String curDate;

    private void LoadCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        curDate = dateFormat.format(date);
    }

    private boolean emptyTxtCheck() {
        if (txtCustomerName.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert customer name!", ButtonType.OK);
            a.show();
            return false;
        } else {
            if (txtCustomerMobile.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Please insert customer mobile number!", ButtonType.OK);
                a.show();
                return false;
            } else {
                if (txtServiceDescription.getText().trim().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Please insert service description!", ButtonType.OK);
                    a.show();
                    return false;
                } else {
                    if (txtTotal.getText().trim().isEmpty()) {
                        Alert a = new Alert(Alert.AlertType.WARNING, "Please insert total price!", ButtonType.OK);
                        a.show();
                        return false;
                    } else {
                        if (txtCustPrice.getText().trim().isEmpty()) {
                            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert cash!", ButtonType.OK);
                            a.show();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void ClearAllTxt() {

        LoadCustomerId();
        LoadServiceId();

        txtCustomerName.setText("");
        txtCustomerMobile.setText("");
        txtCustPrice.setText("");
        txtTotal.setText("");
        txtServiceDescription.setText("");

        customerDAO = new CustomerDAO();
        serviceDAO = new ServiceDAO();
    }
}
