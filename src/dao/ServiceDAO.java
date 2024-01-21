/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.SellItem;
import model.Service;

/**
 *
 * @author User
 */
public class ServiceDAO {
    
    public boolean addService(Service service) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO service VALUES(?,?,?,?,?)", 
                service.getServiceNo(), 
                service.getCustomerId(), 
                service.getDescription(), 
                service.getServiceCharge(), 
                service.getServiceDate()
        ) > 0;
    }
   
   public ArrayList<Service> getAllItems() throws Exception{
       
       ArrayList<Service> services = new ArrayList<>();
       ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM service ");
       
       while (resultSet.next()) {           
           services.add(new Service(
                   resultSet.getString("serviceNo"),
                   resultSet.getString("customerId"),
                   resultSet.getString("description"),
                   resultSet.getDouble("serviceCharge"),
                   resultSet.getString("serviceDate")));
       }
       
       return services;
   }
    
}
