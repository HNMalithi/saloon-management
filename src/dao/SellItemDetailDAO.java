/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.SellItemDetail;

/**
 *
 * @author User
 */
public class SellItemDetailDAO {
    
    public boolean addOrderDetail(SellItemDetail orderDetail) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO sellItemDetail VALUES(?,?,?,?)", orderDetail.getOrderId(), orderDetail.getItemCode(), orderDetail.getQty(), orderDetail.getUnitPrice()) > 0;
    }

//    public boolean updateItem(SellItemDetail item) throws Exception{
//        return CrudUtil.executeUpdate("UPDATE sellItemDetails SET customerId=?, itemCode=?, qty=? WHERE sellNo=? ", item.getCustomerId(), item.getItemCode(), item.getQty(), item.getSellNo()) > 0;
//    }
//
//   public boolean deleteItem(String itemCode) throws Exception{
//       return CrudUtil.executeUpdate("DELETE FROM sellItemDetail WHERE itemCode=? ", itemCode) > 0;
//       
//   }
//   
//   public SellItemDetail searchById(String id)throws Exception{
//       
//       ResultSet rst = CrudUtil.executeQuery("SELECT * FROM sellItemDetail WHERE itemCode=? ", id);
//       
//       if (rst.next()) {
//           return new SellItemDetail(rst.getString("sellNo"), rst.getString("customerId"), rst.getString("itemCode"), rst.getInt("qty"));
//       }else{
//           return null;
//       }
//       
//   }
//   
//   public ArrayList<SellItemDetail> getAllItems() throws Exception{
//       
//       ArrayList<SellItemDetail> items = new ArrayList<>();
//       ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM sellItemDetail ");
//       
//       while (resultSet.next()) {           
//           items.add(new SellItemDetail(
//                   resultSet.getString("sellNo"),
//                   resultSet.getString("customerId"),
//                   resultSet.getString("itemCode"),
//                   resultSet.getInt("qty")));
//       }
//       
//       return items;
//   }
    
}
