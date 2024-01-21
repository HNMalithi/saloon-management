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
import dao.PackagesDAO;
import dao.SoldPackagesDAO;
import db.DBConnection;
import generator.IDGenerator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Customer;
import model.Packages;
import model.SoldPackages;

/**
 * FXML Controller class
 *
 * @author Ishadi Kariyawasam
 */
public class HomePackagesController implements Initializable {

    @FXML
    private TextField txtCustPrice;

    @FXML
    private JFXButton btnDone;

    @FXML
    private TableView<Packages> tblOrder;

    @FXML
    private Label lblPackageTitle;

    @FXML
    private Label lblPackageInfo;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtCustomerMobile;

    @FXML
    void OnClickedDone(MouseEvent event) throws ClassNotFoundException, SQLException, Exception {
        if (isSelectedRow) {
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
                    Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Payment!", ButtonType.OK);
                    a.show();
                    txtCustPrice.selectAll();
                    txtCustPrice.requestFocus();
                }
                if (txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")&& (!(Double.parseDouble(txtCustPrice.getText()) >= packagePrice))) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "AAA!", ButtonType.OK);
                    a.show();
                    txtCustPrice.selectAll();
                    txtCustPrice.requestFocus();
                }
                if (txtCustomerName.getText().matches("[A-Za-z .]+") && txtCustomerMobile.getText().matches("[0-9]{10}") && txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && Double.parseDouble(txtCustPrice.getText()) >= packagePrice) {

                    Customer c = new Customer(customerId, txtCustomerName.getText(), txtCustomerMobile.getText(), curDate);
                    boolean addCustomer;
                    boolean saveOrder;

                    Connection connection = DBConnection.getInstance().getConnection();
                    connection.setAutoCommit(false);

                    if (hasACustomer) {
                        try {
                            saveOrder = packageOrderDAO.addSellPackage(new SoldPackages(soldPackageNo, packageId, customerId, packagePrice, curDate));
                            if (!saveOrder) {
                                connection.rollback();
                            }
                        } finally {
                            connection.setAutoCommit(true);
                        }
                        double change = Double.parseDouble(txtCustPrice.getText()) - packagePrice;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Change : Rs." + change + "\nThank You!", ButtonType.OK);
                        alert.showAndWait();
                        ClearAllTxt();

                    } else {
                        try {
                            addCustomer = customerDAO.addCustomer(c);
                            if (!addCustomer) {
                                connection.rollback();
                            }
                            saveOrder = packageOrderDAO.addSellPackage(new SoldPackages(soldPackageNo, packageId, customerId, packagePrice, curDate));
                            if (!saveOrder) {
                                connection.rollback();
                            }
                        } finally {
                            connection.setAutoCommit(true);
                        }
                        double change = Double.parseDouble(txtCustPrice.getText()) - packagePrice;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Change : Rs." + change + "\nThank You!", ButtonType.OK);
                        alert.showAndWait();
                        ClearAllTxt();
                    }
                }
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please Select a Package!", ButtonType.OK);
            a.show();
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
    void ValidatePrice(ActionEvent event) {
        if (!txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Payment!", ButtonType.OK);
            a.show();
            txtCustPrice.selectAll();
            txtCustPrice.requestFocus();
        }
    }

    private CustomerDAO customerDAO = new CustomerDAO();
    private PackagesDAO packagesDAO = new PackagesDAO();
    private SoldPackagesDAO packageOrderDAO = new SoldPackagesDAO();
    private List<Packages> selectedItemList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadPackagesTbl();
        LoadCustomerId();
        LoadSoldPackagesId();
        LoadCurrentDate();

        LoadOrderTblDetails();
    }

    private boolean isSelectedRow = false;
    private String packageId;
    private double packagePrice;

    private void LoadOrderTblDetails() {
        tblOrder.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selectedItemList.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Table is Empty!", ButtonType.OK);
                    a.show();
                } else {
                    Packages packages = tblOrder.getItems().get(tblOrder.getSelectionModel().getFocusedIndex());
                    packageId = packages.getPackageID();
                    packagePrice = packages.getPackagePrice();
                    lblPackageTitle.setText(packages.getPackageName());
                    lblPackageInfo.setText(packages.getPackageName() + "\n\nPackage Price : Rs." + packages.getPackagePrice());
                    isSelectedRow = true;
                }
            }
        });
    }

    private void LoadPackagesTbl() {
        tblOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("packageID"));
        tblOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("packageName"));
        try {
            tblOrder.setItems(FXCollections.observableArrayList(packagesDAO.getAllPackages()));
            selectedItemList = packagesDAO.getAllPackages();
        } catch (Exception ex) {
            Logger.getLogger(HomePackagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String customerId;

    private void LoadCustomerId() {
        try {
            String id = IDGenerator.getNewID("customer", "custId", "C");
            customerId = id;
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(HomeSellController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String soldPackageNo;

    private void LoadSoldPackagesId() {
        try {
            String id = IDGenerator.getNewID("soldPackages", "soldPackageNo", "S");
            soldPackageNo = id;
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(HomeSellController.class.getName()).log(Level.SEVERE, null, ex);
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
                if (txtCustPrice.getText().trim().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Please insert cash!", ButtonType.OK);
                    a.show();
                    return false;
                }
            }
        }
        return true;
    }

    private void ClearAllTxt() {

        LoadCustomerId();
        LoadSoldPackagesId();
        LoadPackagesTbl();

        txtCustomerName.setText("");
        txtCustomerMobile.setText("");
        txtCustPrice.setText("");

        lblPackageInfo.setText("");
        lblPackageTitle.setText("Select A Package From Table");

        customerDAO = new CustomerDAO();
        packagesDAO = new PackagesDAO();
        packageOrderDAO = new SoldPackagesDAO();
        selectedItemList = new ArrayList<>();

    }

}
