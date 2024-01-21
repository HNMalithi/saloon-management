/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import dao.PackagesDAO;
import generator.IDGenerator;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Packages;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PackagesController implements Initializable {

    @FXML
    private TableView<Packages> tblPackages;

    @FXML
    private TextField txtPackageID;

    @FXML
    private TextField txtPackageName;

    @FXML
    private TextField txtPackagePrice;

    @FXML
    private TextArea txtPackageDescription;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    String packageID;
    String packageName;
    double packagePrice;
    String packageDescription;

    @FXML
    void OnClickedAdd(MouseEvent event) {
        if (emptyTxtCheck()) {
            if (!txtPackagePrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid unitprice!", ButtonType.OK);
                a.show();
                txtPackagePrice.selectAll();
                txtPackagePrice.requestFocus();
            }
            if (txtPackagePrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                packageID = txtPackageID.getText();
                packageName = txtPackageName.getText();
                packagePrice = Double.parseDouble(txtPackagePrice.getText());
                packageDescription = txtPackageDescription.getText();

                Packages c = new Packages(packageID, packageName, packagePrice, packageDescription, curDate, curDate);
                PackagesDAO PackageDAO = new PackagesDAO();
                boolean a;
                try {
                    a = PackageDAO.addPackages(c);
                    if (a) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Package is Added!", ButtonType.OK);
                        alert.showAndWait();
                        ClearText();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(PackagesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    void OnClickedDelete(MouseEvent event) {
        if (isSelectRow) {
            packageID = txtPackageID.getText();
            try {
                boolean a = packagesDAO.deletePackages(packageID);
                if (a) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Package is Deleted!", ButtonType.OK);
                    alert.showAndWait();
                    ClearText();
                }
            } catch (Exception ex) {
                Logger.getLogger(PackagesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select Item From Table!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void OnClickedUpdate(MouseEvent event) {
        if (isSelectRow) {
            if (!txtPackagePrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Invalid unitprice!", ButtonType.OK);
                a.show();
                txtPackagePrice.selectAll();
                txtPackagePrice.requestFocus();
            }
            if (txtPackagePrice.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                packageID = txtPackageID.getText();
                packageName = txtPackageName.getText();
                packagePrice = Double.parseDouble(txtPackagePrice.getText());
                packageDescription = txtPackageDescription.getText();

                Packages c = new Packages(packageID, packageName, packagePrice, packageDescription, curDate, curDate);
                boolean a;
                try {
                    a = packagesDAO.updatePackages(c);
                    if (a) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Package is Updated!", ButtonType.OK);
                        alert.showAndWait();
                        ClearText();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(PackagesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select Item From Table!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void ClearText() {
        txtPackageID.setText("");
        txtPackageName.setText("");
        txtPackagePrice.setText("");
        txtPackageDescription.setText("");
        packagesDAO = new PackagesDAO();
        itemList = new ArrayList<>();
        isSelectRow = false;
        LoadPackageID();
        LoadPackagesTbl();
    }

    PackagesDAO packagesDAO = new PackagesDAO();
    private List<Packages> itemList = new ArrayList<>();
    boolean isSelectRow = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoadPackagesTbl();
        LoadPackageID();
        LoadTblToTxt();
        LoadCurrentDate();

    }

    private void LoadPackageID() {
        try {
            String custId = IDGenerator.getNewID("packages", "packageID", "P");
            txtPackageID.setText(custId);
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(PackagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LoadPackagesTbl() {

        tblPackages.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("packageID"));
        tblPackages.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("packageName"));
        tblPackages.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("packageDescription"));
        tblPackages.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("packagePrice"));

        try {
            tblPackages.setItems(FXCollections.observableArrayList(packagesDAO.getAllPackages()));
            itemList = packagesDAO.getAllPackages();
        } catch (Exception ex) {
            Logger.getLogger(PackagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LoadTblToTxt() {
        tblPackages.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (itemList.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Table is Empty!", ButtonType.OK);
                    a.show();
                } else {
                    Packages packages = tblPackages.getItems().get(tblPackages.getSelectionModel().getFocusedIndex());
                    txtPackageID.setText(packages.getPackageID());
                    txtPackageName.setText(packages.getPackageName());
                    txtPackagePrice.setText(packages.getPackagePrice() + "");
                    txtPackageDescription.setText(packages.getPackageDescription());
                    isSelectRow = true;
                }

            }
        });
    }

    private String curDate;

    private void LoadCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        curDate = dateFormat.format(date);
    }

    private boolean emptyTxtCheck() {
        if (txtPackageName.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please insert package name!", ButtonType.OK);
            a.show();
            return false;
        } else {
            if (txtPackageDescription.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Please insert package description!", ButtonType.OK);
                a.show();
                return false;
            } else {
                if (txtPackagePrice.getText().trim().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Please insert package price!", ButtonType.OK);
                    a.show();
                    return false;
                }
            }
        }
        return true;
    }

}
