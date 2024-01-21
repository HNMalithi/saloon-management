/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.RentItemDetail;

/**
 *
 * @author User
 */
public class RentItemDetailDAO {

    public boolean addRentItemOrderDetail(RentItemDetail item) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO rentItemOrderDetail VALUES(?,?,?,?)",
                item.getOrderId(),
                item.getItemCode(),
                item.getQty(),
                item.getUnitPrice()
        ) > 0;
    }

    public ArrayList<RentItemDetail> getAllRentOrderDetails(String orderId) throws Exception{
       ArrayList<RentItemDetail> orders = new ArrayList<>();
       ResultSet resultSet = CrudUtil.executeQuery(
               "SELECT * FROM rentItemOrderDetail WHERE orderId='"+orderId+"' ");
       
       while (resultSet.next()) {           
           orders.add(new RentItemDetail(
                   resultSet.getString("orderId"),
                   resultSet.getString("itemCode"),
                   resultSet.getInt("qty"),
                   resultSet.getDouble("unitPrice")
           ));
       }
       
       return orders;
    }
}
