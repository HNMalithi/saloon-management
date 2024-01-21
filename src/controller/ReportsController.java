/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.AppoinmentDAO;
import dto.AppointmentDTO;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import model.Appoinment;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ReportsController implements Initializable {

    @FXML
    private AnchorPane pnlTopLeft;

    @FXML
    private AnchorPane pnlTopRight;

    @FXML
    private AnchorPane pnlBottom;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            appointmentList = appoinmentDAO.getAllAppoinmentsForCharts();
        } catch (Exception ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        LoadChartTopLeft();
        LoadChartTopRight();
        LoadChartBottom();

    }

    private AppoinmentDAO appoinmentDAO = new AppoinmentDAO();
    private List<Appoinment> appointmentList = new ArrayList<>();

    private void LoadChartTopLeft() {
        int a = 0;
        int b = 0;
        for (int i = 0; i < appointmentList.size(); i++) {
            if (appointmentList.get(i).getStatus().equals("canceled")) {
                a = a + 1;
            }
            if (!appointmentList.get(i).getStatus().equals("canceled")) {
                b = b + 1;
            }
        }
        pnlTopLeft.getChildren().clear();
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        list.add(new PieChart.Data("Canceled", a));
        list.add(new PieChart.Data("Other", b));
        PieChart chart = new PieChart(list);
        chart.setTitle("Canceled Appointments");
        chart.setPrefSize(350, 350);
        pnlTopLeft.getChildren().add(chart);

    }

    private void LoadChartTopRight() {
        int a = 2;
        int b = 0;
        for (int i = 0; i < appointmentList.size(); i++) {
            if (appointmentList.get(i).getStatus().equals("done")) {
                a = a + 1;
            }
            if (!appointmentList.get(i).getStatus().equals("done")) {
                b = b + 1;
            }
        }
        pnlTopRight.getChildren().clear();
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        list.add(new PieChart.Data("Finished", a));
        list.add(new PieChart.Data("Other", b));
        PieChart chart = new PieChart(list);
        chart.setTitle("Finished Appointments");
        chart.setPrefSize(350, 350);
        pnlTopRight.getChildren().add(chart);
    }

    private void LoadChartBottom() {
        int a = 0;
        int b = 0;
        int c = 0;
        for (int i = 0; i < appointmentList.size(); i++) {
            if (appointmentList.get(i).getStatus().equals("done")) {
                a = a + 1;
            }
            if (appointmentList.get(i).getStatus().equals("canceled")) {
                b = b + 1;
            }
            if (appointmentList.get(i).getStatus().equals("waiting")) {
                c = c + 1;
            }
            pnlBottom.getChildren().clear();
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Appointment Status");
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Customers");
		BarChart chart = new BarChart(xAxis, yAxis);
		chart.setTitle("Appointment Status");
		XYChart.Series series = new XYChart.Series();
                
		series.getData().add(new XYChart.Data<>("Finished",a));
		series.getData().add(new XYChart.Data<>("Canceled",b));
		series.getData().add(new XYChart.Data<>("Waiting",c));
		chart.getData().add(series);
		chart.setPrefSize(850, 300);
		pnlBottom.getChildren().add(chart);
        }
    }

}
