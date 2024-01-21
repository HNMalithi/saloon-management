/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.SellItemOrder;

/**
 *
 * @author Perad
 */
public class SellItemOrderDAO {
    
    public boolean addOrder(SellItemOrder order) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO sellItemOrder VALUES(?,?,?) ", order.getOrderId(), order.getCustomerId(), order.getOrderDate()) > 0;
    }
    
}
