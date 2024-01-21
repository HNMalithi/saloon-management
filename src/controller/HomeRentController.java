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
import dao.CustomerDAO;
import dao.RentItemDAO;
import dao.RentItemDetailDAO;
import dao.RentItemOrderDAO;
import db.DBConnection;
import dto.RentItemDTO;
import generator.IDGenerator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Customer;
import model.RentItem;
import model.RentItemDetail;
import model.RentItemOrder;

/**
 * FXML Controller class
 *
 * @author User
 */
public class HomeRentController implements Initializable {

    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    private TextField txtDescriptin;

    @FXML
    private TextField txtRentPrice;

    @FXML
    private TextField txtQty;

    @FXML
    private TableView<RentItemDTO> tblItemList;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TextField txtTotal;

    @FXML
    private TextField txtCustPrice;

    @FXML
    private JFXButton btnDone;

    @FXML
    private JFXDatePicker txtBorrowDate;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtCustomerMobile;

    @FXML
    void OnClickedDelete(MouseEvent event) {
        if (selectedItemList.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Empty Table!", ButtonType.OK);
            a.show();
        } else {
            if (isSelectedRow) {
                for (int i = 0; i < selectedItemList.size(); i++) {
                    if (selectedItemList.get(i).getItemCode().equals(itemCode)) {
                        subTotal = subTotal - selectedItemList.get(i).getAmount();
                        txtTotal.setText(formatter.format(subTotal));
                        selectedItemList.remove(i);
                        LoadTbl();
                        isSelectedRow = false;
                    }
                }
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Select a row!", ButtonType.OK);
                a.show();
            }
        }
    }

    @FXML
    void OnClickedDone(MouseEvent event) throws Exception {
        
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
            if (txtCustomerName.getText().matches("[A-Za-z .]+") && txtCustomerMobile.getText().matches("[0-9]{10}") && txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {

                String borrowDate = txtBorrowDate.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

                Customer c = new Customer(custId, txtCustomerName.getText(), txtCustomerMobile.getText(), curDate);
                boolean addCustomer;
                boolean saveOrder;
                boolean saveOrderDetail;
                boolean updateSellStock;

                Connection connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);

                if (hasACustomer) {
                    try {
                        saveOrder = rentItemOrderDAO.addOrder(new RentItemOrder(
                                orderId,
                                custId,
                                curDate,
                                Double.parseDouble(txtCustPrice.getText()),
                                Double.parseDouble(txtTotal.getText()),
                                curDate,
                                borrowDate,
                                "Rent"
                        ));
                        if (!saveOrder) {
                            connection.rollback();
                        }
                        for (RentItemDTO itemDto : selectedItemList) {
                            saveOrderDetail = rentItemDetailDAO.addRentItemOrderDetail(new RentItemDetail(orderId, itemDto.getItemCode(), itemDto.getQty(), itemDto.getUnitPrice()));
                            if (!saveOrderDetail) {
                                connection.rollback();
                            }
                            RentItem item = rentItemDAO.searchById(itemDto.getItemCode());
                            int qtyOnHand = item.getQty();
                            if (qtyOnHand < itemDto.getQty()) {
                                Alert a = new Alert(Alert.AlertType.WARNING, "Can not rent this Qty!", ButtonType.OK);
                                a.show();
                                txtQty.selectAll();
                                txtQty.requestFocus();
                            }
                            qtyOnHand = qtyOnHand - itemDto.getQty();
                            updateSellStock = rentItemDAO.updateItemQty(new RentItem(itemDto.getItemCode(), qtyOnHand));
                            if (!updateSellStock) {
                                connection.rollback();
                            }
                        }
                    } finally {
                        connection.setAutoCommit(true);
                    }
                    double change = Double.parseDouble(txtTotal.getText()) - Double.parseDouble(txtCustPrice.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Change : Rs." + change + "\nThank You!", ButtonType.OK);
                    alert.showAndWait();
                    ClearAllTxt();
                } else {
                    try {
                        addCustomer = customerDAO.addCustomer(c);
                        if (!addCustomer) {
                            connection.rollback();
                        }
                        saveOrder = rentItemOrderDAO.addOrder(new RentItemOrder(
                                orderId,
                                custId,
                                curDate,
                                Double.parseDouble(txtCustPrice.getText()),
                                Double.parseDouble(txtTotal.getText()),
                                curDate,
                                borrowDate,
                                "Rent"
                        ));
                        if (!saveOrder) {
                            connection.rollback();
                        }
                        for (RentItemDTO itemDto : selectedItemList) {
                            saveOrderDetail = rentItemDetailDAO.addRentItemOrderDetail(new RentItemDetail(orderId, itemDto.getItemCode(), itemDto.getQty(), itemDto.getUnitPrice()));
                            if (!saveOrderDetail) {
                                connection.rollback();
                            }
                            RentItem item = rentItemDAO.searchById(itemDto.getItemCode());
                            int qtyOnHand = item.getQty();
                            if (qtyOnHand < itemDto.getQty()) {
                                Alert a = new Alert(Alert.AlertType.WARNING, "Can not rent this Qty!", ButtonType.OK);
                                a.show();
                                txtQty.selectAll();
                                txtQty.requestFocus();
                            }
                            qtyOnHand = qtyOnHand - itemDto.getQty();
                            updateSellStock = rentItemDAO.updateItemQty(new RentItem(itemDto.getItemCode(), qtyOnHand));
                            if (!updateSellStock) {
                                connection.rollback();
                            }
                        }
                    } finally {
                        connection.setAutoCommit(true);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Done!", ButtonType.OK);
                    alert.showAndWait();
                    ClearAllTxt();
                }
            }
        }
    }

    @FXML
    void OnPressEnterTxtQty(ActionEvent event) {
        if (isSelectItemFromCmb) {
            if (!txtQty.getText().matches("^\\d+$")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Qty!", ButtonType.OK);
                a.show();
            } else {
                itemCode = cmbItemCode.getValue();
                String description = txtDescriptin.getText();
                double unitPrice = Double.parseDouble(txtRentPrice.getText());
                int qty = Integer.parseInt(txtQty.getText());
                double amount = qty * unitPrice;
                subTotal = subTotal + amount;

                txtTotal.setText(formatter.format(subTotal));

                selectedItemList = tblItemList.getItems();
                RentItemDTO dto = new RentItemDTO(itemCode, description, unitPrice, qty, amount);

                int newQty = 0;
                double newAmount = 0;

                if (selectedItemList.size() != 0) {
                    for (int i = 0; i < selectedItemList.size(); i++) {
                        if (selectedItemList.get(i).getItemCode().equals(itemCode)) {
                            newQty = qty + (selectedItemList.get(i).getQty());
                            newAmount = newQty * unitPrice;
                            dto = new RentItemDTO(itemCode, description, unitPrice, newQty, newAmount);
                            selectedItemList.remove(i);
                            selectedItemList.add(dto);
                            LoadTbl();
                            txtQty.setText("");
                            isSelectItemFromCmb = false;
                            break;
                        } else {
                            selectedItemList.add(dto);
                            LoadTbl();
                            txtQty.setText("");
                            isSelectItemFromCmb = false;
                            break;
                        }
                    }
                } else {
                    selectedItemList.add(dto);
                    LoadTbl();
                    txtQty.setText("");
                    isSelectItemFromCmb = false;
                }
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "Select an Item!", ButtonType.OK);
            a.show();
        }

    }

    @FXML
    void SelectItemFromCmb(ActionEvent event) {
        try {
            RentItem item = rentItemDAO.searchById(cmbItemCode.getSelectionModel().getSelectedItem());

            if (item != null) {
                txtDescriptin.setText(item.getDescription());
                txtRentPrice.setText(item.getUnitPrice() + "");
                isSelectItemFromCmb = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(HomeSellController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void LoadTbl() {
        tblItemList.setItems(FXCollections.observableArrayList(selectedItemList));
    }

    private double subTotal = 0;
    String itemCode;
    private List<RentItemDTO> selectedItemList = new ArrayList<>();

    private CustomerDAO customerDAO = new CustomerDAO();
    private RentItemDAO rentItemDAO = new RentItemDAO();
    private RentItemDetailDAO rentItemDetailDAO = new RentItemDetailDAO();
    private RentItemOrderDAO rentItemOrderDAO = new RentItemOrderDAO();

    private NumberFormat formatter = new DecimalFormat("#0.00");

    private boolean isSelectItemFromCmb = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        LoadItemCombo();
        LoadCurrentDate();
        LoadCustomerId();
        LoadRentOrderId();
        LoadItemCodeForDelete();

        tblItemList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblItemList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItemList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblItemList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblItemList.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("amount"));

    }

    private void LoadItemCombo() {
        try {
            for (RentItem item : rentItemDAO.getAllRentItems()) {
                cmbItemCode.getItems().add(item.getItemCode());
            }

        } catch (Exception ex) {
            Logger.getLogger(HomeSellController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private String orderId;

    private void LoadRentOrderId() {
        try {
            String id = IDGenerator.getNewID("rentItemOrder", "orderId", "R");
            orderId = id;
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(HomeSellController.class.getName()).log(Level.SEVERE, null, ex);
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
                custId = c.getCustID();
                hasACustomer = true;
                cmbItemCode.requestFocus();
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Not found any previous record!", ButtonType.OK);
                a.show();
            }
        }
    }

    private boolean isSelectedRow = false;

    private void LoadItemCodeForDelete() {
        tblItemList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selectedItemList.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Table is Empty!", ButtonType.OK);
                    a.show();
                } else {
                    RentItemDTO itemDto = tblItemList.getItems().get(tblItemList.getSelectionModel().getFocusedIndex());
                    itemCode = itemDto.getItemCode();
                    isSelectedRow = true;
                }
            }
        });
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
                if (txtBorrowDate.getValue() == null) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Please insert Due date!", ButtonType.OK);
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
    }

    private void ClearAllTxt() {
        subTotal = 0;

        tblItemList.getItems().clear();

        txtCustomerName.setText("");
        txtCustomerMobile.setText("");
        txtDescriptin.setText("");
        txtRentPrice.setText("");
        txtQty.setText("");

        txtCustPrice.setText("");
        txtTotal.setText("");
        
        txtBorrowDate.setValue(null);

        selectedItemList = new ArrayList<>();
        customerDAO = new CustomerDAO();
        rentItemDAO = new RentItemDAO();
        rentItemDetailDAO = new RentItemDetailDAO();
        rentItemOrderDAO = new RentItemOrderDAO();
        LoadCustomerId();
        LoadRentOrderId();
    }

}
