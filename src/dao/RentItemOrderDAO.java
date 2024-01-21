/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.RentOrderDTO;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.RentItemOrder;

/**
 *
 * @author Ishadi Kariyawasam
 */
public class RentItemOrderDAO {
    
    public boolean addOrder(RentItemOrder order) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO rentItemOrder VALUES(?,?,?,?,?,?,?,?)",
                order.getOrderId(), 
                order.getCustomerId(),
                order.getOrderDate(),
                order.getAdvancePayment(),
                order.getTotalAmount(),
                order.getBorrowDate(),
                order.getDueDate(),
                order.getRentStatus()
        ) > 0;
    }
    
    public boolean orderStatusDone(String orderId) throws Exception {
        return CrudUtil.executeUpdate("UPDATE rentItemOrder SET rentStatus='Done' WHERE orderId=? ", orderId) > 0;
    }
    
    public boolean orderStatusRemove(String orderId) throws Exception {
        return CrudUtil.executeUpdate("UPDATE rentItemOrder SET rentStatus='Removed' WHERE orderId=? ", orderId) > 0;
    }
    
    public ArrayList<RentItemOrder> getAllRentOrders() throws Exception{
       ArrayList<RentItemOrder> orders = new ArrayList<>();
       ResultSet resultSet = CrudUtil.executeQuery(
               "SELECT * FROM rentItemOrder WHERE rentStatus='Rent' ");
       
       while (resultSet.next()) {           
           orders.add(new RentItemOrder(
                   resultSet.getString("orderId"),
                   resultSet.getString("customerId"),
                   resultSet.getString("orderDate"),
                   resultSet.getDouble("advancePayment"),
                   resultSet.getDouble("totalAmount"),
                   resultSet.getString("borrowDate"),
                   resultSet.getString("dueDate"),
                   resultSet.getString("rentStatus")
           ));
       }
       
       return orders;
    }
    
}
