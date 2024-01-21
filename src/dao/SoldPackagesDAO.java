/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.SoldPackages;

/**
 *
 * @author Ishadi Kariyawasam
 */
public class SoldPackagesDAO {
    
    public boolean addSellPackage(SoldPackages packages) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO soldPackages VALUES(?,?,?,?,?)",
                packages.getSoldPackageNo(), 
                packages.getPackageId(), 
                packages.getCustomerId(), 
                packages.getPackagePrice(),
                packages.getSoldDate()
        ) > 0;
    }
    
}
