/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.Customer;


/**
 *
 * @author User
 */
public class CustomerDAO {
    
    public boolean addCustomer(Customer c) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO customer VALUES(?,?,?,?)", c.getCustID(), c.getCustName(), c.getContactNo(), c.getRegDate()) > 0;
    }

    public boolean updatecustomer(Customer customer) throws Exception{
        return CrudUtil.executeUpdate("UPDATE customer SET custName=?, contactNo=?, qty=? WHERE custId=? ", customer.getCustName(), customer.getContactNo(), customer.getRegDate(), customer.getCustID()) > 0;
    }

   public boolean deleteCustomer(String custId) throws Exception{
       return CrudUtil.executeUpdate("DELETE FROM customer WHERE custId=? ", custId) > 0;
   }
   
   public Customer searchById(String id)throws Exception{
       ResultSet rst = CrudUtil.executeQuery("SELECT * FROM customer WHERE custId=? ", id);
       if (rst.next()) {
           return new Customer(rst.getString("custId"), rst.getString("custName"), rst.getString("contactNo"), rst.getString("custRegDate"));
       }else{
           return null;
       }
   }
   
   public Customer searchByMobile(String mobile)throws Exception{
       ResultSet rst = CrudUtil.executeQuery("SELECT * FROM customer WHERE contactNo=? ", mobile);
       if (rst.next()) {
           return new Customer(rst.getString("custId"), rst.getString("custName"), rst.getString("contactNo"), rst.getString("custRegDate"));
       }else{
           return null;
       }
   }
   
   public ArrayList<Customer> getAllItems() throws Exception{
       
       ArrayList<Customer> customers = new ArrayList<>();
       ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM customer ");
       
       while (resultSet.next()) {           
           customers.add(new Customer(
                   resultSet.getString("custId"),
                   resultSet.getString("custName"),
                   resultSet.getString("contactNo"),
                   resultSet.getString("custRegDate")));
       }
       
       return customers;
   }
    
}
