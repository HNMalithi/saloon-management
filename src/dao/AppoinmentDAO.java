/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.AppointmentDTO;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Appoinment;


/**
 *
 * @author User
 */
public class AppoinmentDAO {
    
    public boolean addAppoinment(Appoinment appoinment) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO appoinment VALUES(?,?,?,?,?,?,?,?)",
                appoinment.getAppoinmentNo(),
                appoinment.getCustomerId(),
                appoinment.getDescription(),
                appoinment.getAppoinmentDate(),
                appoinment.getAppoinmentTime(),
                appoinment.getStatus(),
                appoinment.getAmount(),
                appoinment.getRegDate()
        ) > 0;
    }

    public boolean updateAppoinmentStatusAndAmount(String id, double amount) throws Exception{
        return CrudUtil.executeUpdate("UPDATE appoinment SET appoinmentStatus='done', amount=? WHERE appoinmentNo=? ", amount, id) > 0;
    }
    
    public boolean cancelAppoinment(String id) throws Exception{
        return CrudUtil.executeUpdate("UPDATE appoinment SET appoinmentStatus='canceled' WHERE appoinmentNo=? ", id) > 0;
    }
    
    public boolean updateAppoinment(Appoinment appoinment) throws Exception{
        return CrudUtil.executeUpdate("UPDATE appoinment SET customerId=?, description=?, appoinmentDate=?, appoinmentDate=? WHERE appoinmentNo=? ", appoinment.getCustomerId(), appoinment.getDescription(), appoinment.getAppoinmentDate(), appoinment.getAppoinmentTime(), appoinment.getAppoinmentNo()) > 0;
    }

   public boolean deleteAppoinment(String appoinmentNo) throws Exception{
       return CrudUtil.executeUpdate("DELETE FROM appoinment WHERE appoinmentNo=? ", appoinmentNo) > 0;   
   }
   
   public Appoinment searchById(String id)throws Exception{
       
       ResultSet rst = CrudUtil.executeQuery("SELECT * FROM sellItem WHERE appoinmentNo=? ", id);
       
       if (rst.next()) {
           return new Appoinment(rst.getString("appoinmentNo"), rst.getString("customerId"), rst.getString("description"), rst.getString("appoinmentDate"), rst.getString("appoinmentTime"));
       }else{
           return null;
       }
       
   }
   
   public ArrayList<AppointmentDTO> getAllAppoinment() throws Exception{
       
       ArrayList<AppointmentDTO> appoinments = new ArrayList<>();
       ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM appoinment LEFT JOIN customer ON appoinment.customerId = customer.custId WHERE appoinmentStatus='waiting' ");
       
       while (resultSet.next()) {           
           appoinments.add(new AppointmentDTO(
                   resultSet.getString("appoinmentNo"),
                   resultSet.getString("appoinmentDate"),
                   resultSet.getString("appoinmentTime"),
                   resultSet.getString("description"),
                   resultSet.getString("custName"),
                   resultSet.getString("contactNo"),
                   resultSet.getDouble("amount")
           ));
       }
       
       return appoinments;
   }
   
   public ArrayList<Appoinment> getAllAppoinmentsForCharts() throws Exception{
       
       ArrayList<Appoinment> appoinments = new ArrayList<>();
       ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM appoinment");
       while (resultSet.next()) {           
           appoinments.add(new Appoinment(
                   resultSet.getString("appoinmentNo"),
                   resultSet.getString("customerId"),
                   resultSet.getString("description"),
                   resultSet.getString("appoinmentDate"),
                   resultSet.getString("appoinmentTime"),
                   resultSet.getString("appoinmentStatus"),
                   resultSet.getDouble("amount"),
                   resultSet.getString("regDate")
           ));
       }
       return appoinments;
   }
    
}
