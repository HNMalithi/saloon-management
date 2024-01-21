/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.Packages;

/**
 *
 * @author User
 */
public class PackagesDAO {

    public boolean addPackages(Packages packages) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO packages VALUES(?,?,?,?,?,?)", packages.getPackageID(), packages.getPackageName(), packages.getPackagePrice(), packages.getPackageDescription(), packages.getAddDate(), packages.getModifiedDate()) > 0;
    }
    
    public boolean updatePackages(Packages p) throws Exception {
        return CrudUtil.executeUpdate(
                "UPDATE packages SET packageName=?, packagePrice=?, packageDescription=?, addedDate=?, modifiedDate=? WHERE packageId=? ", 
                p.getPackageName(),
                p.getPackagePrice(),
                p.getPackageDescription(),
                p.getAddDate(),
                p.getModifiedDate(),
                p.getPackageID()
        ) >0;
    }

    public boolean deletePackages(String packageID) throws Exception {
        return CrudUtil.executeUpdate("DELETE FROM packages WHERE packageID=? ", packageID) > 0;
    }

    public ArrayList<Packages> getAllPackages() throws Exception {
        ArrayList<Packages> packages = new ArrayList<>();
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM packages ");
        while (resultSet.next()) {
            packages.add(new Packages(
                    resultSet.getString("packageID"),
                    resultSet.getString("packageName"),
                    resultSet.getDouble("packagePrice"),
                    resultSet.getString("packageDescription"))
            );
        }
        return packages;
    }

}
