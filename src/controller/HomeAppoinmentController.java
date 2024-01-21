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
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import dao.AppoinmentDAO;
import dto.AppointmentDTO;
import dao.CustomerDAO;
import db.DBConnection;
import generator.IDGenerator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appoinment;
import model.Customer;

/**
 * FXML Controller class
 *
 * @author User
 */
public class HomeAppoinmentController implements Initializable {

    @FXML
    private TableView<AppointmentDTO> tblUpcomingAppointments;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private TextField txtAdvancePayment;

    @FXML
    private JFXDatePicker txtAppointmentDate;

    @FXML
    private JFXTimePicker txtAppointmentTime;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtCustomerMobile;

    @FXML
    private JFXButton btnComplete;

    private CustomerDAO customerDAO = new CustomerDAO();
    private AppoinmentDAO appoinmentDAO = new AppoinmentDAO();

    @FXML
    void OnClickedAdd(MouseEvent event) throws Exception {
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
            if (!txtAdvancePayment.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Payment!", ButtonType.OK);
                a.show();
                txtAdvancePayment.selectAll();
                txtAdvancePayment.requestFocus();
            }

            if (txtCustomerName.getText().matches("[A-Za-z .]+") && txtCustomerMobile.getText().matches("[0-9]{10}") && txtAdvancePayment.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                String apDate = txtAppointmentDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String apTime = txtAppointmentTime.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));
                double amount = Double.parseDouble(txtAdvancePayment.getText());

                Customer c = new Customer(custId, txtCustomerName.getText(), txtCustomerMobile.getText(), curDate);
                Appoinment a = new Appoinment(appointmentNo, custId, txtDescription.getText(), apDate, apTime, "waiting", amount, curDate);

                Connection connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                boolean addCustomer;
                boolean addAppointment;
                if (hasACustomer) {
                    try {
                        addAppointment = appoinmentDAO.addAppoinment(a);
                        if (!addAppointment) {
                            connection.rollback();
                        }
                        connection.commit();
                    } finally {
                        connection.setAutoCommit(true);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment is Added!", ButtonType.OK);
                    alert.showAndWait();
                    ClearTxts();
                    hasACustomer = false;

                } else {
                    try {
                        addCustomer = customerDAO.addCustomer(c);
                        if (!addCustomer) {
                            connection.rollback();
                        }
                        addAppointment = appoinmentDAO.addAppoinment(a);
                        if (!addAppointment) {
                            connection.rollback();
                        }
                        connection.commit();
                    } finally {
                        connection.setAutoCommit(true);
                    }
                    if (addCustomer && addAppointment) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment is Added!", ButtonType.OK);
                        alert.showAndWait();
                        ClearTxts();
                    }
                }
            }
        }

    }

    @FXML
    void OnClickedDelete(MouseEvent event) {
        if (isRowSelected) {
            try {
                boolean result = appoinmentDAO.cancelAppoinment(appoinmentDTO.getAppoinmentNo());
                if (result) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment is Canceled!", ButtonType.OK);
                    alert.showAndWait();
                    LoadUpcomingAppointments();
                    isRowSelected = false;
                }
            } catch (Exception ex) {
                Logger.getLogger(HomeAppoinmentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please select a table row!", ButtonType.OK);
            a.show();
        }
    }

    @FXML
    void OnClickedComplete(MouseEvent event) {
        if (upcomingAppointmentList.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Empty Table!", ButtonType.OK);
            a.show();
        } else {
            if (isRowSelected) {
                Stage stage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentComplete.fxml"));
                    stage.setTitle("Appointment Complete!");
                    stage.setResizable(false);
                    stage.setScene(new Scene(root));
                    stage.show();
                    isRowSelected = false;
                } catch (IOException ex) {
                    Logger.getLogger(HomeAppoinmentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Please select a table row!", ButtonType.OK);
                a.show();
            }
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadCustomerId();
        LoadCurrentDate();
        LoadAppointmentNo();
        getTblUpcomingAppointmentsValues();

        LoadUpcomingAppointments();
    }

    private String curDate;

    private void LoadCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        curDate = dateFormat.format(date);
    }

    private String custId;

    private void LoadCustomerId() {
        try {
            String id = IDGenerator.getNewID("customer", "custId", "C");
            custId = id;
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(HomeSellController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String appointmentNo;

    private void LoadAppointmentNo() {
        try {
            String id = IDGenerator.getNewID("appoinment", "appoinmentNo", "A");
            appointmentNo = id;
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(HomeSellController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ClearTxts() {
        txtCustomerName.setText("");
        txtCustomerMobile.setText("");
        txtAdvancePayment.setText("");
        txtDescription.setText("");
        txtAppointmentDate.setValue(null);
        txtAppointmentTime.setValue(null);
        LoadCustomerId();
        LoadAppointmentNo();
        LoadUpcomingAppointments();
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
                if (txtDescription.getText().trim().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Please insert description!", ButtonType.OK);
                    a.show();
                    return false;
                } else {
                    if (txtAppointmentDate.getValue() == null) {
                        Alert a = new Alert(Alert.AlertType.WARNING, "Please insert appointment date!", ButtonType.OK);
                        a.show();
                        return false;
                    } else {
                        if (txtAppointmentTime.getValue() == null) {
                            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert appointment time!", ButtonType.OK);
                            a.show();
                            return false;
                        } else {
                            if (txtAdvancePayment.getText().trim().isEmpty()) {
                                Alert a = new Alert(Alert.AlertType.WARNING, "Please insert advance payment!", ButtonType.OK);
                                a.show();
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @FXML
    void ValidateAdvancePayment(ActionEvent event) {
        String txt = txtAdvancePayment.getText();
        if (!txt.matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Payment!", ButtonType.OK);
            a.show();
            txtAdvancePayment.selectAll();
            txtAdvancePayment.requestFocus();
        } else {
            btnAdd.requestFocus();
        }
    }

    @FXML
    void ValidateAppointmentDate(ActionEvent event) {
        txtAppointmentTime.requestFocus();
    }

    @FXML
    void ValidateAppointmentTime(ActionEvent event) {
        txtAdvancePayment.requestFocus();
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

    @FXML
    void ValidateDescription(ActionEvent event) {
        if (txtDescription.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Description!", ButtonType.OK);
            a.show();
            txtCustomerName.requestFocus();
        } else {
            txtAppointmentDate.requestFocus();
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
                custId = c.getCustID();
                hasACustomer = true;
                txtDescription.requestFocus();
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Not found any previous record!", ButtonType.OK);
                a.show();
            }
        }
    }

    private boolean isRowSelected = false;
    public static AppointmentDTO appoinmentDTO;

    private void getTblUpcomingAppointmentsValues() {
        tblUpcomingAppointments.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (upcomingAppointmentList.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Empty table!", ButtonType.OK);
                    a.show();
                } else {
                    appoinmentDTO = tblUpcomingAppointments.getItems().get(tblUpcomingAppointments.getSelectionModel().getFocusedIndex());
                    isRowSelected = true;
                }
            }
        });
    }

    private List<AppointmentDTO> upcomingAppointmentList = new ArrayList<>();
        
    private void LoadUpcomingAppointments() {

        tblUpcomingAppointments.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("appoinmentDate"));
        tblUpcomingAppointments.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("appoinmentTime"));
        tblUpcomingAppointments.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblUpcomingAppointments.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblUpcomingAppointments.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        try {
            tblUpcomingAppointments.setItems(FXCollections.observableArrayList(appoinmentDAO.getAllAppoinment()));
            upcomingAppointmentList = appoinmentDAO.getAllAppoinment();
        } catch (Exception ex) {
            Logger.getLogger(HomeAppoinmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
