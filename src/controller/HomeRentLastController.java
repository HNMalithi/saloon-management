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
import dao.CustomerDAO;
import dao.RentItemDAO;
import dao.RentItemDetailDAO;
import dao.RentItemOrderDAO;
import dto.RentItemDTO;
import dto.RentOrderDTO;
import java.util.ArrayList;
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
import model.RentItem;
import model.RentItemDetail;
import model.RentItemOrder;

/**
 * FXML Controller class
 *
 * @author User
 */
public class HomeRentLastController implements Initializable {

    @FXML
    private TableView<RentItemOrder> tblOrder;

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
    private Label lblCustName;

    @FXML
    private Label lblContactNo;

    @FXML
    private TextField txtPayable;

    @FXML
    private Label lblAdvance;

    @FXML
    void OnClickedDelete(MouseEvent event) throws Exception {
        if (isSelectedRow) {
            boolean removeOrder = rentItemOrderDAO.orderStatusRemove(orderId);
            if (removeOrder) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Removed!", ButtonType.OK);
                alert.showAndWait();
                ClearAllTxt();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "Select Rent Order!", ButtonType.OK);
            a.show();
        }
    }

    @FXML
    void OnClickedDone(MouseEvent event) throws Exception {

        if (isSelectedRow) {
            if (!txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Payment!", ButtonType.OK);
                a.show();
                txtCustPrice.selectAll();
                txtCustPrice.requestFocus();
            }
            if (txtCustPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && (!(Double.parseDouble(txtCustPrice.getText()) >= Double.parseDouble(txtPayable.getText())))) {
                Alert a = new Alert(Alert.AlertType.WARNING, "AAA!", ButtonType.OK);
                a.show();
                txtCustPrice.selectAll();
                txtCustPrice.requestFocus();
            }
            if (txtCustPrice.getText().matches("[0-9]*") && (Double.parseDouble(txtCustPrice.getText()) >= Double.parseDouble(txtPayable.getText()))) {
                boolean saveOrder = rentItemOrderDAO.orderStatusDone(orderId);
                double change = Double.parseDouble(txtCustPrice.getText()) - Double.parseDouble(txtPayable.getText());
                if (saveOrder) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Change : Rs." + change + "\nThank You!", ButtonType.OK);
                    alert.showAndWait();
                    ClearAllTxt();
                }
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "Select Rent Order!", ButtonType.OK);
            a.show();
        }

    }

    private CustomerDAO customerDAO = new CustomerDAO();
    private RentItemDAO rentItemDAO = new RentItemDAO();
    private RentItemDetailDAO rentItemDetailDAO = new RentItemDetailDAO();
    private RentItemOrderDAO rentItemOrderDAO = new RentItemOrderDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadRentOrderTbl();
        LoadOrderTblDetails();

        tblItemList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblItemList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItemList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblItemList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));

    }

    @FXML
    void ValidatePrice(ActionEvent event) {
        if (!txtCustPrice.getText().matches("[0-9]*")) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Payment!", ButtonType.OK);
            a.show();
            txtCustPrice.selectAll();
            txtCustPrice.requestFocus();
        }
    }

    private void LoadRentOrderTbl() {
        tblOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        try {
            tblOrder.setItems(FXCollections.observableArrayList(rentItemOrderDAO.getAllRentOrders()));
            selectedItemList = rentItemOrderDAO.getAllRentOrders();
        } catch (Exception ex) {
            Logger.getLogger(HomeRentLastController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isSelectedRow = false;
    private List<RentItemOrder> selectedItemList = new ArrayList<>();
    private List<RentItemDTO> itemDetailsList = new ArrayList<>();
    private List<RentItemDetail> tempItemDetailList = new ArrayList<>();
    private String orderId;
    private String customerId;

    private void LoadOrderTblDetails() {
        tblOrder.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selectedItemList.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Table is Empty!", ButtonType.OK);
                    a.show();
                } else {
                    RentItemOrder orderDto = tblOrder.getItems().get(tblOrder.getSelectionModel().getFocusedIndex());
                    orderId = orderDto.getOrderId();
                    customerId = orderDto.getCustomerId();
                    txtTotal.setText(orderDto.getTotalAmount() + "");
                    lblAdvance.setText("Advance : Rs." + orderDto.getAdvancePayment());
                    txtPayable.setText((orderDto.getTotalAmount() - orderDto.getAdvancePayment()) + "");
                    try {
                        Customer c = customerDAO.searchById(customerId);
                        lblCustName.setText(c.getCustName());
                        lblContactNo.setText(c.getContactNo());
                        isSelectedRow = true;
                        itemDetailsList.clear();
                        tempItemDetailList = rentItemDetailDAO.getAllRentOrderDetails(orderId);
                        for (int i = 0; i < tempItemDetailList.size(); i++) {
                            RentItem item = rentItemDAO.searchById(tempItemDetailList.get(i).getItemCode());
                            RentItemDTO dto = new RentItemDTO();
                            dto.setItemCode(tempItemDetailList.get(i).getItemCode());
                            dto.setDescription(item.getDescription());
                            dto.setUnitPrice(tempItemDetailList.get(i).getUnitPrice());
                            dto.setQty(tempItemDetailList.get(i).getQty());
                            itemDetailsList.add(dto);
                        }
                        LoadTbl();
                    } catch (Exception ex) {
                        Logger.getLogger(HomeRentLastController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isSelectedRow = true;
                }
            }
        });
    }

    private void LoadTbl() {
        tblItemList.setItems(FXCollections.observableArrayList(itemDetailsList));
    }

    private boolean emptyTxtCheck() {
        if (txtCustPrice.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert cash!", ButtonType.OK);
            a.show();
            return false;
        } else {
            return true;
        }
    }

    private void ClearAllTxt() {

        tblItemList.getItems().clear();

        txtCustPrice.setText("");
        txtTotal.setText("");
        txtPayable.setText("");

        lblAdvance.setText("");
        lblCustName.setText("");
        lblContactNo.setText("");

        selectedItemList = new ArrayList<>();
        customerDAO = new CustomerDAO();
        rentItemDAO = new RentItemDAO();
        rentItemDetailDAO = new RentItemDetailDAO();
        rentItemOrderDAO = new RentItemOrderDAO();
        isSelectedRow = false;
        LoadRentOrderTbl();

    }
}
