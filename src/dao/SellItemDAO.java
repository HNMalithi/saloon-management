/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.SellItem;

/**
 *
 * @author User
 */
public class SellItemDAO {

    public boolean addItem(SellItem item) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO sellItem VALUES(?,?,?,?)", item.getItemCode(), item.getDescription(), item.getUnitPrice(), item.getQty()) > 0;
    }

    public boolean updateItem(SellItem item) throws Exception {
        return CrudUtil.executeUpdate("UPDATE sellItem SET description=?, unitPrice=?, qty=? WHERE itemCode=? ", item.getDescription(), item.getUnitPrice(), item.getQty(), item.getItemCode()) > 0;
    }

    public boolean updateItemQty(SellItem item) throws Exception {
        return CrudUtil.executeUpdate("UPDATE sellItem SET qty=? WHERE itemCode=? ", item.getQty(), item.getItemCode()) > 0;
    }

    public boolean deleteItem(String itemCode) throws Exception {
        return CrudUtil.executeUpdate("DELETE FROM sellItem WHERE itemCode=? ", itemCode) > 0;

    }

    public SellItem searchById(String id) throws Exception {

        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM sellItem WHERE itemCode=? ", id);

        if (rst.next()) {
            return new SellItem(rst.getString("itemCode"), rst.getString("description"), rst.getDouble("unitPrice"), rst.getInt("qty"));
        } else {
            return null;
        }

    }

    public ArrayList<SellItem> getAllItems() throws Exception {

        ArrayList<SellItem> items = new ArrayList<>();
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM sellItem ");

        while (resultSet.next()) {
            items.add(new SellItem(
                    resultSet.getString("itemCode"),
                    resultSet.getString("description"),
                    resultSet.getDouble("unitPrice"),
                    resultSet.getInt("qty")));
        }

        return items;
    }

}
