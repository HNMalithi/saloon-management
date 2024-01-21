/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.RentItem;

/**
 *
 * @author User
 */
public class RentItemDAO {
    
    public boolean addRentItem(RentItem item) throws Exception{
        return CrudUtil.executeUpdate("INSERT INTO rentItem VALUES(?,?,?,?)", item.getItemCode(), item.getDescription(), item.getUnitPrice(), item.getQty()) > 0;
    }
    
    public boolean updateRentItem(RentItem item) throws Exception{
        return CrudUtil.executeUpdate("UPDATE rentItem SET description=?, rentPrice=?, qty=? WHERE itemCode=? ", 
                item.getDescription(),
                item.getUnitPrice(),
                item.getQty(),
                item.getItemCode()
        ) > 0;
        
    }
    
    public boolean updateItemQty(RentItem item) throws Exception {
        return CrudUtil.executeUpdate("UPDATE rentItem SET qty=? WHERE itemCode=? ", item.getQty(), item.getItemCode()) > 0;
    }

   public boolean deleteRentItem(String itemCode) throws Exception{
       return CrudUtil.executeUpdate("DELETE FROM rentItem WHERE itemCode=? ", itemCode) > 0;
   }
    
   public RentItem searchById(String id)throws Exception{
       
       ResultSet rst = CrudUtil.executeQuery("SELECT * FROM rentItem WHERE itemCode=? ", id);
       
       if (rst.next()) {
           return new RentItem(rst.getString("itemCode"), rst.getString("description"), rst.getDouble("rentPrice"), rst.getInt("qty"));
       }else{
           return null;
       }
       
   }
   
   public ArrayList<RentItem> getAllRentItems() throws Exception{
       
       ArrayList<RentItem> items = new ArrayList<>();
       ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM rentItem ");
       
       while (resultSet.next()) {           
           items.add(new RentItem(
                   resultSet.getString("itemCode"),
                   resultSet.getString("description"),
                   resultSet.getDouble("rentPrice"),
                   resultSet.getInt("qty")));
       }
       
       return items;
   }
}
