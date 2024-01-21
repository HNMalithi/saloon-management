/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.SellItemDAO;
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

/**
 * FXML Controller class
 *
 * @author User
 */
public class SallesStockController implements Initializable {

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<SellItem> tblSellStock;

    String itemCode;
    String description;
    double unitPrice;
    int qty;

    @FXML
    void OnClickedAdd(MouseEvent event) {
        if (emptyTxtCheck()) {
            if (!txtUnitPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid unit price!", ButtonType.OK);
                a.show();
                txtUnitPrice.selectAll();
                txtUnitPrice.requestFocus();
            }
            if (!txtQty.getText().matches("^\\d+$")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Qty!", ButtonType.OK);
                a.show();
                txtQty.selectAll();
                txtQty.requestFocus();
            }
            if (txtUnitPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && txtQty.getText().matches("^\\d+$")) {
                itemCode = txtItemCode.getText();
                description = txtDescription.getText();
                unitPrice = Double.parseDouble(txtUnitPrice.getText());
                qty = Integer.parseInt(txtQty.getText());

                SellItem c = new SellItem(itemCode, description, unitPrice, qty);

                boolean a;
                try {
                    a = itemDAO.addItem(c);
                    if (a) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item is Added!", ButtonType.OK);
                        alert.showAndWait();
                        ClearText();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SallesStockController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    @FXML
    void OnClickedDelete(MouseEvent event) {
        if (isSelectRow) {
            itemCode = txtItemCode.getText();
            try {
                boolean a = itemDAO.deleteItem(itemCode);
                if (a) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item is Deleted!", ButtonType.OK);
                    alert.showAndWait();
                    ClearText();
                }
            } catch (Exception ex) {
                Logger.getLogger(SallesStockController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select Item From Table!", ButtonType.OK);
            alert.showAndWait();
        }

    }

    @FXML
    void OnClickedUpdate(MouseEvent event) {
        if (isSelectRow) {
            if (emptyTxtCheck()) {
                if (!txtUnitPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Invalid unitprice!", ButtonType.OK);
                    a.show();
                    txtUnitPrice.selectAll();
                    txtUnitPrice.requestFocus();
                }
                if (!txtQty.getText().matches("^\\d+$")) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Invalid Qty!", ButtonType.OK);
                    a.show();
                    txtQty.selectAll();
                    txtQty.requestFocus();
                }
                if (txtUnitPrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?") && txtQty.getText().matches("^\\d+$")) {
                    itemCode = txtItemCode.getText();
                    description = txtDescription.getText();
                    unitPrice = Double.parseDouble(txtUnitPrice.getText());
                    qty = Integer.parseInt(txtQty.getText());

                    SellItem c = new SellItem(itemCode, description, unitPrice, qty);

                    boolean a;
                    try {
                        a = itemDAO.updateItem(c);
                        if (a) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item is Updated!", ButtonType.OK);
                            alert.showAndWait();
                            ClearText();
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(SallesStockController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select Item From Table!", ButtonType.OK);
            alert.showAndWait();
        }

    }

    private void ClearText() {
        txtItemCode.setText("");
        txtDescription.setText("");
        txtQty.setText("");
        txtUnitPrice.setText("");
        itemDAO = new SellItemDAO();
        itemList = new ArrayList<>();
        LoadItemCode();
        LoadItemTbl();
        isSelectRow = false;
    }

    SellItemDAO itemDAO = new SellItemDAO();
    private List<SellItem> itemList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoadItemTbl();
        LoadItemCode();
        LoadTblToTxt();

    }

    private void LoadItemCode() {
        try {
            String custId = IDGenerator.getNewID("sellItem", "itemCode", "I");
            txtItemCode.setText(custId);
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(SallesStockController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LoadItemTbl() {

        tblSellStock.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblSellStock.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblSellStock.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblSellStock.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));

        try {
            tblSellStock.setItems(FXCollections.observableArrayList(itemDAO.getAllItems()));
            itemList = itemDAO.getAllItems();
        } catch (Exception ex) {
            Logger.getLogger(SallesStockController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean isSelectRow = false;

    private void LoadTblToTxt() {
        tblSellStock.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (itemList.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Table is Empty!", ButtonType.OK);
                    a.show();
                } else {
                    SellItem item = tblSellStock.getItems().get(tblSellStock.getSelectionModel().getFocusedIndex());
                    txtItemCode.setText(item.getItemCode());
                    txtDescription.setText(item.getDescription());
                    txtUnitPrice.setText(item.getUnitPrice() + "");
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
            if (txtUnitPrice.getText().trim().isEmpty()) {
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
