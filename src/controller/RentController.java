/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.RentItemDAO;
import generator.IDGenerator;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.SellItem;
import model.RentItem;

/**
 * FXML Controller class
 *
 * @author User
 */
public class RentController implements Initializable {

    @FXML
    private JFXTextField txtRentItemCode;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtUnitRentPrice;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXButton btnRentAdd;

    @FXML
    private JFXButton btnRrentDelete;

    @FXML
    private TableView<RentItem> tblRentItem;

    @FXML
    private JFXButton btnRentUpdate;

    String rentItemCode;
    double unitRentPrice;
    String description;
    int qty;

    @FXML
    void OnClickedAdd(MouseEvent event) {
        if (emptyTxtCheck()) {
            if (!txtUnitRentPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid unitprice!", ButtonType.OK);
                a.show();
                txtUnitRentPrice.selectAll();
                txtUnitRentPrice.requestFocus();
            }
            if (!txtQty.getText().matches("^\\d+$")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Qty!", ButtonType.OK);
                a.show();
                txtQty.selectAll();
                txtQty.requestFocus();
            }
            if (txtUnitRentPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && txtQty.getText().matches("^\\d+$")) {
                rentItemCode = txtRentItemCode.getText();
                unitRentPrice = Double.parseDouble(txtUnitRentPrice.getText());
                description = txtDescription.getText();
                qty = Integer.parseInt(txtQty.getText());

                RentItem c = new RentItem(rentItemCode, description, unitRentPrice, qty);
                rentItemDAO = new RentItemDAO();
                boolean a;
                try {
                    a = rentItemDAO.addRentItem(c);
                    if (a) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rent Item is Added!", ButtonType.OK);
                        alert.showAndWait();
                        ClearText();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(RentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @FXML
    void OnClickedDelete(MouseEvent event) {
        if (isSelectRow) {
            rentItemCode = txtRentItemCode.getText();
            try {
                boolean a = rentItemDAO.deleteRentItem(rentItemCode);
                if (a) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item is Deleted!", ButtonType.OK);
                    alert.showAndWait();
                    ClearText();
                }
            } catch (Exception ex) {
                Logger.getLogger(RentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select Item From Table!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void OnClickedUpdate(MouseEvent event) {
        if (isSelectRow) {
            if (emptyTxtCheck()) {
                if (!txtUnitRentPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Invalid unitprice!", ButtonType.OK);
                    a.show();
                    txtUnitRentPrice.selectAll();
                    txtUnitRentPrice.requestFocus();
                }
                if (!txtQty.getText().matches("^\\d+$")) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Qty!", ButtonType.OK);
                    a.show();
                    txtQty.selectAll();
                    txtQty.requestFocus();
                }
                if (txtUnitRentPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && txtQty.getText().matches("^\\d+$")) {
                    rentItemCode = txtRentItemCode.getText();
                    unitRentPrice = Double.parseDouble(txtUnitRentPrice.getText());
                    description = txtDescription.getText();
                    qty = Integer.parseInt(txtQty.getText());

                    RentItem c = new RentItem(rentItemCode, description, unitRentPrice, qty);
                    boolean a;
                    try {
                        a = rentItemDAO.updateRentItem(c);
                        if (a) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rent Item is Updated!", ButtonType.OK);
                            alert.showAndWait();
                            ClearText();
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(RentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select Item From Table!", ButtonType.OK);
            alert.showAndWait();
        }

    }

    private void ClearText() {
        txtRentItemCode.setText("");
        txtUnitRentPrice.setText("");
        txtDescription.setText("");
        txtQty.setText("");
        rentItemDAO = new RentItemDAO();
        itemList = new ArrayList<>();
        LoadItemCode();
        LoadRentItemTbl();
        isSelectRow = false;
    }

    RentItemDAO rentItemDAO = new RentItemDAO();
    private List<RentItem> itemList = new ArrayList<>();
    boolean isSelectRow = false;

    public void initialize(URL location, ResourceBundle resources) {

        LoadRentItemTbl();
        LoadItemCode();

        LoadTblToTxt();

    }

    private void LoadItemCode() {
        try {
            String custId = IDGenerator.getNewID("rentItem", "itemCode", "I");
            txtRentItemCode.setText(custId);
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(RentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LoadRentItemTbl() {

        tblRentItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblRentItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblRentItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblRentItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));

        try {
            tblRentItem.setItems(FXCollections.observableArrayList(rentItemDAO.getAllRentItems()));
            itemList = rentItemDAO.getAllRentItems();
        } catch (Exception ex) {
            Logger.getLogger(RentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LoadTblToTxt() {
        tblRentItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (itemList.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Table is Empty!", ButtonType.OK);
                    a.show();
                } else {
                    RentItem item = tblRentItem.getItems().get(tblRentItem.getSelectionModel().getFocusedIndex());
                    txtRentItemCode.setText(item.getItemCode());
                    txtDescription.setText(item.getDescription());
                    txtUnitRentPrice.setText(item.getUnitPrice() + "");
                    txtQty.setText(item.getQty() + "");
                    isSelectRow = true;
                }
            }
        });
    }

    private boolean emptyTxtCheck() {
        if (txtDescription.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert description!", ButtonType.OK);
            a.show();
            return false;
        } else {
            if (txtUnitRentPrice.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Please insert unit price!", ButtonType.OK);
                a.show();
                return false;
            } else {
                if (txtQty.getText().trim().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Please insert qty!", ButtonType.OK);
                    a.show();
                    return false;
                }
            }
        }
        return true;
    }

}
